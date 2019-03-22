/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import com.google.maps.model.LatLng;
import dao.JpaUtil;
import dao.daoClient;
import dao.daoIntervention;
import dao.daoEmploye;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import metier.modele.Client;
import metier.modele.Employe;
import metier.modele.Intervention;
import util.Message;
import util.GeoTest;

import static util.GeoTest.getFlightDistanceInKm;


/**
 *
 * @author njeanne
 */
public class Service {
    
   
    
    
    public daoClient dC;
    public daoIntervention dI;
    public daoEmploye dE;
    public Service() {
        dC = new daoClient();
        dI = new daoIntervention();
        dE =  new daoEmploye();
    }

    public void envoieMailInscription(Client c)
    {
        StringWriter corps = new StringWriter();
        PrintWriter mailWriter = new PrintWriter(corps);
        mailWriter.println("Bonjour "+c.getPrenom()+",");
        mailWriter.println("Nous vous confirmons votre inscription au Service Proact'if . Votre numéro de client est : "+c.getId()+".");
        mailWriter.println();
        mailWriter.println("A bientôt sur Proact'if !");

        Message.envoyerMail(
                "contact@proact.if",
                c.getEmail(),
                "Bienvenue chez PROACT'IF",
                corps.toString()
        );
    }

    public void envoieMailEchec(Client c)
    {
        StringWriter corps = new StringWriter();
        PrintWriter mailWriter = new PrintWriter(corps);
        mailWriter.println("Bonjour " + c.getPrenom() + ",");
        mailWriter.println("Votre inscription sur les services Proact'if a malheureusement échouée...");
        mailWriter.println("Merci de recommencer ultérieurement");

        Message.envoyerMail(
                "contact@proact.if",
                c.getEmail(),
                "Echec de l'inscription PROACT'IF",
                corps.toString()
        );
    }

    public void envoieNotifEmploye(Intervention i)
    {
        StringWriter message = new StringWriter();
        PrintWriter notificationWriter = new PrintWriter(message);

        notificationWriter.println("Intervention de type ???? demandée le "+i.getHeureD());
        notificationWriter.print("Pour "+i.getClient().getPrenom()+ " "+i.getClient().getNom()+
                " ("+i.getClient().getId()+"), ");
        notificationWriter.print(i.getClient().getAdresse()+". <<"+i.getDescription()+" >>.");
        notificationWriter.println("Trajet : "+
                getFlightDistanceInKm( i.getEmploye().getCoord(),i.getClient().getCoord())+" km.");

        Message.envoyerNotification(
                i.getEmploye().getNum(),
                message.toString()
        );

    }

    public LatLng calculCoord(String s)
    {
        return GeoTest.getLatLng(s);
    }

    public boolean creerClient(Client c)
    {
        boolean r = true;
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try{
            c.setCoord(calculCoord(c.getAdresse()));
            dC.ajouterClient(c);
            JpaUtil.validerTransaction();
            envoieMailInscription(c);
        }
        catch(Exception e)
        {
            r = false;
            JpaUtil.annulerTransaction();
            envoieMailEchec(c);
        }

        JpaUtil.fermerEntityManager();
        return r;
    }
    
    public List<Client> listerClients()
    {
        JpaUtil.creerEntityManager();
        List<Client> l = dC.listerClients();
        JpaUtil.fermerEntityManager();
        return l;      
    }

    public boolean creerEmploye(Employe c)
    {
        boolean r = true;
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try{
            c.setCoord(calculCoord(c.getAdresse()));
            dE.ajouterEmploye(c);
            JpaUtil.validerTransaction();
        }
        catch(Exception e)
        {
            r = false;
            JpaUtil.annulerTransaction();
        }

        JpaUtil.fermerEntityManager();
        return r;
    }

    public List<Employe> listerEmployesDispo(Date d)
    {
        JpaUtil.creerEntityManager();
        List<Employe> l = dE.listerEmployesDispo(new Time(d.getHours(), d.getMinutes(), d.getSeconds()));
        JpaUtil.fermerEntityManager();
        return l;
    }

    public List<Employe> listerEmployes(Time t)
    {
        JpaUtil.creerEntityManager();
        List<Employe> l = dE.listerEmployes(t);
        JpaUtil.fermerEntityManager();
        return l;
    }

    public boolean attribuerEmploye(Intervention i){
        boolean r = false; //Aucun employe dispo
        List<Employe> l = dE.listerEmployesDispo(new Time(i.getHeureD().getHours(),
                i.getHeureD().getMinutes(), i.getHeureD().getSeconds()));
        Employe e = null;
        if(l.size() > 0)
        {
            r = true; //Il y a au moins 1 employe dispo
            double distance = 100000000;
            for(Employe ee : l)
                if (getFlightDistanceInKm(i.getClient().getCoord(), ee.getCoord()) < distance) e = ee;
        }

        if(r) i.setEmploye(e);

        return r;
    }

    public void demandeIntervention(Client c, Intervention i){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        dI.ajouterIntervention(i);
        i.setClient(c);

        if (attribuerEmploye(i))
        {
            envoieNotifEmploye(i);
            i.setStatut("En cours");
            dI.ajouterIntervention(i);
            JpaUtil.validerTransaction();
        }
        else
        {
            JpaUtil.annulerTransaction();
        }
        JpaUtil.fermerEntityManager();
    }
    
    
    
    public List <Intervention> getInterventionsClient(Client c){
        //JpaUtil.creerEntityManager();

        List <Intervention> l = c.getListeInter();
        
        //JpaUtil.fermerEntityManager();
        
        return l;
    }

    
    public Intervention getInterventionAct(Employe e){
        // L'intervention actuelle est la dernière de la liste
        
        JpaUtil.creerEntityManager();
        
        List <Intervention> l = e.getListeInter();
        
        JpaUtil.fermerEntityManager();
        
        return l.get(l.size()-1); // pas sûre : méthode d'en dessous plus sûre mais prend plus de taille en mémoire
        
        /*AUTRE METHODE :
        
        Intervention i = new Intervention();
        
        for (Intervention k : l){
            i=k;
        }
        return i;


        daoIntervention : select i from intervention i where employee = :empl and status = 'en cours' row number >=1
        */
    }

    
    public List <Intervention> getInterventionJour(Employe e, Date d){
        JpaUtil.creerEntityManager();

        List <Intervention> inter = new ArrayList<Intervention>();

        List <Intervention> l = e.getListeInter();
 
        for (Intervention i : l){
            if(i.getHeureF().compareTo(d)==0)
            {
                inter.add(i);
            }
        }

        // dI select i from intervention i where employe = e and date = auj

        JpaUtil.fermerEntityManager();
        return inter;
    }


   
}

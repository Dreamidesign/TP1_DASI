/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.JpaUtil;
import dao.daoClient;
import dao.daoIntervention;
import dao.daoEmploye;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import metier.modele.*;
import util.DebugLogger;

//import static util.GeoTest.getFlightDistanceInKm;

/**
 *
 * @author njeanne
 */
public class Service {
    public daoClient dC;
    public daoIntervention dI;
    public daoEmploye dE;
    public ServiceAffichage serviceAffichage;
    public ServiceGeo serviceGeo;

    public Service() {
        dC = new daoClient();
        dI = new daoIntervention();
        dE = new daoEmploye();
        serviceAffichage = new ServiceAffichage();
        serviceGeo = new ServiceGeo();
    }

    public boolean inscrireClient(Client c)
    {
        boolean r = true;
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try{
            c.setCoord(serviceGeo.calculCoord(c.getAdresse()));
            dC.ajouterClient(c);
            JpaUtil.validerTransaction();
            serviceAffichage.envoieMailInscription(c);
        }
        catch(Exception e)
        {
            r = false;
            JpaUtil.annulerTransaction();
            DebugLogger.log("Impossible de créer le client.");
            serviceAffichage.envoieMailEchec(c);
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

    public void initEmploye()
    {
        Employe tab[] = {
                new Employe("Barack", "Afritte", "8 rue des lilas, Lyon", "0909090909",
                        "makeBelgiumgr8again@bg.bg", "banane",
                        new Time(0, 0, 0), new Time(23,59, 0)),
                new Employe("Trump", "Ette", "12 rue des lilas, Lyon", "0789789088",
                        "trmp@bg.bg", "merica",
                        new Time(8, 0, 0), new Time(16, 0, 0)),
                new Employe("Jean", "Dark", "30 avenue albert einstein, Villeurbanne", "0987657899",
                        "insa@bg.bg", "insa",
                        new Time(8,00,00), new Time(23,59,00)),
                new Employe("Angel", "Amarqué", "70 rue des lilas, Lyon", "0678908766",
                        "wolkswagen@bg.bg", "GER",
                        new Time(10, 0, 0), new Time(20,00, 0)),
                new Employe("Pablito", "Escobar", "01 rue des lilas, Lyon", "7778880089",
                        "cocahojas@bg.bg", "aina",
                        new Time(20, 0, 0), new Time(8,0, 0)),
                new Employe("France", "oies", "5 rue des lilas, Lyon", "0679908766",
                        "hollande@bg.bg", "macaron",
                        new Time(17, 0, 0), new Time(5,0, 0))
        };

        JpaUtil.creerEntityManager();

        for (Employe employe : tab)
        {
            JpaUtil.ouvrirTransaction();
            try {
                employe.setCoord(serviceGeo.calculCoord(employe.getAdresse()));
                dE.ajouterEmploye(employe);
                JpaUtil.validerTransaction();
            } catch (Exception e) {
                JpaUtil.annulerTransaction();
                DebugLogger.log("Erreur lors de la création de l'employe" + employe.getPrenom() + employe.getNom());
            }
        }

        JpaUtil.fermerEntityManager();
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

    public void demandeIntervention(Client c, Intervention i)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        c = dC.rechercherClientParId(c); //pour les besoins du test
        i.setClient(c);

        boolean r = false; //Aucun employe dispo
        List<Employe> l = dE.listerEmployesDispo(new Time(i.getHeureD().getHours(),
                i.getHeureD().getMinutes(), i.getHeureD().getSeconds()));
        Employe e = null;
        if(l.size() > 0) //Il y a au moins 1 employe dispo
        {
            double distance = 100000000;
            for(Employe ee : l)
            {
                double d = serviceGeo.getFlightDistanceInKm(i.getClient().getCoord(), ee.getCoord());
                if ( d < distance)
                {
                    e = ee;
                    distance = d;
                }
            }

            e.setStatus(1);
            i.setEmploye(e);
            i.setStatut(1);
            dI.ajouterIntervention(i);
            JpaUtil.validerTransaction();
            serviceAffichage.envoieNotifEmploye(i);
        }
        else
        {
            JpaUtil.annulerTransaction();
            DebugLogger.log("La demande d'intervention a échoué, aucun employé disponible");
        }
        JpaUtil.fermerEntityManager();
    }

    public List <Intervention> getInterventionsClient(Client c)
    {
        JpaUtil.creerEntityManager();
        List <Intervention> l = dI.listerInterventionsClient(c);
        JpaUtil.fermerEntityManager();
        if(l.size() == 0) DebugLogger.log("Le client n'a aucune interventions");
        return l;
    }

    public Intervention getInterventionAct(Employe e)
    {
        // L'intervention actuelle est la dernière de la liste
        JpaUtil.creerEntityManager();
        Intervention i;
        try {
            i = dI.getInterventionAct(e);
        }
        catch (Exception ee)
        {
            i = null;
            DebugLogger.log("Aucune intervention en cours");
        }
        JpaUtil.fermerEntityManager();
        return i;
    }

    public List <Intervention> getInterventionJour(Employe e, Date d)
    {
        JpaUtil.creerEntityManager();
        List<Intervention> l = dI.getInterventionJour(e, d);
        JpaUtil.fermerEntityManager();
        return l;
    }

    /** Return un client si ok, null sinon **/
    public Client connexionClient(String mail, String mdp)
    {
        JpaUtil.creerEntityManager();
        Client c = dC.connexion(mail, mdp);
        if(c == null) DebugLogger.log("Impossible de se connecter, email ou mot de passe incorrect");
        else DebugLogger.log("Vous êtes connecté !");
        JpaUtil.fermerEntityManager();
        return c;
    }
    
    public Employe connexionEmploye(String mail, String mdp)
    {
        JpaUtil.creerEntityManager();
        Employe e = dE.connexion(mail, mdp);
        if(e == null) DebugLogger.log("Impossible de se connecter, email ou mot de passe incorrect");
        else DebugLogger.log("Vous êtes connecté !");
        JpaUtil.fermerEntityManager();
        return e;
    }

    public boolean validerIntervention(Intervention i, String com)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            dI.setParametresIntervention(i, com, 2); //Fonctionne mais lie pas a i
            dE.setDispo(dI.rechercherInterventionParId(i).getEmploye(), 0);
            JpaUtil.validerTransaction();
            serviceAffichage.envoieNotifClient(dI.rechercherInterventionParId(i));
        }catch (Exception e)
        {
            JpaUtil.annulerTransaction();
            DebugLogger.log("Erreur lors de la validation de l'intervention");
        }
        JpaUtil.fermerEntityManager();

        return true;
    }

    public boolean echecIntervention(Intervention i, String com)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            dI.setParametresIntervention(i, com, 3);
            dE.setDispo(dI.rechercherInterventionParId(i).getEmploye(), 0);
            JpaUtil.validerTransaction();
            serviceAffichage.envoieNotifClient(dI.rechercherInterventionParId(i));
        }catch (Exception e)
        {
            JpaUtil.annulerTransaction();
            DebugLogger.log("Erreur lors de l'echec de l'intervention");
        }
        JpaUtil.fermerEntityManager();

        return false;
    }
}

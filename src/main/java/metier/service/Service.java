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

import static util.GeoTest.getFlightDistanceInKm;

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
                new Employe("Trump", "Ette", "12 rue des lilas, Lyon", "0909090909",
                        "makeBelgiumgr8again@bg.bg", "banane",
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

        for(int i=0; i<tab.length; i++)
        {
            JpaUtil.ouvrirTransaction();
            try{
                tab[i].setCoord(serviceGeo.calculCoord(tab[i].getAdresse()));
                dE.ajouterEmploye(tab[i]);
                JpaUtil.validerTransaction();
            }
            catch(Exception e)
            {
                JpaUtil.annulerTransaction();
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
                if (getFlightDistanceInKm(i.getClient().getCoord(), ee.getCoord()) < distance) e = ee;

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
        }
        JpaUtil.fermerEntityManager();
    }

    public List <Intervention> getInterventionsClient(Client c)
    {
        JpaUtil.creerEntityManager();
        List <Intervention> l = dI.listerInterventionsClient(c);
        JpaUtil.fermerEntityManager();
        return l;
    }

    public Intervention getInterventionAct(Employe e)
    {
        // L'intervention actuelle est la dernière de la liste
        JpaUtil.creerEntityManager();
        Intervention i = dI.getInterventionAct(e);
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
        JpaUtil.fermerEntityManager();
        return c;
    }
    
    public Employe connexionEmploye(String mail, String mdp)
    {
        JpaUtil.creerEntityManager();
        Employe e = dE.connexion(mail, mdp);
        JpaUtil.fermerEntityManager();
        return e;
    }

    public boolean validerIntervention(Intervention i, String com)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            dI.setParametresIntervention(i, com, 2);
            JpaUtil.validerTransaction();
            dE.setDispo(i.getEmploye(), 0);
            serviceAffichage.envoieNotifClient(i);
        }catch (Exception e)
        {
            JpaUtil.annulerTransaction();
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
            JpaUtil.validerTransaction();
            dE.setDispo(i.getEmploye(), 0);
            serviceAffichage.envoieNotifClient(i);
        }catch (Exception e)
        {
            JpaUtil.annulerTransaction();
        }
        JpaUtil.fermerEntityManager();

        return false;
    }
}

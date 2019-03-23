/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import dao.JpaUtil;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import metier.modele.*;
import metier.service.Service;
import sun.security.ssl.Debug;
import util.DebugLogger;


/**
 *
 * @author njeanne
 */
public class Test {

    private static void testFonctionnement(Service s)
    {
        DebugLogger.log("Initialisation des employes");
        s.initEmploye();

        DebugLogger.log("Inscription d'un client");
        s.inscrireClient(new Client("Nathan","J", "Mr", "22/08/2004", "6 rue des Lilas, Lyon", "0658763255", "nathan@gmail.com", "margaux"));

        DebugLogger.log("Connexion...");
        Client current = s.connexionClient("nathan@gmail.com", "margaux");

        DebugLogger.log("Demande d'un intervention");
        Intervention i = new Animal("Kiki", "Il adore le parc et les jeux");
        s.demandeIntervention(current, i);

        Intervention ii = s.getInterventionAct(i.getEmploye());

        DebugLogger.log("Validation de l'intervention");
        s.validerIntervention(ii,"Kiki a été très calme, merci pour ce moment");

        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);

        DebugLogger.log("Historique intervention Employe");
        for(Intervention inte : s.getInterventionJour(ii.getEmploye(), new Date()))
            System.out.println(inte);

        DebugLogger.log("Deconnexion");
        current = null;
        DebugLogger.log("Inscription d'un autre client");
        s.inscrireClient(
                new Client("Margaux","P", "Mme", "07/08/2010", "5 avenue albert einstein, Villeurbanne",
                        "0658889900", "maca@gmail.com", "aedi"));

        DebugLogger.log("Test de connexion avec un mot de passe incorrect");
        current = s.connexionClient("maca@gmail.com", "abc");
        DebugLogger.log("Test de connexion client avec un mail et un mdp employé");
        current = s.connexionClient("insa@bg.bg", "insa");
        DebugLogger.log("Connexion avec le bon mdp");
        current = s.connexionClient("maca@gmail.com", "aedi");

        DebugLogger.log("Demande d'une intervention");
        Intervention i2 = new Livraison("Gateau", "Amazon", "Un peu lourd");
        s.demandeIntervention(current, i2);
        i2 = s.getInterventionAct(i2.getEmploye());

        DebugLogger.log("Validation de l'intervention");
        s.validerIntervention(i2,"RAS");

        DebugLogger.log("Demande d'une autre intervention");
        Intervention i3 = new Incident("Robinet fuit");
        s.demandeIntervention(current, i3);
        i3 = s.getInterventionAct(i3.getEmploye());

        DebugLogger.log("Fin de l'intervention");
        s.echecIntervention(i3,"1m d'eau, impossible");

        DebugLogger.log("Demande d'une autre intervention");
        Intervention i4 = new Incident("Besoin de pates en toute urgence");
        s.demandeIntervention(current, i4);
        i4 = s.getInterventionAct(i4.getEmploye());

        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);



    }
    
       

    public static void main(String[] args){
        
       JpaUtil.init();
      
       Service s = new Service();

       testFonctionnement(s);

       JpaUtil.destroy();
    }

    
    
}

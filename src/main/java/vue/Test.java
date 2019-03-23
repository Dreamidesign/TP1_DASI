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
    }
    
       

    public static void main(String[] args){
        
       JpaUtil.init();
      
       Service s = new Service();

       testFonctionnement(s);

       JpaUtil.destroy();
    }

    
    
}

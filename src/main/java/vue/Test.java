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
import java.util.Scanner;

import metier.modele.*;
import metier.service.Service;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.security.ssl.Debug;
import util.DebugLogger;

public class Test {

    private static void testFonctionnement(Service s)
    {
        DebugLogger.log("Initialisation des employes");
        s.initEmploye();
        Scanner sc = new Scanner(System.in);

        //////////// MODULE D'INSCRPTION ////////////
        DebugLogger.log("Inscription d'un client");
        System.out.println("----- Veuillez saisir vos informations -----");
        System.out.println("Prenom : ");
        String prenom = sc.nextLine();
        System.out.println("Nom : ");
        String nom = sc.nextLine();
        System.out.println("Civilité : ");
        String civilite = sc.nextLine();
        System.out.println("Date de naissance : (dd/mm/yyyy");
        String dob = sc.nextLine();
        System.out.println("Adresse : ");
        String adresse = sc.nextLine();
        System.out.println("Numéro de téléphone :");
        String numero = sc.nextLine();
        System.out.println("Adresse email :");
        String email = sc.nextLine();
        System.out.println("Mot de passe : ");
        String mdp = sc.nextLine();

        s.inscrireClient(new Client(prenom,nom, civilite, dob, adresse, numero, email, mdp));


        //////////// MODULE DE CONNEXION ////////////
        System.out.println("----- Connectez vous -----");
        Client current = null;
        while(current == null)
        {
            System.out.println("Adresse email :");
            email = sc.nextLine();
            System.out.println("Mot de passe : ");
            mdp = sc.nextLine();
            DebugLogger.log("Connexion...");
            current = s.connexionClient(email, mdp);
        }

        //////////// MODULE DE DEMANDE D'UNE INTERVENTION : ANIMAL ////////////
        DebugLogger.log("Demande d'un intervention");

        System.out.println("----- Demande d'une intervention de type : Animal -----");
        System.out.println("Animal de compagnie");
        String animal = sc.nextLine();
        System.out.println("Description");
        String des = sc.nextLine();

        Intervention i = new Animal(animal, des);
        s.demandeIntervention(current, i);

        /////////// MODULE HISTORIQUE INTERVENTION CLIENT ///////////
        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);

        ////////// DECONNEXION CLIENT ///////////

        DebugLogger.log("Deconnexion...");
        current = null;

        /////////// MODULE DE RECUPERATION DE l'INTERVENTION EN COURS ///////////
        DebugLogger.log("Connexion en tant qu'employé");
        Employe currentE = s.connexionEmploye("insa@bg.bg", "insa");

        DebugLogger.log("Récupération de l'intervention en cours de l'employé");
        Intervention currentI = s.getInterventionAct(currentE);

        //////////// VALIDATION DE L'INTERVENTION /////////////
        DebugLogger.log("Validation de l'intervention");
        s.validerIntervention(currentI, "Kiki a été très calme, merci pour ce moment");

        ////////// MODULE HISTORIQUE INTERVENTION EMPLOYE /////////
        DebugLogger.log("Historique intervention Employe");
        for(Intervention inte : s.getInterventionJour(currentE, new Date()))
            System.out.println(inte);

        ///////// DECONNEXION EMPLOYE ///////
        DebugLogger.log("Deconnexion...");
        currentE = null;

        //////////// MODULE DE CONNEXION ////////////
        System.out.println("----- Connectez vous -----");
        current = null;
        while(current == null)
        {
            System.out.println("Adresse email :");
            email = sc.nextLine();
            System.out.println("Mot de passe : ");
            mdp = sc.nextLine();
            DebugLogger.log("Connexion...");
            current = s.connexionClient(email, mdp);
        }

        //////////// MODULE DE DEMANDE D'UNE INTERVENTION : LIVRAISON ////////////
        DebugLogger.log("Demande d'un intervention");

        System.out.println("----- Demande d'une intervention de type : Livraison -----");
        System.out.println("Colis : ");
        String colis = sc.nextLine();
        System.out.println("Societe de livraison : ");
        String societe = sc.nextLine();
        System.out.println("Description : ");
        des = sc.nextLine();

        i = new Livraison(colis, societe, des);
        s.demandeIntervention(current, i);

        //////////// MODULE DE DEMANDE D'UNE INTERVENTION : INCIDENT ////////////
        DebugLogger.log("Demande d'un intervention");

        System.out.println("----- Demande d'une intervention de type : Incident -----");
        System.out.println("Description : ");
        des = sc.nextLine();

        i = new Incident(des);
        s.demandeIntervention(current, i);

        /////////// MODULE HISTORIQUE INTERVENTION CLIENT ///////////
        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);

        /*DebugLogger.log("Inscription d'un autre client");
        s.inscrireClient(
                new Client("Margaux","P", "Mme", "07/08/2010", "5 avenue albert " +
                        "einstein, Villeurbanne", "0658889900", "maca@gmail.com", "aedi"));

        DebugLogger.log("Test de connexion avec un mot de passe incorrect");
        current = s.connexionClient("maca@gmail.com", "abc");
        DebugLogger.log("Test de connexion client avec un mail et un mdp employé");
        current = s.connexionClient("insa@bg.bg", "insa");
        DebugLogger.log("Connexion avec le bon mdp");
        current = s.connexionClient("maca@gmail.com", "aedi");*/

        DebugLogger.log("Demande d'une autre intervention");
        Intervention i3 = new Incident("Robinet fuit");
        s.demandeIntervention(current, i3);
        //i3 = s.getInterventionAct(i3.getEmploye());

        DebugLogger.log("Demande d'une autre intervention");
        Intervention i4 = new Incident("Besoin de pates en toute urgence");
        s.demandeIntervention(current, i4);
        //i4 = s.getInterventionAct(i4.getEmploye());

        /////////// MODULE HISTORIQUE INTERVENTION CLIENT ///////////
        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);




        DebugLogger.log("Intervention en cours de l'employé de Villeurbanne");
        System.out.println(s.getInterventionAct(i3.getEmploye()));

        DebugLogger.log("On ajoute encore 4 interventions pour occuper tous les employés");
        Intervention tabInterv[] = {
                new Incident("Commande de caviar"),
                new Incident("Besoin de shampoing"),
                new Animal("Chouquette","-"),
                new Livraison("colis", "UPS","Laisser sur le pallier"),
        };

        for(Intervention iii : tabInterv)
        {
            s.demandeIntervention(current, iii);
            //iii = s.getInterventionAct(i.getEmploye());
        }

        DebugLogger.log("Ajout d'une intervention supplémentaire");
        Intervention i5 = new Incident("Repassage costume");
        s.demandeIntervention(current, i5);

        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);

        DebugLogger.log("Deconnexion...");

        DebugLogger.log("Connexion en tant qu'employé");
        currentE = s.connexionEmploye("insa@bg.bg", "insa");

        DebugLogger.log("Récupération de l'intervention en cours de l'employé");
        currentI = s.getInterventionAct(currentE);

        DebugLogger.log("Validation de l'intervention");
        s.validerIntervention(currentI, "RAS");

        DebugLogger.log("Deconnexion...");
        currentE = null;

        DebugLogger.log("Connexion en tant qu'employé");
        currentE = s.connexionEmploye("hollande@bg.bg", "macaron");

        DebugLogger.log("Récupération de l'intervention en cours de l'employé");
        currentI = s.getInterventionAct(currentE);

        DebugLogger.log("Echec de l'intervention");
        s.echecIntervention(currentI, "Inaccessible");

        DebugLogger.log("Historique d'intervention du jour de l'employe");
        for(Intervention inter : s.getInterventionJour(currentE, new Date()))
            System.out.println(inter);

        DebugLogger.log("Deconnexion...");
        current = null;

        DebugLogger.log("Connexion client");
        current = s.connexionClient("maca@gmail.com", "aedi");

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

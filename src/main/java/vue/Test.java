package vue;

import dao.JpaUtil;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import metier.modele.*;
import metier.service.Service;
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
        DebugLogger.log("Demande d'une intervention");

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
        Employe intE = i.getEmploye();
        Employe currentE = s.connexionEmploye(intE.getEmail(), intE.getMdp());

        DebugLogger.log("Récupération de l'intervention en cours de l'employé");
        Intervention currentI = s.getInterventionAct(currentE);
        System.out.println(currentI);

        //////////// VALIDATION DE L'INTERVENTION QUI NE MARCHE PAS CAR ELLE A ETE EFFECTUEE PAR QQUN DAUTRE /////////////
        DebugLogger.log("Validation de l'intervention");
        s.validerIntervention(currentI, "Kiki a été très calme, merci pour ce moment");

        ////////// MODULE HISTORIQUE INTERVENTION EMPLOYE / vide (logique) /////////
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
        DebugLogger.log("Demande d'une intervention");

        System.out.println("----- Demande d'une intervention de type : Incident -----");
        System.out.println("Description : ");
        des = sc.nextLine();

        i = new Incident(des);
        s.demandeIntervention(current, i);

        /////////// MODULE HISTORIQUE INTERVENTION CLIENT ///////////
        DebugLogger.log("Historique d'intervention du client");
        for(Intervention inter : s.getInterventionsClient(current))
            System.out.println(inter);

        DebugLogger.log("Inscription d'un autre client");
        s.inscrireClient(
                new Client("Margaux","P", "Mme", "07/08/2010", "5 avenue albert " +
                        "einstein, Villeurbanne", "0658889900", "maca@gmail.com", "aedi"));

        DebugLogger.log("Inscription d'un autre client avec le meme email");
        s.inscrireClient(
                new Client("Marie","M", "Mme", "07/10/2010", "30 avenue albert " +
                        "einstein, Villeurbanne", "0658909900", "maca@gmail.com", "kk"));

        //////////////TESTS DE CONNEXION///////////////////////// 
        DebugLogger.log("Test de connexion avec un mot de passe incorrect");
        current = s.connexionClient("maca@gmail.com", "abc");
        
        DebugLogger.log("Test de connexion client avec un mail et un mdp employé");
        current = s.connexionClient("insa@bg.bg", "insa");
        
        DebugLogger.log("Connexion avec le bon mdp");
        current = s.connexionClient("maca@gmail.com", "aedi");

        ///////////ON DEMANDE D'AUTRES INTERVENTIONS//////////////
        DebugLogger.log("Demande d'une autre intervention");
        Intervention i3 = new Incident("Robinet fuit");
        s.demandeIntervention(current, i3);
                
        DebugLogger.log("Demande d'une autre intervention");
        Intervention i4 = new Incident("Besoin de pates en toute urgence");
        s.demandeIntervention(current, i4);
        
        DebugLogger.log("On se connecte en tant qu'employe");
        currentE = s.connexionEmploye(intE.getEmail(), intE.getMdp());

        DebugLogger.log("Récupération de l'intervention en cours de l'employé");
        currentI = s.getInterventionAct(currentE);
        System.out.println(currentI);

        DebugLogger.log("Echec de l'intervention");
        s.echecIntervention(currentI, "Inaccessible");

        DebugLogger.log("Historique d'intervention du jour de l'employe");
        for(Intervention inter : s.getInterventionJour(currentE, new Date()))
            System.out.println(inter);

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
        
        /////////// MODULE HISTORIQUE INTERVENTION CLIENT ///////////
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

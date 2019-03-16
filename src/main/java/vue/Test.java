/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import dao.JpaUtil;

import java.sql.Time;
import java.util.List;
import metier.modele.Client;
import metier.modele.Employe;
import metier.modele.Intervention;
import metier.service.Service;


/**
 *
 * @author njeanne
 */
public class Test {
    
    public static void AfficherC(Client c)
    {
        System.out.println(c.toString());
    }
    public static void AfficherE(Employe c)
    {
        System.out.println(c.toString());
    }
    
    public static void Lister(Service s)
    {
       List<Client> l = s.listerClients();
       for(Client c : l)
           AfficherC(c);
    }

    public static void ListerE(Service s)
    {
        List<Employe> l = s.listerEmployesDispo(new Time(10,00,00));

        for(Employe e : l)
        {
            AfficherE(e);
        }
    }

    public static void TestInscriptionClients(Service s)
    {
        Client cp = new Client("Nathan","LPmeilleur", "Mme", "22/08/2004", "6 rue des Lilas, Lyon", "0658763255", "nathan.j@gmail.com", "margaux");

        if(s.creerClient(cp)) System.out.println("-------Inscription validée !-------");
        else System.out.println("-------Echec lors de l'inscription !-------");
        Lister(s);

        Client cpp = new Client("Nathan","LPM", "Mme", "22/08/2004", "6 rue des Lilas, Lyon", "0658763255", "nathan.j@gmail.com", "margaux");

        if(s.creerClient(cpp)) System.out.println("-------Inscription validée !-------");
        else System.out.println("-------Echec lors de l'inscription !-------");
        Lister(s);

        Client cppp = new Client("Margaux","Paris", "Mme", "04/09/2007", "97 rue des Lilas, Lyon", "0987654678", "margaux.lpm@gmail.com", "nathan");

        if(s.creerClient(cppp)) System.out.println("-------Inscription validée !-------");
        else System.out.println("-------Echec lors de l'inscription !-------");
        Lister(s);
    }

    public static void TestCreationEmployes(Service s)
    {
        Employe e = new Employe("Barack", "Afritte", "8 rue des lilas, Lyon", "0909090909",
                "makeBelgiumgr8again@bg.bg", "banane",
                new Time(8,00,00), new Time(18,00,00));

        if(s.creerEmploye(e)) System.out.println("-------Employee validée !-------");
        else System.out.println("-------Echec lors de l'employee !-------");
        ListerE(s);
    }

    public static void TestDemandeIntervention(Service s)
    {
        Intervention i = new Intervention("Faire la vaisselle svp et vite");

        Client c = new Client("Nathan","LPM", "Mme", "22/08/2004", "6 rue des Lilas, Lyon", "0658763255", "nathan.jEANNE@gmail.com", "margaux");

        s.creerClient(c);

        Employe e = new Employe("Barack", "Afritte", "8 rue des lilas, Lyon", "0909090909",
                "makeBelgiumgr8again@bg.bg", "banane",
                new Time(8,00,00), new Time(23,59,00));

        s.creerEmploye(e);

        Lister(s);
        ListerE(s);

        s.demandeIntervention(c, i);
    }

    public static void main(String[] args){
        
       JpaUtil.init();
      
       Service s = new Service();

       /** Tests de l'inscription de plusieurs Clients et de l'unicité des Clients **/
       //TestInscriptionClients(s);

       /** Test de la création d'employés et de leur persistence **/
       //TestCreationEmployes(s);

       /** Test demande d'intevention **/
       TestDemandeIntervention(s);



       JpaUtil.destroy();
    }

    
    
}

package metier.service;

import metier.modele.Animal;
import metier.modele.Client;
import metier.modele.Intervention;
import metier.modele.Livraison;
import util.Message;

import java.io.PrintWriter;
import java.io.StringWriter;

import static util.GeoTest.getFlightDistanceInKm;

public class ServiceAffichage {

    public ServiceAffichage() {}

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

        notificationWriter.print("Intervention de type ");
        if (i instanceof Animal){
            notificationWriter.print("Animal");
        }
        else if (i instanceof Livraison)
        {
            notificationWriter.print("Livraison");
        }
        else
        {
            notificationWriter.print("Incident");
        }
        notificationWriter.print(" demandée le "+i.getHeureD());
        notificationWriter.print(" Pour "+i.getClient().getPrenom()+ " "+i.getClient().getNom()+
                " ("+i.getClient().getId()+"), ");
        notificationWriter.print(i.getClient().getAdresse()+". <<"+i.getDescription()+" >>.");
        notificationWriter.println(" Trajet : "+
                getFlightDistanceInKm( i.getEmploye().getCoord(),i.getClient().getCoord())+" km.");

        Message.envoyerNotification(
                i.getEmploye().getNum(),
                message.toString()
        );

    }

    public void envoieNotifClient(Intervention i)
    {
        StringWriter message = new StringWriter();
        PrintWriter notificationWriter = new PrintWriter(message);

        notificationWriter.println("Votre demande d'intervention du  "+i.getHeureD());
        notificationWriter.print(" a été cloturée à  "+i.getHeureF()+". ");
        notificationWriter.print(i.getCommentaire());
        notificationWriter.print(" Cordialement, " + i.getEmploye().getPrenom());

        Message.envoyerNotification(
                i.getClient().getNum(),
                message.toString()
        );

    }
}

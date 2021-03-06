package dao;
import metier.modele.Client;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class daoClient {

    public daoClient() { }

    public void ajouterClient(Client c)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist (c);
    }

    public List<Client> listerClients()
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select c from Client c";
        Query requete = em.createQuery (jpql);
        List <Client> resultats = (List <Client>) requete.getResultList();
        return resultats;
    }

    public Client rechercherClientParId(Client c)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select c from Client c where c=:c";
        Query requete = em.createQuery(jpql);
        requete.setParameter("c", c); //on donne l'entité en paramètre (le c entre guillemets est celui après le :"
        Client resultat = (Client) requete.getSingleResult();
        return resultat;
    }

    public Client connexion(String email, String mdp)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select c from Client c where c.email=:email and c.mdp=:mdp";
        Query requete = em.createQuery(jpql);
        requete.setParameter("email", email);
        requete.setParameter("mdp", mdp);
        try
        {
            return (Client) requete.getSingleResult();
        } catch (Exception e)
        {
            return null;
        }
    }
}

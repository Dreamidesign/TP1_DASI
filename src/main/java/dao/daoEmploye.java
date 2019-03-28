package dao;

import metier.modele.Employe;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Time;
import java.util.List;

public class daoEmploye {

    public daoEmploye() {}

    public void ajouterEmploye(Employe e)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist (e);
    }

    public List<Employe> listerEmployesDispo(Time d)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Employe e where e.status = 0 " +
                "and e.horaireD <=:d and e.horaireF >= :d";
        Query requete = em.createQuery (jpql);
        requete.setParameter("d",d);
        List <Employe> resultats = (List <Employe>) requete.getResultList();
        return resultats;
    }

    public Employe rechercherEmployeParId(Employe c)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Employe e where e=:e";
        Query requete = em.createQuery(jpql);
        requete.setParameter("e", c);
        return (Employe) requete.getSingleResult();
    }
    
    public Employe connexion(String email, String mdp)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Employe e where e.email=:email and e.mdp=:mdp";
        Query requete = em.createQuery(jpql);
        requete.setParameter("email", email);
        requete.setParameter("mdp", mdp);
        try
        {
            return (Employe) requete.getSingleResult();
        } catch (Exception e)
        {
            return null;
        }
    }

    public void setDispo(Employe e, int newStatut)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Employe ee = em.find(Employe.class, e.getId());
        e.setStatus(newStatut);
    }
}

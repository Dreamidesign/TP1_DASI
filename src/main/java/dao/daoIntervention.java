/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 */


package dao;

import java.util.List;
import metier.modele.Intervention;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Client;
import metier.modele.Employe;

/**
 *
 * @author njeanne
 */
public class daoIntervention {

    public daoIntervention() {
    }

    public void ajouterIntervention(Intervention i)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist (i);
    }
    
    public List<Intervention> listerInterventionsClient(Client c)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select i from Intervention i where i.client:c";
        Query requete = em.createQuery (jpql);
        requete.setParameter("c", c);
        List <Intervention> resultats = (List <Intervention>) requete.getResultList();
        return resultats;
    }
    
     public Intervention getInterventionAct(Employe e){
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select i from Intervention i where i.employe = :e and i.statut = 1";
        Query requete = em.createQuery(jpql);
        requete.setParameter("e", e);
        requete.setMaxResults(1);
        Intervention resultat = (Intervention) requete.getSingleResult();
        
        return resultat;
        
    }

    

}

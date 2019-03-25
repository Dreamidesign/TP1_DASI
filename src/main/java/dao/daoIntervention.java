/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 */


package dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import metier.modele.Intervention;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import metier.modele.Client;
import metier.modele.Employe;
import util.DebugLogger;

/**
 *
 * @author njeanne
 */
public class daoIntervention {

    public daoIntervention() {}

    public void ajouterIntervention(Intervention i)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist (i);
    }
    
    public List<Intervention> listerInterventionsClient(Client c)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select i from Intervention i where i.client=:c";
        Query requete = em.createQuery (jpql);
        requete.setParameter("c", c);
        List <Intervention> resultats = (List <Intervention>) requete.getResultList();
        return resultats;
    }
    
    public Intervention getInterventionAct(Employe e)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select i from Intervention i where i.employe = :e and i.statut = 1";
        Query requete = em.createQuery(jpql);
        requete.setParameter("e", e);
        requete.setMaxResults(1);
        Intervention resultat = (Intervention) requete.getSingleResult();
        
        return resultat;
    }
     
    public List<Intervention> getInterventionJour(Employe e, Date d)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        GregorianCalendar cal2 = (GregorianCalendar)cal.clone();
        cal2.add(Calendar.DAY_OF_YEAR, 1); //Pour limiter à un jour le gregorian calendar.

        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select i from Intervention i where i.employe =:e and i.heureD>=:d and i.heureF<=:a";
        Query requete = em.createQuery(jpql);
        requete.setParameter("e",e);
        requete.setParameter("d",cal.getTime(), TemporalType.DATE);
        requete.setParameter("a", cal2.getTime(),TemporalType.DATE);
        List <Intervention> resultats= (List <Intervention>) requete.getResultList();
        
        return resultats;
    }

    public void setParametresIntervention(Intervention i, String c, int s)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Intervention e = em.find(Intervention.class, i.getId());
        e.setCommentaire(c);
        e.setHeureF(new Date());
        e.setStatut(s);
    }
    
    public Intervention rechercherInterventionParId(Intervention i)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Intervention e where e=:i";
        Query requete = em.createQuery(jpql);
        requete.setParameter("i", i);
        return (Intervention) requete.getSingleResult();
    }
    

    

}

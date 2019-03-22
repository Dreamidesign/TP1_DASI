/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import metier.modele.Employe;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Time;
import java.util.List;
import java.util.Date;
import metier.modele.Intervention;

/**
 *
 * @author njeanne
 */
public class daoEmploye {

    public daoEmploye() { }

    public void ajouterEmploye(Employe e)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist (e);
    }

    public List<Employe> listerEmployes(Time d)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Employe e where e.horaireD >="+d+" and e.horaireF <= "+d+"";
        Query requete = em.createQuery (jpql);
        List <Employe> resultats = (List <Employe>) requete.getResultList();
        return resultats;
    }

    public List<Employe> listerEmployesDispo(Time d)
    {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Employe e " +
                "where e not in (select distinct i.employe from Intervention i) " +
                "and e.horaireD <='"+d+"' and e.horaireF >= '"+d+"'";
        Query requete = em.createQuery (jpql);
        List <Employe> resultats = (List <Employe>) requete.getResultList();
        return resultats;
    }

    public Employe rechercherEmployeParId(Employe c){
        EntityManager em = JpaUtil.obtenirEntityManager();
        String jpql = "select e from Employe e where e=:e";
        Query requete = em.createQuery(jpql);
        requete.setParameter("e", c);
        return (Employe) requete.getSingleResult();
    }
    
}

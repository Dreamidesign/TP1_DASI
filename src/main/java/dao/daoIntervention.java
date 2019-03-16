/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 */


package dao;

import metier.modele.Intervention;

import javax.persistence.EntityManager;

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

}

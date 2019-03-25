package metier.service;

import dao.JpaUtil;
import dao.daoClient;
import dao.daoEmploye;
import metier.modele.Client;
import metier.modele.Employe;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class ServiceUtil {

    public daoClient dC;
    public daoEmploye dE;

    public ServiceUtil() {
        dC = new daoClient();
        dE = new daoEmploye();
    }

    public List<Client> listerClients()
    {
        JpaUtil.creerEntityManager();
        List<Client> l = dC.listerClients();
        JpaUtil.fermerEntityManager();
        return l;
    }

    public List<Employe> listerEmployesDispo(Date d)
    {
        JpaUtil.creerEntityManager();
        List<Employe> l = dE.listerEmployesDispo(new Time(d.getHours(), d.getMinutes(), d.getSeconds()));
        JpaUtil.fermerEntityManager();
        return l;
    }
}

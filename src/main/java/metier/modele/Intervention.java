/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author njeanne
 */
@Entity
public class Intervention implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Employe employe;
    
    @ManyToOne
    private Client client;
    
  
    private String statut;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date heureD;
    @Temporal(TemporalType.DATE)
    private Date heureF;
   
    

    public Intervention(String description) {
        this.statut = "en attente";
        this.description = description;
        this.heureD = new Date();
    }

    public Intervention() {
    }

    public Long getId() {
        return id;
    }

    public String getStatut() {
        return statut;
    }

    public String getDescription() {
        return description;
    }

    public Date getHeureD() {
        return heureD;
    }

    public Date getHeureF() {
        return heureF;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHeureD(Date heureD) {
        this.heureD = heureD;
    }

    public void setHeureF(Date heureF) {
        this.heureF = heureF;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
}

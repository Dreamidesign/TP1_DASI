 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import com.google.maps.model.LatLng;
import sun.misc.GC;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

 /**
 *
 * @author njeanne
 */

@Entity
public class Employe implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy = "employe")
    private List<Intervention> listeInter;
    
    private String nom;
    private String prenom;
    private String adresse;
    private String num;
    private String email;
    private String mdp;
    private LatLng coord;
    private Integer status;

    @Basic
    private Time horaireD;


    @Basic
    private Time horaireF;

    public Employe(String prenom, String nom, String adresse, String num, String email, String mdp, Time horaireD, Time horaireF) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.num = num;
        this.email = email;
        this.mdp = mdp;
        this.horaireD = horaireD;
        this.horaireF = horaireF;
        status = 0;
    }

    
    
    public Employe() {
    }
    

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNum() {
        return num;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Time getHoraireD() {
        return horaireD;
    }

    public Time getHoraireF() {
        return horaireF;
    }

    public void setHoraireD(Time horaireD) {
        this.horaireD = horaireD;
    }

    public void setHoraireF(Time horaireF) {
        this.horaireF = horaireF;
    }

    public List<Intervention> getListeInter() {
        return listeInter;
    }

     public LatLng getCoord() {
         return coord;
     }

     public void setCoord(LatLng coord) {
         this.coord = coord;
     }
     
    public void setStatus(Integer status) {
       this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

     @Override
     public String toString() {
         return "Employe{" +
                 "id=" + id +
                 ", listeInter=" + listeInter +
                 ", nom='" + nom + '\'' +
                 ", prenom='" + prenom + '\'' +
                 ", adresse='" + adresse + '\'' +
                 ", num='" + num + '\'' +
                 ", email='" + email + '\'' +
                 ", mdp='" + mdp + '\'' +
                 ", coord=" + coord +
                 ", horaireD=" + horaireD +
                 ", horaireF=" + horaireF +
                 '}';
     }
 }

package metier.modele;

import com.google.maps.model.LatLng;

import java.io.Serializable;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Client implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy = "client")
    private List <Intervention> listeInter;
    
    private String nom;
    private String prenom;
    private String civilite;

    @Temporal(TemporalType.DATE)
    private Date ddn;
    private String adresse;
    private String num;
    private LatLng coord;
    
    @Column(unique=true)
    private String email;
    
    private String mdp;

    public Client(String prenom, String nom, String civilite, String d, String adresse, String num, String email, String mdp) {
        this.nom = nom;
        this.prenom = prenom;
        this.civilite = civilite;

        SimpleDateFormat dateformat2 = new SimpleDateFormat("dd/MM/yyyy");
        Date ddn = null;
        try {
            ddn = dateformat2.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.ddn = ddn;
        this.adresse = adresse;
        this.num = num;
        this.email = email;
        this.mdp = mdp;
    }

    public Client() { }

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

    public String getCivilite() {
        return civilite;
    }

    public Date getDdn() {
        return ddn;
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

    public LatLng getCoord() {
        return coord;
    }

    public String getMdp() {
        return mdp;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public void setDdn(Date ddn) {
        this.ddn = ddn;
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

    public void setCoord(LatLng coord) {
        this.coord = coord;
    }

    public List<Intervention> getListeInter() {
        return listeInter;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", listeInter=" + listeInter +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", civilite='" + civilite + '\'' +
                ", ddn='" + ddn + '\'' +
                ", adresse='" + adresse + '\'' +
                ", num='" + num + '\'' +
                ", coord=" + coord +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                '}';
    }
}

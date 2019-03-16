/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.time.LocalTime;
import javax.persistence.Entity;

/**
 *
 * @author njeanne
 */
@Entity
public class Livraison extends Intervention {
    
    private String objet;
    private String entreprise;

    public Livraison(String objet, String entreprise, String description) {
        super(description);
        this.objet = objet;
        this.entreprise = entreprise;
    }

    

    public Livraison() {
    }

    public String getObjet() {
        return objet;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    
    
    
}

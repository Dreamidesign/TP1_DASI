package metier.modele;

import javax.persistence.Entity;

@Entity
public class Livraison extends Intervention {
    
    private String objet;
    private String entreprise;

    public Livraison(String objet, String entreprise, String description) {
        super(description);
        this.objet = objet;
        this.entreprise = entreprise;
    }

    public Livraison() { }

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

    @Override
    public String toString() {
        String s = "---<([********])>---\n\n-- Livraison --\n"
                +objet+"\n"+entreprise+"\n"+super.toString();
        return s;
    }
}

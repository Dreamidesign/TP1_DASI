package metier.modele;
import javax.persistence.Entity;

@Entity
public class Incident extends Intervention {

    public Incident(String description) {
        super(description);
    }

    public Incident() { }

    @Override
    public String toString() {
        String s = "---<([********])>---\n\n-- Incident --\n"+super.toString();
        return s;
    }
}

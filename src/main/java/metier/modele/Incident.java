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
public class Incident extends Intervention {

    public Incident(String description) {
        super(description);
    }

    public Incident() {
    }

    @Override
    public String toString() {
        String s = "---<([********])>---\n\n-- Incident --\n"+super.toString();
        return s;
    }
}

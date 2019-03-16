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
public class Animal extends Intervention{
    private String animal;

    public Animal(String animal, String description) {
        super(description);
        this.animal = animal;
    }

    
    

    public Animal() {
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

}

package metier.modele;

import javax.persistence.Entity;

@Entity
public class Animal extends Intervention{
    private String animal;

    public Animal(String animal, String description) {
        super(description);
        this.animal = animal;
    }

    public Animal() { }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        String s = "---<([********])>---\n\n-- Animal --\n"+animal+"\n"+super.toString();
        return s;
    }
}

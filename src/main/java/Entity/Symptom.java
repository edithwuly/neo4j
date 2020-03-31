package Entity;

import java.util.Objects;

public class Symptom {
    public static final int MALE = 1;
    public static final int FEMALE = -1;
    public static final int UNISEX = 0;
    public String name;
    public Integer gender;

    public Symptom(String name, Integer gender) {
        this.name = name;
        this.gender = gender;
    }

    public Symptom(Symptom another) {
        this.name = another.name;
        this.gender = another.gender;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Symptom symptom = (Symptom)o;
            return Objects.equals(this.name, symptom.name) && Objects.equals(this.gender, symptom.gender);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.gender});
    }
}

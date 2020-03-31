package Entity;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class Recursion {
    public static final int MALE = 1;
    public static final int FEMALE = -1;
    public static final int UNISEX = 0;
    public String name;
    public String type;
    public boolean isDefinition;
    public Integer gender;
    public String[] position;
    public Boolean subjectivity;
    public Recursion subject;
    public String predicate;
    public Recursion object;

    public Recursion() {
    }

    public Recursion(String name, String type, boolean isDefinition, Integer gender, String[] position, Boolean subjectivity, Recursion subject, String predicate, Recursion object) {
        this.name = name;
        this.type = type;
        this.isDefinition = isDefinition;
        this.gender = gender;
        this.position = position;
        this.subjectivity = subjectivity;
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public Recursion(@NotNull Recursion another) {
        this.name = another.name;
        this.type = another.type;
        this.isDefinition = another.isDefinition;
        this.gender = another.gender;
        if (null == another.position) {
            this.position = null;
        } else {
            this.position = new String[another.position.length];
            System.arraycopy(another.position, 0, this.position, 0, another.position.length);
        }

        this.subjectivity = another.subjectivity;
        if (another.subject == null) {
            this.subject = null;
        } else {
            this.subject = new Recursion(another.subject);
        }

        this.predicate = another.predicate;
        if (another.object == null) {
            this.object = null;
        } else {
            this.object = new Recursion(another.object);
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Recursion recursion = (Recursion)o;
            return this.isDefinition == recursion.isDefinition && Objects.equals(this.name, recursion.name) && Objects.equals(this.type, recursion.type) && Objects.equals(this.gender, recursion.gender) && Arrays.equals(this.position, recursion.position) && Objects.equals(this.subjectivity, recursion.subjectivity) && Objects.equals(this.subject, recursion.subject) && Objects.equals(this.predicate, recursion.predicate) && Objects.equals(this.object, recursion.object);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = Objects.hash(new Object[]{this.name, this.type, this.isDefinition, this.gender, this.subjectivity, this.subject == null ? 0 : this.subject.hashCode(), this.predicate, this.object == null ? 0 : this.object.hashCode()});
        result = 31 * result + Arrays.hashCode(this.position);
        return result;
    }
}

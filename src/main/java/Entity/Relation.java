package Entity;

import java.util.Objects;

public class Relation {
    public String subject;
    public String object;

    public Relation(String subject, String object) {
        this.subject = subject;
        this.object = object;
    }

    public Relation(Relation another) {
        this.subject = another.subject;
        this.object = another.object;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Relation relation = (Relation)o;
            return Objects.equals(this.subject, relation.subject) && Objects.equals(this.object, relation.object);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.subject, this.object});
    }
}

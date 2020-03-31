package Entity;

import java.util.Arrays;
import java.util.Objects;

public class Neighbour {
    public boolean neighbourIsObject;
    public String predicate;
    public String predicateID;
    public String[] source;
    public String type;
    public String name;
    public Integer confidence;

    public Neighbour(boolean neighbourIsObject, String predicate, String predicateID, String[] source, String type, String name, Integer confidence) {
        this.neighbourIsObject = neighbourIsObject;
        this.predicate = predicate;
        this.predicateID = predicateID;
        this.source = source;
        this.type = type;
        this.name = name;
        this.confidence = confidence;
    }

    public Neighbour(Neighbour another) {
        this.neighbourIsObject = another.neighbourIsObject;
        this.predicate = another.predicate;
        this.predicateID = another.predicateID;
        this.type = another.type;
        this.name = another.name;
        this.confidence = another.confidence;
        if (null == another.source) {
            this.source = null;
        } else {
            this.source = new String[another.source.length];
            System.arraycopy(another.source, 0, this.source, 0, another.source.length);
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Neighbour neighbour = (Neighbour)o;
            return this.neighbourIsObject == neighbour.neighbourIsObject && Objects.equals(this.predicate, neighbour.predicate) && Objects.equals(this.predicateID, neighbour.predicateID) && Arrays.equals(this.source, neighbour.source) && Objects.equals(this.type, neighbour.type) && Objects.equals(this.name, neighbour.name) && Objects.equals(this.confidence, neighbour.confidence);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = Objects.hash(new Object[]{this.neighbourIsObject, this.predicate, this.predicateID, this.type, this.name, this.confidence});
        result = 31 * result + Arrays.hashCode(this.source);
        return result;
    }
}

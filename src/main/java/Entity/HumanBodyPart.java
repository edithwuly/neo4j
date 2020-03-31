package Entity;

import java.util.Arrays;
import java.util.Objects;

public class HumanBodyPart {
    public String name;
    public String code;
    public HumanBodyPart[] subParts;
    public Symptom[] symptoms;

    public HumanBodyPart(String name, String code, HumanBodyPart[] subParts, Symptom[] symptoms) {
        this.name = name;
        this.code = code;
        this.subParts = subParts;
        this.symptoms = symptoms;
    }

    public HumanBodyPart(HumanBodyPart another) {
        this.name = another.name;
        this.code = another.code;
        int i;
        if (null == another.subParts) {
            this.subParts = null;
        } else {
            this.subParts = new HumanBodyPart[another.subParts.length];

            for(i = 0; i < another.subParts.length; ++i) {
                this.subParts[i] = new HumanBodyPart(another.subParts[i]);
            }
        }

        if (null == another.symptoms) {
            this.symptoms = null;
        } else {
            this.symptoms = new Symptom[another.symptoms.length];

            for(i = 0; i < another.symptoms.length; ++i) {
                this.symptoms[i] = new Symptom(another.symptoms[i]);
            }
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            HumanBodyPart that = (HumanBodyPart)o;
            return Objects.equals(this.name, that.name) && Objects.equals(this.code, that.code) && Arrays.equals(this.subParts, that.subParts) && Arrays.equals(this.symptoms, that.symptoms);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = Objects.hash(new Object[]{this.name, this.code});
        result = 31 * result + Arrays.hashCode(this.subParts);
        result = 31 * result + Arrays.hashCode(this.symptoms);
        return result;
    }
}

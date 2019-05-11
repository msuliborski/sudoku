package pl.comp.model;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Verifier implements Serializable {

    private List<SudokuField> fields = new ArrayList<>();
    private int addCounter = 0;

    public void addField(final SudokuField field) {
        if (addCounter == 9) {
            fields = Collections.unmodifiableList(fields);
        } else {
            fields.add(field);
        }
        addCounter++;
    }

    public boolean verify() {
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (fields.get(i).getFieldValue() != 0 && fields.get(j).getFieldValue() != 0) {
                    if (fields.get(i).equals(fields.get(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof SudokuField)) {
            return false;
        }

        Verifier v = (Verifier) o;

        for (int i = 0; i < 8; i++) {
            if (!v.fields.get(0).equals(this.fields.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(fields, addCounter);
    }
}

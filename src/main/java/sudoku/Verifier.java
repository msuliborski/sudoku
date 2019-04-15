package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Verifier {

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
                    if (fields.get(i).getFieldValue() == fields.get(j).getFieldValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

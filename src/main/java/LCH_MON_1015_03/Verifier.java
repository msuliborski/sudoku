package LCH_MON_1015_03;

import java.util.ArrayList;
import java.util.List;

public class Verifier {

    public void addField(SudokuField field) {
        fields.add(field);
    }

    private List<SudokuField> fields = new ArrayList<>();


    public boolean verify() {
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (fields.get(i).getFieldValue() != 0 && fields.get(j).getFieldValue() != 0) {
                    if (fields.get(i).getFieldValue() == fields.get(j).getFieldValue()) return false;
                }
            }
        }
        return true;
    }
}

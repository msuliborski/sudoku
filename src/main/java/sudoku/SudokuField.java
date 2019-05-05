package sudoku;

import com.google.common.base.Objects;

public class SudokuField {

    private int value;

    SudokuField() {
        this.value = 0;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof SudokuField)){
            return false;
        }

        SudokuField s = (SudokuField) o;

        return s.value == this.value;
    }


}

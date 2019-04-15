package sudoku;

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
        int result = 17;
        result = 31 * result + value;
        return result;
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

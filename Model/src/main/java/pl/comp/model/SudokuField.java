package pl.comp.model;

import com.google.common.base.Objects;

import java.io.Serializable;

public class SudokuField implements Serializable, Comparable<SudokuField>, Cloneable {

    private int value;
    private boolean isDefault = true;

    SudokuField() {
        this.value = 0;
    }

    SudokuField(int val) {
        this.value = val;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
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

        SudokuField s = (SudokuField) o;

        return s.value == this.value;
    }

    @Override
    public int compareTo(SudokuField field) {
        return Integer.compare(field.value, value);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

}

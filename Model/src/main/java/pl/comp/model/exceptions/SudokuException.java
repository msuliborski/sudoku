package pl.comp.model.exceptions;

public class SudokuException extends Exception {


//    private final ErrorCode code;

    public SudokuException(String message) {
        super(message);
    }

    public SudokuException(String message, Throwable cause) {
        super(message, cause);
    }
}

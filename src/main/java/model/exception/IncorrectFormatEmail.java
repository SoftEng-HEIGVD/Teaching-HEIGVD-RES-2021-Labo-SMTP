package model.exception;

public class IncorrectFormatEmail extends Exception {
    public IncorrectFormatEmail() {
        super();
    }

    public IncorrectFormatEmail(String message) {
        super(message);
    }
}

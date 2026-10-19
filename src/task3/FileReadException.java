package task3;


public class FileReadException extends Exception {

    private static final long serialVersionUID = 1L;

    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}

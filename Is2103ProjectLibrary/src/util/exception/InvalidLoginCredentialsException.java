package util.exception;

public class InvalidLoginCredentialsException extends Exception {

    public InvalidLoginCredentialsException() {
        super("Wrong username or password");
    }

    public InvalidLoginCredentialsException(String msg) {
        super(msg);
    }
}

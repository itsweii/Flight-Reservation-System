package util.exception;

public class NoAirportException extends Exception {

    public NoAirportException() {
        super("No airport was found");
    }

    public NoAirportException(String msg) {
        super(msg);
    }
}

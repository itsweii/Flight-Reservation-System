package util.exception;

public class NoAirportFoundException extends Exception {

    public NoAirportFoundException() {
    }

    public NoAirportFoundException(String msg) {
        super(msg);
    }
}

package util.exception;

public class NoFlightRouteException extends Exception {

    public NoFlightRouteException() {
        super("No flight route was found");
    }

    public NoFlightRouteException(String msg) {
        super(msg);
    }
}

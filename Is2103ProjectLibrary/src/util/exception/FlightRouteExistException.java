package util.exception;

public class FlightRouteExistException extends Exception {

    public FlightRouteExistException() {
    }

    public FlightRouteExistException(String msg) {
        super(msg);
    }
}

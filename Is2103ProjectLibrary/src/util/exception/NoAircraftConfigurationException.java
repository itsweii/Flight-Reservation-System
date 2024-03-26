package util.exception;

public class NoAircraftConfigurationException extends Exception {

    public NoAircraftConfigurationException() {
        super("no aircraft configuration was found");
    }

    public NoAircraftConfigurationException(String msg) {
        super(msg);
    }
}

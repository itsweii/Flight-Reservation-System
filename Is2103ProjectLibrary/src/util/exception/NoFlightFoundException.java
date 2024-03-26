package util.exception;

public class NoFlightFoundException extends Exception {

    public NoFlightFoundException() {
        super("No flight was found");
    }

    /**
     * Constructs an instance of <code>CancelledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoFlightFoundException(String msg) {
        super(msg);
    }
}

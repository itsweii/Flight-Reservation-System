package util.exception;

public class CancelledException extends Exception {

    public CancelledException() {
    }

    /**
     * Constructs an instance of <code>CancelledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CancelledException(String msg) {
        super(msg + " cancelled");
    }
}

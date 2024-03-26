package util.exception;

public class CustomerAccountFoundException extends Exception {

    public CustomerAccountFoundException() {
    }

    /**
     * Constructs an instance of <code>CancelledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerAccountFoundException(String msg) {
        super(msg + " cancelled");
    }
}

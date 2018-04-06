package es.eurohelp.lod.aldapa.core.exception;

public class SilkLinksNotDiscoveredException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -1422827913594836768L;
    private static final String MESSAGE = "The configuration file must be a '.xml'";

    public SilkLinksNotDiscoveredException() {
        super(MESSAGE);
    }

    public SilkLinksNotDiscoveredException(String message) {
        super(MESSAGE + ": " + message);
    }

    public SilkLinksNotDiscoveredException(Throwable cause) {
        super(cause);
    }

    public SilkLinksNotDiscoveredException(String message, Throwable cause) {
        super(message, cause);
    }
}

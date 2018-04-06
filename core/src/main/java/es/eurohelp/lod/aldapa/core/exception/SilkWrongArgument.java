package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * @author dmuchuari
 */
public class SilkWrongArgument extends IllegalArgumentException {
    private static final String MESSAGE = "The configuration file must be a '.xml'";
    
    public SilkWrongArgument() {
        super(MESSAGE);
    }
    
    public SilkWrongArgument(String message) {
        super(MESSAGE + ": " + message);
    }

    public SilkWrongArgument(Throwable cause) {
        super(cause);
    }

    public SilkWrongArgument(String message, Throwable cause) {
        super(message, cause);
    }
}

package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * An IO exception related to the file holding the configuration
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationFileIOException extends ConfigurationException {

    /**
     * serialVersionUID (type: {@link long})
     */
    private static final long serialVersionUID = 7661895710448417371L;

    private static final String MESSAGE = "IO problem with configuration file";

    /**
     * Constructs a new ConfigurationFileIOException exception with the default detail message: {@value #MESSAGE}.
     */
    public ConfigurationFileIOException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new ConfigurationFileIOException exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt>.
     *
     * @param cause
     *            the cause that can be later retrieved by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public ConfigurationFileIOException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ConfigurationFileIOException exception with the specified detail message and
     * cause.
     * 
     * @param message
     *            the detail message that can be later retrieved by the {@link #getMessage()} method.
     * 
     * @param cause
     *            the cause that can be later retrieved by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public ConfigurationFileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigurationFileIOException exception with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param message
     *            the detail message that can be later retrieved by the {@link #getMessage()} method.
     * @param cause
     *            the cause that can be later retrieved by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     * @param enableSuppression
     *            whether or not suppression is enabled
     *            or disabled
     * @param writableStackTrace
     *            whether or not the stack trace should
     *            be writable
     */
    public ConfigurationFileIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

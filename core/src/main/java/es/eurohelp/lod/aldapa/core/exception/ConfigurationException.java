package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * An exception related to the configuration of the API
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationException extends AldapaException {

    /**
     * serialVersionUID (type: {@link long})
     */
    private static final long serialVersionUID = -8184966583886906920L;

    /**
     * Constructs a new ConfigurationException exception without detail message.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Constructs a new ConfigurationException exception with the specified detail message.
     *
     * @param message
     *            the detail message that can be later retrieved by the {@link #getMessage()} method.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigurationException exception with the specified detail message and
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
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigurationException exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt>.
     *
     * @param cause
     *            the cause that can be later retrieved by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ConfigurationException exception with the specified detail message,
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
    protected ConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

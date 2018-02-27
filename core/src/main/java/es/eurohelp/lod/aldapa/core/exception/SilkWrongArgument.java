package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * @author dmuchuari
 */
public class SilkWrongArgument extends IllegalArgumentException {
	private final static String MESSAGE = "The configuration file must be a '.xml'";

	public SilkWrongArgument() {
		// TODO Auto-generated constructor stub
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

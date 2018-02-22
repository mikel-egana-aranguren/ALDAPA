package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * @author dmuchuari
 */
public class WrongArgument extends IllegalArgumentException {

	public WrongArgument() {
		// TODO Auto-generated constructor stub
	}

	public WrongArgument(String message) {
		super(message);
	}

	public WrongArgument(Throwable cause) {
		super(cause);
	}

	public WrongArgument(String message, Throwable cause) {
		super(message, cause);
	}
}

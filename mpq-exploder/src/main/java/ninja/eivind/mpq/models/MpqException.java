package ninja.eivind.mpq.models;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqException extends RuntimeException {

    public MpqException(String message, Throwable cause) {
        super(message, cause);
    }
}

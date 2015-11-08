package ninja.eivind.mpq.models;

/**
 * RuntimeException denoting that something went wrong in working with a .mpq file, mostly used to wrap specific
 * exceptions as an unchecked exception
 */
public class MpqException extends RuntimeException {

    public MpqException(String message, Throwable cause) {
        super(message, cause);
    }
}

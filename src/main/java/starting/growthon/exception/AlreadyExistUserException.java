package starting.growthon.exception;

public class AlreadyExistUserException extends RuntimeException {
    public AlreadyExistUserException(String message) {
        super(message);
    }
}

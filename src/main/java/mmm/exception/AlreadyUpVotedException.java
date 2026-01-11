package mmm.exception;

public class AlreadyUpVotedException extends RuntimeException {
    public AlreadyUpVotedException(String message) {
      super(message);
    }
}

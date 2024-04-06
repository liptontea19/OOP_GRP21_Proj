package bank;

public class ExitException extends Exception {  // check that out its an inheritance yo!
    public ExitException(String message) {
        super(message);
    }
}

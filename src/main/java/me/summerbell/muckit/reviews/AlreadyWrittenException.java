package me.summerbell.muckit.reviews;

public class AlreadyWrittenException extends RuntimeException {
    public AlreadyWrittenException() {
    }

    public AlreadyWrittenException(String message) {
        super(message);
    }
}

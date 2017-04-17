package com.srobinson24.sandbox.tradeskillmaster.exception;

/**
 * Created by srobinso on 3/28/2017.
 */
public class RuntimeFileException extends RuntimeException {

    public RuntimeFileException() {
        super();
    }

    public RuntimeFileException(Throwable throwable) {
        super(throwable);
    }

    public RuntimeFileException(String message) {
        super(message);
    }

    public RuntimeFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

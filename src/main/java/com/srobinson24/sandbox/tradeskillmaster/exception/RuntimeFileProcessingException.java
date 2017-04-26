package com.srobinson24.sandbox.tradeskillmaster.exception;

/**
 * Created by srobinso on 3/28/2017.
 */
public class RuntimeFileProcessingException extends RuntimeException {

    public RuntimeFileProcessingException() {
        super();
    }

    public RuntimeFileProcessingException(Throwable throwable) {
        super(throwable);
    }

    public RuntimeFileProcessingException(String message) {
        super(message);
    }

    public RuntimeFileProcessingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

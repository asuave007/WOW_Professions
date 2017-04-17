package com.srobinson24.sandbox.tradeskillmaster.exception;

/**
 * Created by srobinso on 3/28/2017.
 */
public class RuntimeHttpException extends RuntimeException {

    public RuntimeHttpException() {
        super();
    }

    public RuntimeHttpException(Throwable throwable) {
        super(throwable);
    }

    public RuntimeHttpException(String message) {
        super(message);
    }

    public RuntimeHttpException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

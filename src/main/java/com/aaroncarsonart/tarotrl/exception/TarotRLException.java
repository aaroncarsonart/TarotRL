package com.aaroncarsonart.tarotrl.exception;

public class TarotRLException extends RuntimeException {

    public TarotRLException(String errorMessage) {
        super(errorMessage);
        System.exit(1);
    }

    public TarotRLException(Exception cause) {
        super(cause);
        System.exit(1);
    }
}

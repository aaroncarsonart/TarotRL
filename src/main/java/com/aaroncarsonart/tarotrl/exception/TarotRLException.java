package com.aaroncarsonart.tarotrl.exception;

/**
 * Just a convenience wrapper for unchecked exceptions that should
 * report the error, halt game behavior, and exit the program, as
 * the game has entered an erroneous, buggy state.
 *
 * (With all necessary apologies to players who lose their game's
 * progress as a result of abruptly quitting the game.)
 *
 */
public class TarotRLException extends RuntimeException {

    public TarotRLException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
//        System.exit(1);
    }

    public TarotRLException(String errorMessage) {
        super(errorMessage);
//        System.exit(1);
    }

    public TarotRLException(Exception cause) {
        super(cause);
//        System.exit(1);
    }

    public TarotRLException() {
    }
}

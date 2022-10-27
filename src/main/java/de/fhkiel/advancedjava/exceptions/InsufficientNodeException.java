package de.fhkiel.advancedjava.exceptions;

/**
 * Exception for missing node dependency.
 */
public class InsufficientNodeException extends Exception {
    public InsufficientNodeException(String message) {
        super(message);
    }
}

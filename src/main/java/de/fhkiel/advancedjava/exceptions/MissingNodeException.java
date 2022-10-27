package de.fhkiel.advancedjava.exceptions;

/**
 * Exception for Nodes that could not be found.
 */
public class MissingNodeException extends Exception {

    public MissingNodeException(String message) {
        super(message);
    }
}

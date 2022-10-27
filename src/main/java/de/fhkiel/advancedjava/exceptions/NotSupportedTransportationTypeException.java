package de.fhkiel.advancedjava.exceptions;

/**
 * Exception for incompatible transportation types.
 */
public class NotSupportedTransportationTypeException extends Exception {

    public NotSupportedTransportationTypeException(String message) {
        super(message);
    }
}

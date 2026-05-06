package com.votesecure.exception;

public class ElectionClosedException extends RuntimeException {
    public ElectionClosedException(String message) { super(message); }
}

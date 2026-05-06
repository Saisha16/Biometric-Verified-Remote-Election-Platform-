package com.votesecure.exception;

public class ElectionNotFoundException extends RuntimeException {
    public ElectionNotFoundException(String message) { super(message); }
}

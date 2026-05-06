package com.votesecure.exception;

public class VotingInProgressException extends RuntimeException {
    public VotingInProgressException(String message) { super(message); }
}

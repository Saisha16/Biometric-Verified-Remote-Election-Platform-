package com.votesecure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler using @ControllerAdvice.
 * All exceptions are caught here and converted to proper HTTP responses.
 * This is an enterprise best practice — no raw stack traces leak to clients.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(AlreadyVotedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyVoted(AlreadyVotedException ex) {
        log.warn("Double vote attempt: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "ALREADY_VOTED", ex.getMessage());
    }

    @ExceptionHandler(VotingInProgressException.class)
    public ResponseEntity<ErrorResponse> handleVotingInProgress(VotingInProgressException ex) {
        log.warn("Concurrent voting blocked: {}", ex.getMessage());
        return buildResponse(HttpStatus.TOO_MANY_REQUESTS, "VOTING_IN_PROGRESS", ex.getMessage());
    }

    @ExceptionHandler(ElectionClosedException.class)
    public ResponseEntity<ErrorResponse> handleElectionClosed(ElectionClosedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "ELECTION_CLOSED", ex.getMessage());
    }

    @ExceptionHandler(VoterNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVoterNotFound(VoterNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "VOTER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ElectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleElectionNotFound(ElectionNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "ELECTION_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_STATE", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.error("Runtime error: ", ex);
        // If it's the "counting not started" error, return 403 or 202 instead of 500
        if (ex.getMessage().contains("counting has not started")) {
            return buildResponse(HttpStatus.ACCEPTED, "COUNTING_NOT_STARTED", ex.getMessage());
        }
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Error: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String code, String message) {
        ErrorResponse error = new ErrorResponse(status.value(), code, message, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }

    public record ErrorResponse(int status, String code, String message, LocalDateTime timestamp) {}
}

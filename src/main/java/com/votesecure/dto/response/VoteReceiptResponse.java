package com.votesecure.dto.response;

import java.time.LocalDateTime;

 
 

public class VoteReceiptResponse {
    private String receiptCode;
    private String message;
    private String electionId;
    private LocalDateTime timestamp;

    public VoteReceiptResponse() {}

    public VoteReceiptResponse(String receiptCode, String message, String electionId, LocalDateTime timestamp) {
        this.receiptCode = receiptCode;
        this.message = message;
        this.electionId = electionId;
        this.timestamp = timestamp;
    }

    public String getReceiptCode() { return this.receiptCode; }

    public void setReceiptCode(String receiptCode) { this.receiptCode = receiptCode; }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public LocalDateTime getTimestamp() { return this.timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class VoteReceiptResponseBuilder {
        private String receiptCode;
        private String message;
        private String electionId;
        private LocalDateTime timestamp;

        public VoteReceiptResponseBuilder receiptCode(String receiptCode) {
            this.receiptCode = receiptCode;
            return this;
        }

        public VoteReceiptResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public VoteReceiptResponseBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public VoteReceiptResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public VoteReceiptResponse build() {
            return new VoteReceiptResponse(this.receiptCode, this.message, this.electionId, this.timestamp);
        }
    }

    public static VoteReceiptResponseBuilder builder() { return new VoteReceiptResponseBuilder(); }
}

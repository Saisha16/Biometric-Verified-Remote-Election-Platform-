package com.votesecure.dto.request;

import jakarta.validation.constraints.NotBlank;

 
 

public class BiometricVerifyRequest {

    @NotBlank(message = "Aadhaar number is required")
    private String aadhaarNumber;

    @NotBlank(message = "Fingerprint data is required")
    private String fingerprintData;

    public BiometricVerifyRequest() {}

    public BiometricVerifyRequest(String aadhaarNumber, String fingerprintData) {
        this.aadhaarNumber = aadhaarNumber;
        this.fingerprintData = fingerprintData;
    }

    public String getAadhaarNumber() { return this.aadhaarNumber; }

    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }

    public String getFingerprintData() { return this.fingerprintData; }

    public void setFingerprintData(String fingerprintData) { this.fingerprintData = fingerprintData; }

    public static class BiometricVerifyRequestBuilder {
        private String aadhaarNumber;
        private String fingerprintData;

        public BiometricVerifyRequestBuilder aadhaarNumber(String aadhaarNumber) {
            this.aadhaarNumber = aadhaarNumber;
            return this;
        }

        public BiometricVerifyRequestBuilder fingerprintData(String fingerprintData) {
            this.fingerprintData = fingerprintData;
            return this;
        }

        public BiometricVerifyRequest build() {
            return new BiometricVerifyRequest(this.aadhaarNumber, this.fingerprintData);
        }
    }

    public static BiometricVerifyRequestBuilder builder() { return new BiometricVerifyRequestBuilder(); }
}

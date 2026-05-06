package com.votesecure.dto.response;


 
 

public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String boothId;
    private long expiresIn;

    public LoginResponse() {}

    public LoginResponse(String token, String username, String role, String boothId, long expiresIn) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.boothId = boothId;
        this.expiresIn = expiresIn;
    }

    public String getToken() { return this.token; }

    public void setToken(String token) { this.token = token; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getRole() { return this.role; }

    public void setRole(String role) { this.role = role; }

    public String getBoothId() { return this.boothId; }

    public void setBoothId(String boothId) { this.boothId = boothId; }

    public long getExpiresIn() { return this.expiresIn; }

    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }

    public static class LoginResponseBuilder {
        private String token;
        private String username;
        private String role;
        private String boothId;
        private long expiresIn;

        public LoginResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public LoginResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public LoginResponseBuilder boothId(String boothId) {
            this.boothId = boothId;
            return this;
        }

        public LoginResponseBuilder expiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(this.token, this.username, this.role, this.boothId, this.expiresIn);
        }
    }

    public static LoginResponseBuilder builder() { return new LoginResponseBuilder(); }
}

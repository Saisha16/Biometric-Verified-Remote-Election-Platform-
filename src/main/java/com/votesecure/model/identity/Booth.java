package com.votesecure.model.identity;

import jakarta.persistence.*;

/**
 * Voting booth — a physical location where voters can cast their vote.
 */
@Entity
@Table(name = "booths")
 
 

public class Booth {

    @Id
    @Column(name = "booth_id", length = 20)
    private String boothId;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "is_active", nullable = false)
    
    private Boolean isActive = true;

    public Booth() {}

    public Booth(String boothId, String location, String district, String state, Boolean isActive) {
        this.boothId = boothId;
        this.location = location;
        this.district = district;
        this.state = state;
        this.isActive = isActive;
    }

    public String getBoothId() { return this.boothId; }

    public void setBoothId(String boothId) { this.boothId = boothId; }

    public String getLocation() { return this.location; }

    public void setLocation(String location) { this.location = location; }

    public String getDistrict() { return this.district; }

    public void setDistrict(String district) { this.district = district; }

    public String getState() { return this.state; }

    public void setState(String state) { this.state = state; }

    public Boolean getIsActive() { return this.isActive; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static class BoothBuilder {
        private String boothId;
        private String location;
        private String district;
        private String state;
        private Boolean isActive = true;

        public BoothBuilder boothId(String boothId) {
            this.boothId = boothId;
            return this;
        }

        public BoothBuilder location(String location) {
            this.location = location;
            return this;
        }

        public BoothBuilder district(String district) {
            this.district = district;
            return this;
        }

        public BoothBuilder state(String state) {
            this.state = state;
            return this;
        }

        public BoothBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Booth build() {
            return new Booth(this.boothId, this.location, this.district, this.state, this.isActive);
        }
    }

    public static BoothBuilder builder() { return new BoothBuilder(); }
}

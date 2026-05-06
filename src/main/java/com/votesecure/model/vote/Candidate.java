package com.votesecure.model.vote;

import jakarta.persistence.*;

/**
 * Candidate standing in an election for a specific constituency.
 */
@Entity
@Table(name = "candidates")
 
 

public class Candidate {

    @Id
    @Column(name = "candidate_id", length = 20)
    private String candidateId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "party", nullable = false, length = 100)
    private String party;

    @Column(name = "party_symbol", length = 50)
    private String partySymbol;

    @Column(name = "constituency", nullable = false, length = 100)
    private String constituency;

    @Column(name = "election_id", nullable = false, length = 20)
    private String electionId;

    public Candidate() {}

    public Candidate(String candidateId, String name, String party, String partySymbol, String constituency, String electionId) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.partySymbol = partySymbol;
        this.constituency = constituency;
        this.electionId = electionId;
    }

    public String getCandidateId() { return this.candidateId; }

    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getParty() { return this.party; }

    public void setParty(String party) { this.party = party; }

    public String getPartySymbol() { return this.partySymbol; }

    public void setPartySymbol(String partySymbol) { this.partySymbol = partySymbol; }

    public String getConstituency() { return this.constituency; }

    public void setConstituency(String constituency) { this.constituency = constituency; }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public static class CandidateBuilder {
        private String candidateId;
        private String name;
        private String party;
        private String partySymbol;
        private String constituency;
        private String electionId;

        public CandidateBuilder candidateId(String candidateId) {
            this.candidateId = candidateId;
            return this;
        }

        public CandidateBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CandidateBuilder party(String party) {
            this.party = party;
            return this;
        }

        public CandidateBuilder partySymbol(String partySymbol) {
            this.partySymbol = partySymbol;
            return this;
        }

        public CandidateBuilder constituency(String constituency) {
            this.constituency = constituency;
            return this;
        }

        public CandidateBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public Candidate build() {
            return new Candidate(this.candidateId, this.name, this.party, this.partySymbol, this.constituency, this.electionId);
        }
    }

    public static CandidateBuilder builder() { return new CandidateBuilder(); }
}

# VoteSecure: Biometric-Verified Remote Election Platform

VoteSecure is a state-of-the-art, secure digital voting system designed to provide maximum transparency and integrity in elections. It leverages Aadhaar-based biometric authentication and cryptographic hash-chaining to ensure that every vote is cast by a verified citizen and cannot be tampered with after being recorded.

## 🏛️ Architecture Overview

The system follows a modern multi-tiered architecture designed for security and scalability:

- **Frontend (React + TypeScript):** A sleek, responsive dashboard for voters, booth officers, and election administrators. Built with Vite for high performance.
- **Backend (Spring Boot):** A robust Java-based REST API that handles authentication, vote encryption, and election lifecycle management.
- **Identity Layer (Aadhaar Mock):** Simulates the Aadhaar biometric verification process (fingerprint scanning) to ensure "One Citizen, One Vote."
- **Integrity Layer (Cryptographic Hash Chain):** Every vote contains a hash of the previous vote, creating an immutable ledger similar to a blockchain. If even a single vote is altered, the entire chain breaks.

## ✨ Key Features

- **Biometric Authentication:** Voter identity is verified via mock Aadhaar fingerprint scanning before the ballot is unlocked.
- **Cryptographic Secrecy:** Votes are encrypted before being stored, ensuring the secrecy of the ballot.
- **Tamper-Proof Ledger:** Uses a SHA-256 hash chain to provide mathematical proof that the election data has not been modified.
- **Real-Time Turnout Monitoring:** Admin dashboard shows live voter turnout and data integrity status.
- **Role-Based Access Control:** 
  - **Voters:** Can only access the ballot after biometric verification.
  - **Booth Officers:** Authorized to unlock terminals and verify physical presence.
  - **Election Admins:** Full control over election lifecycle (starting counts, verifying chains, system reset).

## 🛠️ Tech Stack

- **Frontend:** React 19, TypeScript, Vite, Lucide React, Axios.
- **Backend:** Java 21, Spring Boot 3.3, Spring Security (JWT), Spring Data JPA.
- **Database:** H2 (In-memory for demo), support for PostgreSQL/MySQL.
- **Deployment:** Docker, Render (Blueprint support).

## 🚀 Getting Started

### Local Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Saisha16/Biometric-Verified-Remote-Election-Platform-.git
   ```

2. **Run the Backend:**
   ```bash
   # From the root directory
   mvn spring-boot:run
   ```
   The backend will start on `http://localhost:8080`.

3. **Run the Frontend:**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   The frontend will start on `http://localhost:5173`.

### 🔑 Demo Credentials

| Role | Username | Password |
| :--- | :--- | :--- |
| **Election Admin** | `admin` | `admin123` |
| **Booth Officer** | `officer1` | `officer123` |

**Sample Aadhaar Numbers for Verification:**
- `1234-5678-9012` (Arjun Mehta)
- `2345-6789-0123` (Sneha Reddy)
- `1111-2222-3333` (Ishan Malhotra)

## ☁️ Deployment

This project is configured for one-click deployment on **Render**.
1. Push this code to GitHub.
2. Go to Render and create a new **Blueprint**.
3. Connect this repository and click **Apply**.

## 🛡️ Security Note
This is a demonstration project. In a production environment, the Aadhaar verification would connect to the official UIDAI CIDR, and the encryption keys would be managed by a Hardware Security Module (HSM).

---
Built with ❤️ for a more transparent democracy.
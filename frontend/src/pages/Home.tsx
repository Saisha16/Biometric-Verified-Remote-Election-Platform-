import { useNavigate } from 'react-router-dom';
import { Fingerprint, UserCheck, Shield } from 'lucide-react';

export default function Home() {
  const navigate = useNavigate();

  return (
    <div className="animate-fade-in" style={{ maxWidth: '1000px', margin: '0 auto', textAlign: 'center' }}>
      <h1 className="text-gradient" style={{ fontSize: '3rem', marginBottom: '1rem' }}>Welcome to VoteSecure</h1>
      <p style={{ color: 'var(--text-secondary)', fontSize: '1.2rem', marginBottom: '4rem' }}>
        Aadhaar-Based Remote Voting System with Cryptographic Secrecy
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '2rem' }}>
        
        {/* Voter Card */}
        <div className="glass-card stat-card" style={{ cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '3rem 2rem' }} onClick={() => navigate('/voter')}>
          <div style={{ padding: '1.5rem', background: 'rgba(6, 182, 212, 0.1)', borderRadius: '50%', marginBottom: '1.5rem', border: '1px solid var(--accent-secondary)' }}>
            <Fingerprint size={48} color="var(--accent-secondary)" />
          </div>
          <h2 style={{ marginBottom: '1rem' }}>Voter</h2>
          <p style={{ color: 'var(--text-muted)', marginBottom: '2rem' }}>Cast your vote securely using biometric Aadhaar authentication.</p>
          <button className="btn btn-secondary" style={{ marginTop: 'auto', width: '100%' }}>Enter as Voter</button>
        </div>

        {/* Booth Officer Card */}
        <div className="glass-card stat-card" style={{ cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '3rem 2rem' }} onClick={() => navigate('/login?role=officer')}>
          <div style={{ padding: '1.5rem', background: 'rgba(79, 70, 229, 0.1)', borderRadius: '50%', marginBottom: '1.5rem', border: '1px solid var(--accent-primary)' }}>
            <UserCheck size={48} color="var(--accent-primary)" />
          </div>
          <h2 style={{ marginBottom: '1rem' }}>Booth Officer</h2>
          <p style={{ color: 'var(--text-muted)', marginBottom: '2rem' }}>Authenticate your physical voting terminal before voters arrive.</p>
          <button className="btn btn-primary" style={{ marginTop: 'auto', width: '100%' }}>Officer Login</button>
        </div>

        {/* Admin Card */}
        <div className="glass-card stat-card" style={{ cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '3rem 2rem' }} onClick={() => navigate('/login?role=admin')}>
          <div style={{ padding: '1.5rem', background: 'rgba(16, 185, 129, 0.1)', borderRadius: '50%', marginBottom: '1.5rem', border: '1px solid var(--status-success)' }}>
            <Shield size={48} color="var(--status-success)" />
          </div>
          <h2 style={{ marginBottom: '1rem' }}>Election Admin</h2>
          <p style={{ color: 'var(--text-muted)', marginBottom: '2rem' }}>Monitor live turnout, verify hash chains, and start the counting process.</p>
          <button className="btn btn-secondary" style={{ marginTop: 'auto', width: '100%', borderColor: 'var(--status-success)', color: 'var(--status-success)' }}>Admin Login</button>
        </div>

      </div>
    </div>
  );
}

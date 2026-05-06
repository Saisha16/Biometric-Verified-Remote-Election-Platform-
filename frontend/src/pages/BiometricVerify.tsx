import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Fingerprint, Loader2, AlertCircle, Lock } from 'lucide-react';
import { api } from '../api/apiClient';

export default function BiometricVerify() {
  const navigate = useNavigate();
  const [aadhaar, setAadhaar] = useState('1234-5678-9012'); // Default to Arjun Mehta
  const [isScanning, setIsScanning] = useState(false);
  const [error, setError] = useState('');
  
  const boothId = localStorage.getItem('boothId');

  // If no officer has logged in to authenticate the terminal, show it as locked
  if (!boothId) {
    return (
      <div className="auth-container animate-fade-in">
        <div className="glass-card auth-card" style={{ textAlign: 'center' }}>
          <div style={{ display: 'inline-flex', padding: '1.5rem', background: 'rgba(239, 68, 68, 0.1)', borderRadius: '50%', marginBottom: '1.5rem', border: '1px solid var(--status-error)' }}>
            <Lock size={48} color="var(--status-error)" />
          </div>
          <h2>Terminal Locked</h2>
          <p style={{ color: 'var(--text-secondary)', marginTop: '0.5rem', marginBottom: '2rem' }}>
            This voting terminal has not been authenticated. A Booth Officer must unlock this machine before voters can cast their ballots.
          </p>
          <button className="btn btn-primary" onClick={() => navigate('/login?role=officer')} style={{ width: '100%' }}>
            Officer Login
          </button>
        </div>
      </div>
    );
  }

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsScanning(true);
    setError('');

    try {
      // Simulate fingerprint mapping for demo
      const fingerprintMapping: Record<string, string> = {
        '1234-5678-9012': 'fingerprint_arjun',
        '2345-6789-0123': 'fingerprint_sneha',
        '3456-7890-1234': 'fingerprint_vikram',
        '4567-8901-2345': 'fingerprint_ananya',
        '5678-9012-3456': 'fingerprint_rahul',
        '6789-0123-4567': 'fingerprint_kavya',
        '7890-1234-5678': 'fingerprint_rohan',
        '8901-2345-6789': 'fingerprint_neha',
        '9012-3456-7890': 'fingerprint_siddharth',
        '0123-4567-8901': 'fingerprint_ayesha',
        '1111-2222-3333': 'fingerprint_ishan',
        '4444-5555-6666': 'fingerprint_tanvi',
        '7777-8888-9999': 'fingerprint_aditya',
        '1212-3434-5656': 'fingerprint_meera',
        '7878-9090-1212': 'fingerprint_kabir'
      };
      
      const fingerprint = fingerprintMapping[aadhaar] || 'unknown_fingerprint';

      // Call biometric verification API
      const response = await api.post('/biometric/verify', {
        aadhaarNumber: aadhaar,
        fingerprintData: fingerprint
      });

      const data = response.data;

      if (data.verified) {
        if (data.hasVoted) {
          setError('This voter has already cast their vote.');
          setIsScanning(false);
          return;
        }

        // Store voter session
        sessionStorage.setItem('voterId', data.voterId);
        sessionStorage.setItem('constituency', data.constituency);
        sessionStorage.setItem('voterName', data.name);
        
        // Navigate to ballot
        navigate('/ballot');
      }
    } catch (err: any) {
      setError(err.message || 'Verification failed. Please try again.');
    } finally {
      setIsScanning(false);
    }
  };

  return (
    <div className="auth-container animate-fade-in">
      <div className="glass-card auth-card">
        <h2 style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <span className="text-gradient">Biometric Authentication</span>
        </h2>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid var(--status-error)', padding: '1rem', borderRadius: '8px', color: 'var(--status-error)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <AlertCircle size={20} />
            <span>{error}</span>
          </div>
        )}

        <div className="scanner-overlay">
          {isScanning && <div className="scanner-line"></div>}
          <div className="fingerprint-icon">
            <Fingerprint size={80} color={isScanning ? "var(--accent-secondary)" : "rgba(255, 255, 255, 0.2)"} />
          </div>
        </div>

        <form onSubmit={handleVerify}>
          <div className="input-group">
            <label className="input-label">Aadhaar Number</label>
            <input 
              type="text" 
              className="input-field" 
              value={aadhaar}
              onChange={(e) => setAadhaar(e.target.value)}
              placeholder="0000-0000-0000"
              required
              disabled={isScanning}
            />
          </div>
          
          <button 
            type="submit" 
            className="btn btn-primary" 
            style={{ width: '100%' }}
            disabled={isScanning}
          >
            {isScanning ? (
              <><Loader2 className="animate-spin" size={20} /> Verifying Identity...</>
            ) : (
              'Scan & Verify'
            )}
          </button>
        </form>

        <div style={{ marginTop: '2rem', fontSize: '0.85rem', color: 'var(--text-muted)', textAlign: 'center' }}>
          <p>Demo Aadhaar Numbers:</p>
          <p>1234-5678-9012 (Arjun) | 2345-6789-0123 (Sneha)</p>
          <p>1111-2222-3333 (Ishan) | 4444-5555-6666 (Tanvi)</p>
          <p>7777-8888-9999 (Aditya) | 7878-9090-1212 (Kabir)</p>
        </div>
      </div>
    </div>
  );
}

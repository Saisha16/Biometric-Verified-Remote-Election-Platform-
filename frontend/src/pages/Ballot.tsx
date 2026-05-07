import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { CheckCircle2, ShieldAlert, Loader2 } from 'lucide-react';
import { api } from '../api/apiClient';

interface Candidate {
  candidateId: string;
  name: string;
  party: string;
  partySymbol: string;
}

export default function Ballot() {
  const navigate = useNavigate();
  const [candidates, setCandidates] = useState<Candidate[]>([]);
  const [selectedCandidate, setSelectedCandidate] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const voterId = sessionStorage.getItem('voterId');
  const constituency = sessionStorage.getItem('constituency');
  const voterName = sessionStorage.getItem('voterName');

  useEffect(() => {
    if (!voterId || !constituency) {
      navigate('/voter');
      return;
    }

    const fetchBallot = async () => {
      try {
        const response = await api.get(`/vote/ballot/GE2024/${constituency}`);
        setCandidates(response.data);
      } catch (err: any) {
        setError('Failed to load ballot: ' + err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchBallot();
  }, [navigate, voterId, constituency]);

  const handleCastVote = async () => {
    if (!selectedCandidate) return;
    
    setIsSubmitting(true);
    setError('');

    try {
      const response = await api.post('/demo/vote', {
        voterId,
        electionId: 'GE2024',
        candidateId: selectedCandidate
      });

      // Clear voter session to prevent back-button voting
      sessionStorage.removeItem('voterId');
      sessionStorage.removeItem('constituency');
      sessionStorage.removeItem('voterName');

      // Store receipt for display
      sessionStorage.setItem('receiptCode', response.data.receiptCode);
      sessionStorage.setItem('timestamp', response.data.timestamp);
      
      navigate('/receipt');
    } catch (err: any) {
      setError(err.message || 'Failed to cast vote.');
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return <div className="auth-container"><Loader2 className="animate-spin" size={48} color="var(--accent-primary)" /></div>;
  }

  return (
    <div className="animate-fade-in" style={{ maxWidth: '800px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <div>
          <h1 className="text-gradient">Electronic Ballot</h1>
          <p style={{ color: 'var(--text-secondary)' }}>Constituency: {constituency}</p>
        </div>
        <div style={{ textAlign: 'right', background: 'rgba(255,255,255,0.05)', padding: '0.75rem 1.5rem', borderRadius: 'var(--radius-md)' }}>
          <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block' }}>Verified Voter</span>
          <span style={{ fontWeight: 600 }}>{voterName}</span>
        </div>
      </div>

      {error && (
        <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid var(--status-error)', padding: '1rem', borderRadius: '8px', color: 'var(--status-error)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <ShieldAlert size={20} />
          <span>{error}</span>
        </div>
      )}

      <div className="glass-card" style={{ marginBottom: '2rem' }}>
        <div className="candidate-list">
          {candidates.map((c) => (
            <div 
              key={c.candidateId} 
              className={`candidate-item ${selectedCandidate === c.candidateId ? 'selected' : ''}`}
              onClick={() => setSelectedCandidate(c.candidateId)}
            >
              <div className="candidate-info">
                <div className="candidate-symbol">{c.partySymbol}</div>
                <div>
                  <div className="candidate-name">{c.name}</div>
                  <div className="candidate-party">{c.party}</div>
                </div>
              </div>
              <div>
                <div style={{ 
                  width: '24px', height: '24px', borderRadius: '50%', 
                  border: `2px solid ${selectedCandidate === c.candidateId ? 'var(--accent-primary)' : 'var(--border-color)'}`,
                  background: selectedCandidate === c.candidateId ? 'var(--accent-primary)' : 'transparent',
                  display: 'flex', alignItems: 'center', justifyContent: 'center'
                }}>
                  {selectedCandidate === c.candidateId && <CheckCircle2 size={16} color="white" />}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem' }}>
        <button className="btn btn-secondary" onClick={() => navigate('/voter')} disabled={isSubmitting}>
          Cancel
        </button>
        <button 
          className="btn btn-primary" 
          disabled={!selectedCandidate || isSubmitting}
          onClick={handleCastVote}
          style={{ padding: '1rem 3rem', fontSize: '1.1rem' }}
        >
          {isSubmitting ? <><Loader2 className="animate-spin" size={20} /> Encrypting Vote...</> : 'CAST VOTE'}
        </button>
      </div>
      
      <p style={{ textAlign: 'center', marginTop: '2rem', color: 'var(--text-muted)', fontSize: '0.85rem' }}>
        <ShieldAlert size={14} style={{ display: 'inline', verticalAlign: 'text-bottom', marginRight: '4px' }} />
        Your vote will be encrypted with AES-256 before leaving this device. Your identity is permanently separated from your vote.
      </p>
    </div>
  );
}

import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ShieldCheck, ArrowRight, Lock } from 'lucide-react';

export default function Receipt() {
  const navigate = useNavigate();
  const receiptCode = sessionStorage.getItem('receiptCode');
  const timestamp = sessionStorage.getItem('timestamp');

  useEffect(() => {
    if (!receiptCode) {
      navigate('/voter');
    }
  }, [receiptCode, navigate]);

  const handleFinish = () => {
    sessionStorage.clear();
    navigate('/voter');
  };

  if (!receiptCode) return null;

  return (
    <div className="auth-container animate-fade-in">
      <div className="glass-card auth-card" style={{ textAlign: 'center' }}>
        <div style={{ display: 'inline-flex', padding: '1rem', background: 'rgba(16, 185, 129, 0.1)', borderRadius: '50%', marginBottom: '1.5rem', border: '1px solid var(--status-success)', animation: 'pulse 2s infinite' }}>
          <ShieldCheck size={48} color="var(--status-success)" />
        </div>
        
        <h2><span className="text-gradient">Vote Recorded Securely</span></h2>
        <p style={{ color: 'var(--text-secondary)', marginTop: '0.5rem', marginBottom: '2rem' }}>
          Your vote has been encrypted and added to the immutable ledger.
        </p>

        <div style={{ background: 'rgba(0,0,0,0.3)', padding: '1.5rem', borderRadius: 'var(--radius-md)', border: '1px dashed var(--border-highlight)', marginBottom: '2rem' }}>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Tracking Receipt</p>
          <div style={{ fontFamily: 'monospace', fontSize: '1.5rem', fontWeight: 'bold', margin: '0.5rem 0', letterSpacing: '2px', color: 'var(--accent-secondary)' }}>
            {receiptCode}
          </div>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
            {new Date(timestamp!).toLocaleString()}
          </p>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', justifyContent: 'center', color: 'var(--text-muted)', fontSize: '0.85rem', marginBottom: '2rem' }}>
          <Lock size={14} />
          <span>This receipt cannot be used to reveal who you voted for.</span>
        </div>

        <button className="btn btn-primary" onClick={handleFinish} style={{ width: '100%' }}>
          Finish & Logout <ArrowRight size={18} />
        </button>
      </div>
    </div>
  );
}

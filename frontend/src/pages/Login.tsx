import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Lock, Loader2, AlertCircle } from 'lucide-react';
import { api } from '../api/apiClient';

export default function Login() {
  const navigate = useNavigate();
  const [] = useSearchParams();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  // If already logged in, redirect away from login page
  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    if (token) {
      if (role === 'ELECTION_ADMIN') {
        navigate('/admin');
      } else {
        navigate('/voter');
      }
    }
  }, [navigate]);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      const response = await api.post('/auth/login', {
        username,
        password
      });

      const { token, role, boothId } = response.data;
      
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
      if (boothId) localStorage.setItem('boothId', boothId);
      
      if (role === 'ELECTION_ADMIN') {
        navigate('/admin');
      } else {
        navigate('/voter'); // Booth officers go to the scanner
      }
    } catch (err: any) {
      setError(err.message || 'Invalid credentials');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-container animate-fade-in">
      <div className="glass-card auth-card">
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{ display: 'inline-flex', padding: '1rem', background: 'rgba(79, 70, 229, 0.1)', borderRadius: '50%', marginBottom: '1rem' }}>
            <Lock size={32} color="var(--accent-primary)" />
          </div>
          <h2>Authorized Access</h2>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Booth Officers & Election Commission</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid var(--status-error)', padding: '1rem', borderRadius: '8px', color: 'var(--status-error)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <AlertCircle size={20} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label className="input-label">Username</label>
            <input 
              type="text" 
              className="input-field" 
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          
          <div className="input-group">
            <label className="input-label">Password</label>
            <input 
              type="password" 
              className="input-field" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          
          <button 
            type="submit" 
            className="btn btn-primary" 
            style={{ width: '100%', marginTop: '1rem' }}
            disabled={isLoading}
          >
            {isLoading ? <><Loader2 className="animate-spin" size={20} /> Authenticating...</> : 'Login securely'}
          </button>
        </form>

        <div style={{ marginTop: '2rem', fontSize: '0.85rem', color: 'var(--text-muted)', textAlign: 'center' }}>
          <p>Demo Admin: admin / admin123</p>
          <p>Demo Officer: officer1 / officer123</p>
        </div>
      </div>
    </div>
  );
}

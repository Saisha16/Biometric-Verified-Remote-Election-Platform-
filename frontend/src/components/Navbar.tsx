import { Link, useNavigate } from 'react-router-dom';
import { ShieldCheck, LogOut, LayoutDashboard, Fingerprint } from 'lucide-react';

export default function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('boothId');
    navigate('/');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="nav-brand">
        <ShieldCheck className="nav-logo" size={32} />
        <span>VoteSecure</span>
      </Link>
      
      <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
        {token ? (
          <>
            <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
              Booth: {localStorage.getItem('boothId')}
            </span>
            {role === 'ELECTION_ADMIN' && (
              <Link to="/admin" className="btn btn-secondary" style={{ padding: '0.5rem 1rem' }}>
                <LayoutDashboard size={18} />
                Dashboard
              </Link>
            )}
            {role === 'BOOTH_OFFICER' && (
              <Link to="/voter" className="btn btn-secondary" style={{ padding: '0.5rem 1rem' }}>
                <Fingerprint size={18} />
                Terminal
              </Link>
            )}
            <button onClick={handleLogout} className="btn btn-danger" style={{ padding: '0.5rem 1rem' }}>
              <LogOut size={18} />
              Logout
            </button>
          </>
        ) : (
          <Link to="/login" className="btn btn-secondary">
            Officer Login
          </Link>
        )}
      </div>
    </nav>
  );
}

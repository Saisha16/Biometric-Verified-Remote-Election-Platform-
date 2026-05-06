import React, { useState, useEffect } from 'react';
import { Users, Vote, Link, Play, ShieldCheck, Loader2, RotateCcw } from 'lucide-react';
import { api } from '../api/apiClient';

export default function AdminDashboard() {
  const [turnout, setTurnout] = useState<any>(null);
  const [results, setResults] = useState<any>(null);
  const [chainStatus, setChainStatus] = useState<any>(null);
  const [isCounting, setIsCounting] = useState(false);
  const [isLoadingCounting, setIsLoadingCounting] = useState(false);
  const [resultsError, setResultsError] = useState('');
  const [loading, setLoading] = useState(true);

  const ELECTION_ID = 'GE2024';

  const fetchData = async () => {
    try {
      const turnoutRes = await api.get(`/vote/turnout/${ELECTION_ID}`);
      setTurnout(turnoutRes.data);
      
      // Try to fetch results if counting has started
      try {
        const resultsRes = await api.get(`/results/${ELECTION_ID}`);
        if (resultsRes.data) {
          setResults(resultsRes.data);
          setIsCounting(true);
          setResultsError(''); 
        }
      } catch (e: any) {
        if (isCounting) {
          setResultsError(e.message || "Failed to fetch live results.");
        }
      }
    } catch (err) {
      console.error("Failed to fetch data", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 5000);
    return () => clearInterval(interval);
  }, []);

  const resetElection = async () => {
    if (!window.confirm("CRITICAL: This will PERMANENTLY delete all cast votes and reset the election. Are you sure?")) return;
    
    try {
      await api.post('/admin/reset-election');
      setResults(null);
      setIsCounting(false);
      setResultsError('');
      await fetchData();
      alert("System reset successfully. You can now start fresh.");
    } catch (err: any) {
      alert("Reset failed: " + (err.message || "Server error"));
    }
  };

  const startCounting = async () => {
    console.log("Start Counting clicked!");
    setIsLoadingCounting(true);
    try {
      console.log("Making API call to start counting...");
      const response = await api.post(`/results/start-counting/${ELECTION_ID}`, {}, {
        headers: { 'X-Admin-Id': 'admin' }
      });
      console.log("API Response:", response.data);
      
      // Give the backend a moment to process the state change
      setTimeout(async () => {
        await fetchData();
        setIsCounting(true);
        setIsLoadingCounting(false);
      }, 1000);
    } catch (err: any) {
      console.error("Start counting failed:", err);
      setIsLoadingCounting(false);
      alert("Failed to start counting: " + (err.message || "Server error"));
    }
  };

  const verifyChain = async () => {
    try {
      const res = await api.get(`/results/${ELECTION_ID}/verify-chain`);
      setChainStatus(res.data);
    } catch (err) {
      alert("Failed to verify chain.");
    }
  };

  if (loading && !turnout) {
    return <div className="auth-container"><Loader2 className="animate-spin" size={48} color="var(--accent-primary)" /></div>;
  }

  return (
    <div className="animate-fade-in">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <div>
          <h1>Election Dashboard</h1>
          <p style={{ color: 'var(--text-secondary)' }}>ID: {ELECTION_ID} | LIVE MONITORING</p>
        </div>
        
        <div style={{ display: 'flex', gap: '1rem' }}>
          <button className="btn btn-secondary" onClick={resetElection} style={{ borderColor: 'var(--status-error)', color: 'var(--status-error)' }}>
            <RotateCcw size={18} /> Reset System
          </button>
          <button className="btn btn-secondary" onClick={verifyChain}>
            <Link size={18} /> Verify Hash Chain
          </button>
          {!isCounting && (
            <button className="btn btn-primary" onClick={startCounting} disabled={isLoadingCounting}>
              {isLoadingCounting ? (
                <><Loader2 className="animate-spin" size={18} /> Initializing...</>
              ) : (
                <><Play size={18} /> Start Counting</>
              )}
            </button>
          )}
        </div>
      </div>

      {chainStatus && (
        <div className="glass-card animate-fade-in" style={{ marginBottom: '2rem', border: `1px solid ${chainStatus.chainValid ? 'var(--status-success)' : 'var(--status-error)'}` }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            {chainStatus.chainValid ? <ShieldCheck size={32} color="var(--status-success)" /> : <ShieldCheck size={32} color="var(--status-error)" />}
            <div>
              <h3 style={{ color: chainStatus.chainValid ? 'var(--status-success)' : 'var(--status-error)' }}>
                {chainStatus.chainValid ? 'Integrity Verified: Chain Intact' : 'INTEGRITY COMPROMISED: CHAIN BROKEN'}
              </h3>
              <p style={{ color: 'var(--text-secondary)' }}>{chainStatus.message}</p>
            </div>
          </div>
        </div>
      )}

      <div className="dashboard-grid" style={{ marginBottom: '3rem' }}>
        <div className="stat-card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <div className="stat-title">Registered Voters</div>
              <div className="stat-value">{turnout?.totalRegisteredVoters || 0}</div>
            </div>
            <Users color="var(--accent-secondary)" size={28} />
          </div>
        </div>
        
        <div className="stat-card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <div className="stat-title">Votes Cast</div>
              <div className="stat-value">{turnout?.totalVotesCast || 0}</div>
            </div>
            <Vote color="var(--accent-primary)" size={28} />
          </div>
          <div style={{ marginTop: '1rem', width: '100%', background: 'var(--bg-secondary)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
            <div style={{ height: '100%', width: `${turnout?.turnoutPercentage || 0}%`, background: 'var(--gradient-primary)' }}></div>
          </div>
          <div style={{ marginTop: '0.5rem', fontSize: '0.85rem', color: 'var(--text-muted)', textAlign: 'right' }}>
            {turnout?.turnoutPercentage}% Turnout
          </div>
        </div>

        <div className="stat-card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <div className="stat-title">Data Integrity Check</div>
              <div className="stat-value" style={{ fontSize: '1.5rem', marginTop: '0.5rem', color: turnout?.countsMatch ? 'var(--status-success)' : 'var(--status-error)' }}>
                {turnout?.countsMatch ? 'MATCH' : 'MISMATCH'}
              </div>
            </div>
            <ShieldCheck color={turnout?.countsMatch ? "var(--status-success)" : "var(--status-error)"} size={28} />
          </div>
          <p style={{ marginTop: '1rem', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            Turnout vs Encrypted Ledger
          </p>
        </div>
      </div>

      {isCounting && (
        <div className="glass-card animate-fade-in">
          <h2 style={{ marginBottom: '1.5rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '1rem' }}>Live Results</h2>
          
          {resultsError ? (
            <div style={{ color: 'var(--status-error)', padding: '1rem', background: 'rgba(239, 68, 68, 0.1)', borderRadius: '8px', textAlign: 'center' }}>
              <ShieldCheck size={20} style={{ verticalAlign: 'middle', marginRight: '0.5rem' }} />
              {resultsError}
            </div>
          ) : results && (
            results.results.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '2rem', color: 'var(--text-muted)' }}>
                No votes have been cast yet for this election.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {results.results.map((r: any, idx: number) => {
                  const percentage = results.totalVotesCounted > 0 ? (r.voteCount / results.totalVotesCounted) * 100 : 0;
                  return (
                    <div key={r.candidateId} style={{ background: 'var(--bg-secondary)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                        <div>
                          <span style={{ fontWeight: 600, marginRight: '0.5rem' }}>{r.candidateName}</span>
                          <span style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>({r.party})</span>
                        </div>
                        <div style={{ fontWeight: 700, fontSize: '1.1rem' }}>{r.voteCount} votes</div>
                      </div>
                      <div style={{ width: '100%', background: 'var(--bg-tertiary)', height: '12px', borderRadius: '6px', overflow: 'hidden' }}>
                        <div style={{ height: '100%', width: `${percentage}%`, background: idx === 0 ? 'var(--status-success)' : 'var(--accent-primary)', transition: 'width 1s ease-out' }}></div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )
          )}
        </div>
      )}
    </div>
  );
}

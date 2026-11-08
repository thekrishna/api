const claims = [
  { claimId: 'CLM-1001', claimantName: 'Jane Doe', status: 'IN_PROGRESS', amount: '$15,400.00' },
  { claimId: 'CLM-1002', claimantName: 'John Smith', status: 'REVIEW_REQUIRED', amount: '$23,750.00' },
  { claimId: 'CLM-1003', claimantName: 'Alicia Jones', status: 'SETTLED', amount: '$11,200.00' }
];

const statusColors: Record<string, string> = {
  IN_PROGRESS: '#3b82f6',
  REVIEW_REQUIRED: '#f59e0b',
  SETTLED: '#22c55e'
};

export default function App() {
  return (
    <div className="page-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Property Claims</p>
          <h1>FNOL Claims Dashboard</h1>
        </div>
        <button className="primary-btn">New FNOL</button>
      </header>

      <section className="summary-grid">
        <div className="stat-card">
          <span>Total Claims</span>
          <strong>128</strong>
        </div>
        <div className="stat-card">
          <span>In Progress</span>
          <strong>42</strong>
        </div>
        <div className="stat-card">
          <span>Human Review</span>
          <strong>9</strong>
        </div>
        <div className="stat-card">
          <span>Settled</span>
          <strong>77</strong>
        </div>
      </section>

      <section className="content-grid">
        <div className="panel">
          <h2>Open claims</h2>
          <table>
            <thead>
              <tr>
                <th>Claim ID</th>
                <th>Claimant</th>
                <th>Status</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              {claims.map((claim) => (
                <tr key={claim.claimId}>
                  <td>{claim.claimId}</td>
                  <td>{claim.claimantName}</td>
                  <td>
                    <span
                      className="status-pill"
                      style={{ backgroundColor: `${statusColors[claim.status]}22`, color: statusColors[claim.status] }}
                    >
                      {claim.status}
                    </span>
                  </td>
                  <td>{claim.amount}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="panel form-panel">
          <h2>FNOL submission</h2>
          <form>
            <label>
              Policy number
              <input defaultValue="POL-9001" />
            </label>
            <label>
              Claimant
              <input defaultValue="Jane Doe" />
            </label>
            <label>
              Claim type
              <select defaultValue="PROPERTY">
                <option value="PROPERTY">Property</option>
                <option value="AUTO">Auto</option>
              </select>
            </label>
            <label>
              Description
              <textarea defaultValue="Roof damage after hailstorm." />
            </label>
            <button type="button" className="primary-btn">Submit claim</button>
          </form>
        </div>
      </section>
    </div>
  );
}

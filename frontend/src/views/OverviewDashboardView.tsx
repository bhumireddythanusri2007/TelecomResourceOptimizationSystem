import React from 'react';
import { useData } from '../context/DataContext';
import { KpiCard } from '../components/common/KpiCard';
import {
  Radio,
  Smartphone,
  Users,
  Activity,
  Bell,
  Zap,
  Gauge,
  ArrowUpRight,
  ShieldAlert,
  Server
} from 'lucide-react';
import {
  AreaChart,
  Area,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend
} from 'recharts';
import { NavModule, NetworkCell, Alert, NetworkSlice, NetworkKPI } from '../types';

interface OverviewProps {
  onNavigate: (module: NavModule) => void;
}

export const OverviewDashboardView: React.FC<OverviewProps> = ({ onNavigate }) => {
  const { data, loading } = useData();
  const { stats, cells, alerts, slices, kpis } = data;

  const cellLoadData = cells.slice(0, 8).map((c: NetworkCell) => ({
    name: c.cellName ? c.cellName.replace(/^Cell[-_]/i, '') : `C-${c.cellId}`,
    Users: c.currentUsers || 0,
    Capacity: c.totalCapacity || 0,
    utilization: c.totalCapacity ? Math.round((c.currentUsers / c.totalCapacity) * 100) : 0,
  }));

  // Actual database telemetry: maps real cells with corresponding KPI throughput records
  const telemetryData = React.useMemo(() => {
    if (cells.length === 0 && kpis.length === 0) return [];

    return cells.slice(0, 8).map((c: NetworkCell, index: number) => {
      const kpi = kpis.find((k: NetworkKPI) => k.cellId === c.cellId) || kpis[index];
      const utilization = c.totalCapacity ? Math.round((c.currentUsers / c.totalCapacity) * 100) : 0;
      const throughput = kpi ? Math.round(kpi.throughput) : 0;
      const label = c.cellName ? c.cellName.replace(/^Cell[-_]/i, 'Cell ') : `Cell ${c.cellId}`;

      return {
        label,
        load: utilization,
        throughput: throughput,
      };
    });
  }, [cells, kpis]);

  // Alert Severity Mapping: Consistent categorization of database severities (CRITICAL/HIGH, WARNING/MEDIUM, INFO/LOW)
  const criticalCount = alerts.filter(a => {
    const s = (a.severity || '').toUpperCase().trim();
    return s === 'CRITICAL' || s === 'HIGH';
  }).length;

  const warningCount = alerts.filter(a => {
    const s = (a.severity || '').toUpperCase().trim();
    return s === 'WARNING' || s === 'MEDIUM';
  }).length;

  const infoCount = alerts.filter(a => {
    const s = (a.severity || '').toUpperCase().trim();
    return s === 'INFO' || s === 'LOW' || (!['CRITICAL', 'HIGH', 'WARNING', 'MEDIUM'].includes(s) && s !== '');
  }).length;

  const totalAlertsCount = criticalCount + warningCount + infoCount;

  const alertPieData = [
    { name: 'Critical / High', value: criticalCount, color: '#ef4444' },
    { name: 'Warning / Med', value: warningCount, color: '#f59e0b' },
    { name: 'Info / Low', value: infoCount, color: '#3b82f6' },
  ].filter(d => d.value > 0);

  const sliceData = slices.map((s: NetworkSlice) => ({
    name: s.sliceName ? s.sliceName.replace(/^Slice[-_]/i, '') : `S-${s.sliceId}`,
    Users: s.connectedUsers || 0,
    Bandwidth: s.bandwidth || 0,
  }));

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-navy-900/60 border border-blue-900/30 p-5 rounded-2xl backdrop-blur-md">
        <div>
          <h2 className="text-xl font-bold text-white tracking-tight flex items-center gap-2">
            Network Operations & Telemetry Overview
          </h2>
          <p className="text-xs text-slate-400 mt-1">
            Real-time status of radio cells, 5G slices, edge computing resources, and rule-based optimization.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={() => onNavigate('optimization')}
            className="inline-flex items-center gap-2 px-3.5 py-2 rounded-xl bg-blue-600 hover:bg-blue-500 text-white text-xs font-semibold shadow-[0_0_15px_rgba(37,99,235,0.3)] transition"
          >
            <Zap className="w-4 h-4" />
            Run Optimization Engine
          </button>
        </div>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-7 gap-3.5">
        <KpiCard
          title="Network Cells"
          value={stats.networkCells}
          unit="cells"
          subtext="Base stations registered"
          icon={Radio}
          variant="blue"
          loading={loading}
        />
        <KpiCard
          title="Devices"
          value={stats.devices}
          unit="units"
          subtext="Nodes & RRUs online"
          icon={Smartphone}
          variant="cyan"
          loading={loading}
        />
        <KpiCard
          title="Subscribers"
          value={stats.subscribers}
          unit="accounts"
          subtext="Active subscriber profiles"
          icon={Users}
          variant="purple"
          loading={loading}
        />
        <KpiCard
          title="Traffic Flows"
          value={stats.trafficRecords}
          unit="flows"
          subtext="Packets & flows analyzed"
          icon={Activity}
          variant="emerald"
          loading={loading}
        />
        <KpiCard
          title="Active Alerts"
          value={stats.alerts}
          unit="events"
          subtext="System notifications"
          icon={Bell}
          variant="rose"
          loading={loading}
        />
        <KpiCard
          title="Optimizations"
          value={stats.optimizationResults}
          unit="runs"
          subtext="Simulated rebalances"
          icon={Zap}
          variant="amber"
          loading={loading}
        />
        <KpiCard
          title="KPI Records"
          value={stats.kpiRecords}
          unit="metrics"
          subtext="Throughput / Latency rows"
          icon={Gauge}
          variant="blue"
          loading={loading}
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider flex items-center gap-2">
                <Activity className="w-4 h-4 text-blue-400" />
                Network Traffic Load & Throughput Telemetry
              </h3>
              <p className="text-xs text-slate-400 mt-0.5">
                Aggregated cell load (%) and aggregate throughput (Mbps) over recorded periods
              </p>
            </div>
            <button
              onClick={() => onNavigate('traffic')}
              className="text-xs text-blue-400 hover:text-blue-300 font-semibold inline-flex items-center gap-1"
            >
              View Flows <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="h-64 w-full">
            {telemetryData.length === 0 ? (
              <div className="flex items-center justify-center h-full text-slate-500 text-xs font-mono">
                Telemetry records unavailable from database
              </div>
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={telemetryData}>
                  <defs>
                    <linearGradient id="loadGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.4} />
                      <stop offset="95%" stopColor="#3b82f6" stopOpacity={0.0} />
                    </linearGradient>
                    <linearGradient id="tpGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#10b981" stopOpacity={0.3} />
                      <stop offset="95%" stopColor="#10b981" stopOpacity={0.0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                  <XAxis dataKey="label" stroke="#64748b" tick={{ fontSize: 10 }} />
                  <YAxis stroke="#64748b" tick={{ fontSize: 10 }} />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: '#0b1329',
                      borderColor: '#1e3a8a',
                      borderRadius: '8px',
                      fontSize: '12px',
                      color: '#e2e8f0',
                    }}
                  />
                  <Legend wrapperStyle={{ fontSize: '11px', paddingTop: '10px' }} />
                  <Area
                    type="monotone"
                    dataKey="load"
                    name="Traffic Load (%)"
                    stroke="#3b82f6"
                    strokeWidth={2}
                    fillOpacity={1}
                    fill="url(#loadGrad)"
                  />
                  <Area
                    type="monotone"
                    dataKey="throughput"
                    name="Throughput (Mbps)"
                    stroke="#10b981"
                    strokeWidth={2}
                    fillOpacity={1}
                    fill="url(#tpGrad)"
                  />
                </AreaChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>

        <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md flex flex-col">
          <div className="flex items-center justify-between mb-2">
            <div>
              <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider flex items-center gap-2">
                <ShieldAlert className="w-4 h-4 text-rose-400" />
                Alerts by Severity
              </h3>
              <p className="text-xs text-slate-400 mt-0.5">
                Current severity distribution
              </p>
            </div>
            <button
              onClick={() => onNavigate('alerts')}
              className="text-xs text-blue-400 hover:text-blue-300 font-semibold inline-flex items-center gap-1"
            >
              All Alerts <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="h-56 w-full my-auto">
            {totalAlertsCount === 0 ? (
              <div className="flex flex-col items-center justify-center h-full text-slate-500 text-xs">
                <ShieldAlert className="w-8 h-8 text-slate-600 mb-2" />
                <span>No active network alerts logged</span>
              </div>
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={alertPieData}
                    cx="50%"
                    cy="50%"
                    innerRadius={50}
                    outerRadius={75}
                    paddingAngle={5}
                    dataKey="value"
                  >
                    {alertPieData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip
                    contentStyle={{
                      backgroundColor: '#0b1329',
                      borderColor: '#1e3a8a',
                      borderRadius: '8px',
                      fontSize: '12px',
                      color: '#e2e8f0',
                    }}
                  />
                  <Legend wrapperStyle={{ fontSize: '11px' }} />
                </PieChart>
              </ResponsiveContainer>
            )}
          </div>

          <div className="grid grid-cols-3 gap-2 pt-2 border-t border-navy-800 text-center text-xs">
            <div className="p-1.5 rounded bg-rose-950/40 border border-rose-900/50">
              <span className="text-[10px] text-rose-400 block uppercase font-medium">Critical / High</span>
              <span className="font-bold text-rose-200">{criticalCount}</span>
            </div>
            <div className="p-1.5 rounded bg-amber-950/40 border border-amber-900/50">
              <span className="text-[10px] text-amber-400 block uppercase font-medium">Warning / Med</span>
              <span className="font-bold text-amber-200">{warningCount}</span>
            </div>
            <div className="p-1.5 rounded bg-blue-950/40 border border-blue-900/50">
              <span className="text-[10px] text-blue-400 block uppercase font-medium">Info / Low</span>
              <span className="font-bold text-blue-200">{infoCount}</span>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider flex items-center gap-2">
                <Radio className="w-4 h-4 text-emerald-400" />
                Cell Capacity vs Connected Users
              </h3>
              <p className="text-xs text-slate-400 mt-0.5">
                Current active users compared against station capacity limit
              </p>
            </div>
            <button
              onClick={() => onNavigate('network-cells')}
              className="text-xs text-blue-400 hover:text-blue-300 font-semibold inline-flex items-center gap-1"
            >
              All Cells <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="h-60 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={cellLoadData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#64748b" tick={{ fontSize: 10 }} />
                <YAxis stroke="#64748b" tick={{ fontSize: 10 }} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#0b1329',
                    borderColor: '#1e3a8a',
                    borderRadius: '8px',
                    fontSize: '12px',
                    color: '#e2e8f0',
                  }}
                />
                <Legend wrapperStyle={{ fontSize: '11px', paddingTop: '8px' }} />
                <Bar dataKey="Users" fill="#3b82f6" radius={[4, 4, 0, 0]} />
                <Bar dataKey="Capacity" fill="#1e3a8a" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider flex items-center gap-2">
                <Server className="w-4 h-4 text-purple-400" />
                5G Network Slice Allocation
              </h3>
              <p className="text-xs text-slate-400 mt-0.5">
                Bandwidth reservation (MHz) across active service profiles
              </p>
            </div>
            <button
              onClick={() => onNavigate('network-slices')}
              className="text-xs text-blue-400 hover:text-blue-300 font-semibold inline-flex items-center gap-1"
            >
              Manage Slices <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="h-60 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={sliceData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis type="number" stroke="#64748b" tick={{ fontSize: 10 }} />
                <YAxis dataKey="name" type="category" stroke="#64748b" tick={{ fontSize: 10 }} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#0b1329',
                    borderColor: '#1e3a8a',
                    borderRadius: '8px',
                    fontSize: '12px',
                    color: '#e2e8f0',
                  }}
                />
                <Legend wrapperStyle={{ fontSize: '11px', paddingTop: '8px' }} />
                <Bar dataKey="Bandwidth" fill="#8b5cf6" radius={[0, 4, 4, 0]} name="Bandwidth (MHz)" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-2">
            <Bell className="w-4 h-4 text-rose-400" />
            <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider">
              Recent Network Alerts & Incidents
            </h3>
          </div>
          <button
            onClick={() => onNavigate('alerts')}
            className="text-xs text-blue-400 hover:text-blue-300 font-semibold"
          >
            View All ({alerts.length}) →
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-navy-950 text-slate-400 border-b border-navy-800 uppercase font-semibold">
              <tr>
                <th className="py-2.5 px-3">ID</th>
                <th className="py-2.5 px-3">Cell ID</th>
                <th className="py-2.5 px-3">Type</th>
                <th className="py-2.5 px-3">Message</th>
                <th className="py-2.5 px-3">Severity</th>
                <th className="py-2.5 px-3">Status</th>
                <th className="py-2.5 px-3">Time</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-navy-800/60 text-slate-300">
              {alerts.slice(0, 5).map((alert: Alert) => (
                <tr key={alert.alertId} className="hover:bg-blue-950/20">
                  <td className="py-2.5 px-3 font-mono text-slate-400">#{alert.alertId}</td>
                  <td className="py-2.5 px-3 font-semibold text-slate-200">Cell-{alert.cellId}</td>
                  <td className="py-2.5 px-3">
                    <span className="px-2 py-0.5 rounded text-[10px] font-mono bg-navy-950 border border-navy-800">
                      {alert.alertType}
                    </span>
                  </td>
                  <td className="py-2.5 px-3 text-slate-200">{alert.alertMessage}</td>
                  <td className="py-2.5 px-3">
                    <span
                      className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                        alert.severity === 'CRITICAL'
                          ? 'bg-rose-950 text-rose-300 border-rose-800'
                          : alert.severity === 'WARNING'
                          ? 'bg-amber-950 text-amber-300 border-amber-800'
                          : 'bg-blue-950 text-blue-300 border-blue-800'
                      }`}
                    >
                      {alert.severity}
                    </span>
                  </td>
                  <td className="py-2.5 px-3">
                    <span
                      className={`inline-block w-2 h-2 rounded-full mr-1.5 ${
                        alert.status === 'OPEN'
                          ? 'bg-rose-400 animate-ping'
                          : alert.status === 'INVESTIGATING'
                          ? 'bg-amber-400'
                          : 'bg-emerald-400'
                      }`}
                    />
                    {alert.status}
                  </td>
                  <td className="py-2.5 px-3 font-mono text-slate-400">{alert.createdTime}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

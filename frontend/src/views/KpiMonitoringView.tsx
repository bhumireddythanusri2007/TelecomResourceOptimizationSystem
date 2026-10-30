import React from 'react';
import { useData } from '../context/DataContext';
import { NetworkKPI } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Gauge, Activity, Clock, AlertTriangle, Radio, Signal } from 'lucide-react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend
} from 'recharts';

export const KpiMonitoringView: React.FC = () => {
  const { data } = useData();
  const kpis = data.kpis;

  const avgThroughput =
    kpis.length > 0
      ? kpis.reduce((a: number, b: NetworkKPI) => a + (b.throughput || 0), 0) / kpis.length
      : 0;
  const avgLatency =
    kpis.length > 0
      ? kpis.reduce((a: number, b: NetworkKPI) => a + (b.latency || 0), 0) / kpis.length
      : 0;
  const avgPacketLoss =
    kpis.length > 0
      ? kpis.reduce((a: number, b: NetworkKPI) => a + (b.packetLoss || 0), 0) / kpis.length
      : 0;
  const avgAvailability =
    kpis.length > 0
      ? kpis.reduce((a: number, b: NetworkKPI) => a + (b.availability || 0), 0) / kpis.length
      : 0;

  const chartData = kpis.map((k: NetworkKPI) => ({
    name: `Cell-${k.cellId}`,
    'Throughput (Mbps)': k.throughput,
    'Latency (ms)': k.latency,
    'Packet Loss (%)': k.packetLoss,
  }));

  const columns: Column<NetworkKPI>[] = [
    {
      header: 'KPI ID',
      accessor: 'kpiId',
      className: 'font-mono text-slate-400 font-bold',
      render: k => `#${k.kpiId}`,
    },
    {
      header: 'Cell Station',
      accessor: 'cellId',
      render: k => (
        <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded font-mono text-xs bg-navy-950 border border-navy-800 text-blue-300 font-bold">
          <Radio className="w-3.5 h-3.5 text-blue-400" />
          Cell-{k.cellId}
        </span>
      ),
    },
    {
      header: 'Throughput',
      accessor: 'throughput',
      render: k => (
        <div className="flex items-center gap-1.5 font-mono text-xs font-bold text-emerald-300">
          <Activity className="w-3.5 h-3.5 text-emerald-400" />
          {k.throughput.toFixed(1)} Mbps
        </div>
      ),
    },
    {
      header: 'Round-Trip Latency',
      accessor: 'latency',
      render: k => (
        <div className="flex items-center gap-1.5 font-mono text-xs text-cyan-300">
          <Clock className="w-3.5 h-3.5 text-cyan-400" />
          {k.latency.toFixed(1)} ms
        </div>
      ),
    },
    {
      header: 'Packet Loss Rate',
      accessor: 'packetLoss',
      render: k => {
        const isLossHigh = k.packetLoss > 0.3;
        return (
          <span
            className={`font-mono text-xs font-semibold px-2 py-0.5 rounded border ${
              isLossHigh
                ? 'bg-rose-950/70 text-rose-300 border-rose-800/60'
                : 'bg-navy-950 text-slate-300 border-navy-800'
            }`}
          >
            {k.packetLoss.toFixed(2)}%
          </span>
        );
      },
    },
    {
      header: 'Carrier Availability',
      accessor: 'availability',
      render: k => (
        <span className="font-mono text-xs text-slate-100 font-bold">
          {k.availability.toFixed(2)}%
        </span>
      ),
    },
    {
      header: 'RF Signal Strength (RSRP)',
      accessor: 'signalStrength',
      render: k => (
        <span className="font-mono text-xs text-purple-300 flex items-center gap-1">
          <Signal className="w-3.5 h-3.5 text-purple-400" />
          {k.signalStrength.toFixed(1)} dBm
        </span>
      ),
    },
    {
      header: 'Recorded Time',
      accessor: 'recordedTime',
      render: k => <span className="font-mono text-xs text-slate-400">{k.recordedTime}</span>,
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Average Throughput"
          value={avgThroughput.toFixed(1)}
          unit="Mbps"
          subtext="Aggregate downstream speed"
          icon={Activity}
          variant="emerald"
        />
        <KpiCard
          title="Mean Radio Latency"
          value={avgLatency.toFixed(1)}
          unit="ms"
          subtext="End-to-end propagation delay"
          icon={Clock}
          variant="cyan"
        />
        <KpiCard
          title="Avg Packet Loss"
          value={`${avgPacketLoss.toFixed(2)}%`}
          subtext="Target SLA < 0.50%"
          icon={AlertTriangle}
          variant={avgPacketLoss > 0.3 ? 'rose' : 'blue'}
        />
        <KpiCard
          title="Network Availability"
          value={`${avgAvailability.toFixed(2)}%`}
          subtext="Carrier uptime grade"
          icon={Gauge}
          variant="purple"
        />
      </div>

      <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
        <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider mb-2">
          Throughput vs Latency Performance Profile
        </h3>
        <p className="text-xs text-slate-400 mb-4">
          Throughput (Mbps) and Latency (ms) measured across active cell base stations
        </p>
        <div className="h-56 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
              <XAxis dataKey="name" stroke="#64748b" tick={{ fontSize: 11 }} />
              <YAxis stroke="#64748b" tick={{ fontSize: 11 }} />
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
              <Line
                type="monotone"
                dataKey="Throughput (Mbps)"
                stroke="#10b981"
                strokeWidth={2}
                dot={{ r: 4 }}
              />
              <Line
                type="monotone"
                dataKey="Latency (ms)"
                stroke="#06b6d4"
                strokeWidth={2}
                dot={{ r: 4 }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      <DataTable
        title="Telecom Key Performance Indicators (KPI) Log"
        subtitle="Historical telemetry metrics recorded in the network_kpis database table"
        columns={columns}
        data={kpis}
        searchPlaceholder="Search by Cell ID, throughput, latency, timestamp..."
        searchFilter={(k, q) =>
          String(k.cellId).includes(q) ||
          String(k.kpiId).includes(q) ||
          String(k.throughput).includes(q) ||
          String(k.latency).includes(q)
        }
      />
    </div>
  );
};

import React from 'react';
import { useData } from '../context/DataContext';
import { NetworkSlice } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Network, Zap, CheckCircle2, Clock } from 'lucide-react';

export const NetworkSlicesView: React.FC = () => {
  const { data } = useData();
  const slices = data.slices;

  const totalSliceBw = slices.reduce((acc: number, s: NetworkSlice) => acc + (s.bandwidth || 0), 0);
  const totalSliceUsers = slices.reduce((acc: number, s: NetworkSlice) => acc + (s.connectedUsers || 0), 0);
  const minLatency = slices.length > 0 ? Math.min(...slices.map((s: NetworkSlice) => s.latencyRequirement || 999)) : 0;

  const columns: Column<NetworkSlice>[] = [
    {
      header: 'Slice ID',
      accessor: 'sliceId',
      className: 'font-mono text-slate-400 font-bold',
      render: s => `#${s.sliceId}`,
    },
    {
      header: 'Network Slice Name',
      accessor: 'sliceName',
      render: s => (
        <div className="flex items-center gap-2.5">
          <div className="p-1.5 rounded bg-purple-950 text-purple-400 border border-purple-900/50">
            <Network className="w-3.5 h-3.5" />
          </div>
          <div>
            <div className="font-bold text-slate-100">{s.sliceName}</div>
            <div className="text-[10px] text-slate-400 font-mono">Profile: 3GPP Rel-17</div>
          </div>
        </div>
      ),
    },
    {
      header: 'Slice Type / Service Class',
      accessor: 'sliceType',
      render: s => {
        const typeStyle = s.sliceType.toLowerCase().includes('ultra') || s.sliceType.toLowerCase().includes('urllc')
          ? 'bg-rose-950 text-rose-300 border-rose-800'
          : s.sliceType.toLowerCase().includes('broadband') || s.sliceType.toLowerCase().includes('embb')
          ? 'bg-blue-950 text-blue-300 border-blue-800'
          : 'bg-emerald-950 text-emerald-300 border-emerald-800';

        return (
          <span className={`px-2.5 py-1 rounded text-xs font-semibold border ${typeStyle}`}>
            {s.sliceType}
          </span>
        );
      },
    },
    {
      header: 'Reserved Bandwidth',
      accessor: 'bandwidth',
      render: s => (
        <span className="font-mono text-xs font-bold text-slate-100">
          {s.bandwidth} MHz
        </span>
      ),
    },
    {
      header: 'Latency SLA Target',
      accessor: 'latencyRequirement',
      render: s => (
        <span className="inline-flex items-center gap-1 font-mono text-xs font-semibold text-amber-300 bg-amber-950/40 px-2.5 py-0.5 rounded border border-amber-900/40">
          <Clock className="w-3 h-3 text-amber-400" />
          ≤ {s.latencyRequirement} ms
        </span>
      ),
    },
    {
      header: 'Connected Users / UEs',
      accessor: 'connectedUsers',
      render: s => (
        <span className="font-mono text-xs text-slate-200">
          {s.connectedUsers.toLocaleString()} UEs
        </span>
      ),
    },
    {
      header: 'State',
      accessor: 'status',
      render: s => (
        <span
          className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[11px] font-semibold border ${
            s.status.toLowerCase() === 'active'
              ? 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60'
              : 'bg-slate-900 text-slate-400 border-slate-700'
          }`}
        >
          <span
            className={`w-1.5 h-1.5 rounded-full ${
              s.status.toLowerCase() === 'active' ? 'bg-emerald-400 animate-pulse' : 'bg-slate-400'
            }`}
          />
          {s.status}
        </span>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Configured Slices"
          value={slices.length}
          unit="slices"
          subtext="3GPP end-to-end slice instances"
          icon={Network}
          variant="purple"
        />
        <KpiCard
          title="Total Slice Bandwidth"
          value={totalSliceBw}
          unit="MHz"
          subtext="Dedicated RF slice reservations"
          icon={Zap}
          variant="blue"
        />
        <KpiCard
          title="Active Slice Devices"
          value={totalSliceUsers.toLocaleString()}
          unit="UEs"
          subtext="Connected IoT & Mobile devices"
          icon={CheckCircle2}
          variant="emerald"
        />
        <KpiCard
          title="Ultra-Low Latency SLA"
          value={`≤ ${minLatency} ms`}
          subtext="Critical URLLC guaranteed floor"
          icon={Clock}
          variant="amber"
        />
      </div>

      <DataTable
        title="5G Network Slice Orchestration & SLA Profiles"
        subtitle="Manage virtualized network instances for eMBB, URLLC, and mMTC telecommunications"
        columns={columns}
        data={slices}
        searchPlaceholder="Search slice name, slice type, bandwidth, SLA..."
        statusFilterKey="status"
        searchFilter={(s, q) =>
          s.sliceName.toLowerCase().includes(q) ||
          s.sliceType.toLowerCase().includes(q) ||
          String(s.bandwidth).includes(q) ||
          String(s.latencyRequirement).includes(q)
        }
      />
    </div>
  );
};

import React from 'react';
import { useData } from '../context/DataContext';
import { TrafficFlow } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Activity, Radio, ArrowRight, Clock, Box } from 'lucide-react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend
} from 'recharts';

export const TrafficManagementView: React.FC = () => {
  const { data } = useData();
  const flows = data.trafficFlows;

  const totalVolume = flows.reduce((acc: number, f: TrafficFlow) => acc + (f.dataVolume || 0), 0);
  const totalPackets = flows.reduce((acc: number, f: TrafficFlow) => acc + (f.packetCount || 0), 0);
  const activeFlows = flows.filter((f: TrafficFlow) => f.flowStatus.toLowerCase() === 'active').length;

  const protocolData = [
    {
      protocol: 'TCP',
      Volume: flows.filter((f: TrafficFlow) => f.protocol === 'TCP').reduce((a: number, b: TrafficFlow) => a + b.dataVolume, 0),
    },
    {
      protocol: 'UDP',
      Volume: flows.filter((f: TrafficFlow) => f.protocol === 'UDP').reduce((a: number, b: TrafficFlow) => a + b.dataVolume, 0),
    },
    {
      protocol: 'SCTP',
      Volume: flows.filter((f: TrafficFlow) => f.protocol === 'SCTP').reduce((a: number, b: TrafficFlow) => a + b.dataVolume, 0),
    },
  ];

  const columns: Column<TrafficFlow>[] = [
    {
      header: 'Flow ID',
      accessor: 'flowId',
      className: 'font-mono text-slate-400 font-bold',
      render: f => `#${f.flowId}`,
    },
    {
      header: 'Cell Station',
      accessor: 'cellId',
      render: f => (
        <span className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded font-mono text-xs bg-navy-950 border border-navy-800 text-blue-300">
          <Radio className="w-3 h-3 text-blue-400" />
          Cell-{f.cellId}
        </span>
      ),
    },
    {
      header: 'Network Route (Source → Destination)',
      accessor: 'sourceIp',
      render: f => (
        <div className="flex items-center gap-2 font-mono text-xs text-slate-300">
          <span className="text-slate-200">{f.sourceIp}</span>
          <ArrowRight className="w-3.5 h-3.5 text-slate-500" />
          <span className="text-slate-200">{f.destinationIp}</span>
        </div>
      ),
    },
    {
      header: 'Protocol',
      accessor: 'protocol',
      render: f => {
        const pColor =
          f.protocol === 'TCP'
            ? 'bg-blue-950 text-blue-300 border-blue-800'
            : f.protocol === 'UDP'
            ? 'bg-emerald-950 text-emerald-300 border-emerald-800'
            : 'bg-purple-950 text-purple-300 border-purple-800';
        return (
          <span className={`px-2 py-0.5 rounded text-[11px] font-mono font-bold border ${pColor}`}>
            {f.protocol}
          </span>
        );
      },
    },
    {
      header: 'Data Volume',
      accessor: 'dataVolume',
      render: f => (
        <span className="font-mono text-xs font-semibold text-slate-100">
          {f.dataVolume.toLocaleString()} MB
        </span>
      ),
    },
    {
      header: 'Duration',
      accessor: 'flowDuration',
      render: f => (
        <span className="flex items-center gap-1 font-mono text-xs text-slate-400">
          <Clock className="w-3 h-3 text-slate-500" />
          {f.flowDuration} sec
        </span>
      ),
    },
    {
      header: 'Packets',
      accessor: 'packetCount',
      render: f => (
        <span className="font-mono text-xs text-slate-300">
          {f.packetCount.toLocaleString()} pkts
        </span>
      ),
    },
    {
      header: 'Flow State',
      accessor: 'flowStatus',
      render: f => (
        <span
          className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[11px] font-semibold border ${
            f.flowStatus.toLowerCase() === 'active'
              ? 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60'
              : 'bg-slate-900 text-slate-400 border-slate-700'
          }`}
        >
          <span
            className={`w-1.5 h-1.5 rounded-full ${
              f.flowStatus.toLowerCase() === 'active' ? 'bg-emerald-400 animate-pulse' : 'bg-slate-400'
            }`}
          />
          {f.flowStatus}
        </span>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Total Traffic Volume"
          value={(totalVolume / 1024).toFixed(2)}
          unit="GB"
          subtext="Cumulative payload transferred"
          icon={Activity}
          variant="emerald"
        />
        <KpiCard
          title="Active Sessions"
          value={activeFlows}
          unit="flows"
          subtext="Live network sockets"
          icon={Radio}
          variant="blue"
        />
        <KpiCard
          title="Total Packet Counter"
          value={(totalPackets / 1_000_000).toFixed(2)}
          unit="M pkts"
          subtext="L3/L4 packets routed"
          icon={Box}
          variant="purple"
        />
        <KpiCard
          title="Protocols Monitored"
          value="TCP / UDP / SCTP"
          subtext="Deep packet flow inspection"
          icon={Clock}
          variant="cyan"
        />
      </div>

      <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
        <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider mb-2">
          Payload Volume Distribution by Transport Protocol (MB)
        </h3>
        <p className="text-xs text-slate-400 mb-4">
          Breakdown of telemetry payload across TCP streaming, UDP low-latency, and SCTP signaling
        </p>
        <div className="h-44 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={protocolData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
              <XAxis dataKey="protocol" stroke="#64748b" tick={{ fontSize: 11 }} />
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
              <Bar dataKey="Volume" fill="#10b981" radius={[4, 4, 0, 0]} name="Data Volume (MB)" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <DataTable
        title="Network Traffic Flows & Flow Telemetry"
        subtitle="Granular connection records between cell base stations and client endpoints"
        columns={columns}
        data={flows}
        searchPlaceholder="Search by Flow ID, Cell, Source IP, Destination IP, Protocol..."
        statusFilterKey="flowStatus"
        searchFilter={(f, q) =>
          String(f.flowId).includes(q) ||
          String(f.cellId).includes(q) ||
          f.sourceIp.toLowerCase().includes(q) ||
          f.destinationIp.toLowerCase().includes(q) ||
          f.protocol.toLowerCase().includes(q)
        }
      />
    </div>
  );
};

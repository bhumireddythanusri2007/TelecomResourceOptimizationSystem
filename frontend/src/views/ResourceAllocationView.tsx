import React from 'react';
import { useData } from '../context/DataContext';
import { ResourceAllocation } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Layers, Radio, Cpu, Activity } from 'lucide-react';
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

export const ResourceAllocationView: React.FC = () => {
  const { data } = useData();
  const allocations = data.allocations;

  const totalBandwidth = allocations.reduce((acc: number, a: ResourceAllocation) => acc + (a.bandwidth || 0), 0);
  const totalAllocatedUsers = allocations.reduce((acc: number, a: ResourceAllocation) => acc + (a.allocatedUsers || 0), 0);
  const avgBwPerUser =
    totalAllocatedUsers > 0 ? (totalBandwidth / totalAllocatedUsers) * 1000 : 0; // kHz per user

  const chartData = allocations.map((a: ResourceAllocation) => ({
    name: `Cell-${a.cellId}`,
    Bandwidth: a.bandwidth,
    Frequency: (a.frequency / 1000).toFixed(1),
    Users: a.allocatedUsers,
  }));

  const columns: Column<ResourceAllocation>[] = [
    {
      header: 'Alloc ID',
      accessor: 'allocationId',
      className: 'font-mono text-slate-400 font-bold',
      render: a => `#${a.allocationId}`,
    },
    {
      header: 'Target Cell Station',
      accessor: 'cellId',
      render: a => (
        <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded font-mono text-xs bg-navy-950 border border-navy-800 text-blue-300 font-bold">
          <Radio className="w-3.5 h-3.5 text-blue-400" />
          Cell-{a.cellId}
        </span>
      ),
    },
    {
      header: 'Allocated Bandwidth',
      accessor: 'bandwidth',
      render: a => (
        <div className="flex items-center gap-2">
          <div className="w-2.5 h-2.5 rounded-full bg-blue-400" />
          <span className="font-mono text-xs font-bold text-slate-100">
            {a.bandwidth} MHz
          </span>
        </div>
      ),
    },
    {
      header: 'Operating Frequency Band',
      accessor: 'frequency',
      render: a => {
        const ghz = (a.frequency / 1000).toFixed(2);
        const bandLabel =
          a.frequency >= 3300
            ? '5G C-Band (n78)'
            : a.frequency >= 2100
            ? 'Mid-Band (n1/n3)'
            : 'Sub-2GHz Coverage';

        return (
          <div>
            <div className="font-mono text-xs font-semibold text-cyan-300">
              {a.frequency} MHz ({ghz} GHz)
            </div>
            <div className="text-[10px] text-slate-400 font-medium">{bandLabel}</div>
          </div>
        );
      },
    },
    {
      header: 'Active Allocated Subscribers',
      accessor: 'allocatedUsers',
      render: a => (
        <span className="font-mono text-xs text-slate-200">
          {a.allocatedUsers.toLocaleString()} users
        </span>
      ),
    },
    {
      header: 'Spectral Density',
      render: a => {
        const density = (a.bandwidth / (a.allocatedUsers || 1)).toFixed(3);
        return (
          <span className="font-mono text-xs text-slate-300 bg-navy-950 px-2 py-0.5 rounded border border-navy-800">
            {density} MHz/user
          </span>
        );
      },
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Total RF Bandwidth"
          value={totalBandwidth}
          unit="MHz"
          subtext="Total frequency spectrum allocated"
          icon={Layers}
          variant="blue"
        />
        <KpiCard
          title="Allocated Cell Nodes"
          value={allocations.length}
          unit="stations"
          subtext="Configured spectrum channels"
          icon={Radio}
          variant="cyan"
        />
        <KpiCard
          title="Total Channel Users"
          value={totalAllocatedUsers.toLocaleString()}
          unit="users"
          subtext="Subscribers connected to spectrum"
          icon={Activity}
          variant="emerald"
        />
        <KpiCard
          title="Avg Spectral Share"
          value={avgBwPerUser.toFixed(1)}
          unit="kHz / user"
          subtext="Theoretical carrier share"
          icon={Cpu}
          variant="purple"
        />
      </div>

      <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
        <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider mb-2">
          RF Bandwidth Spectrum per Base Station (MHz)
        </h3>
        <p className="text-xs text-slate-400 mb-4">
          Visual comparison of allocated carrier channel width across operating cell sites
        </p>
        <div className="h-48 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData}>
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
              <Bar dataKey="Bandwidth" fill="#3b82f6" radius={[4, 4, 0, 0]} name="Bandwidth (MHz)" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <DataTable
        title="Radio Frequency & Carrier Spectrum Allocations"
        subtitle="Manage carrier frequency assignments, channel widths, and user allocations"
        columns={columns}
        data={allocations}
        searchPlaceholder="Search by Allocation ID, Cell ID, Bandwidth, Frequency..."
        searchFilter={(a, q) =>
          String(a.allocationId).includes(q) ||
          String(a.cellId).includes(q) ||
          String(a.bandwidth).includes(q) ||
          String(a.frequency).includes(q)
        }
      />
    </div>
  );
};

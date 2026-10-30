import React from 'react';
import { useData } from '../context/DataContext';
import { EdgeServer } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Server, Cpu, HardDrive, CheckCircle2 } from 'lucide-react';

export const EdgeServersView: React.FC = () => {
  const { data } = useData();
  const servers = data.edgeServers;

  const totalCpu = servers.reduce((acc: number, s: EdgeServer) => acc + (s.cpuCapacity || 0), 0);
  const totalMemory = servers.reduce((acc: number, s: EdgeServer) => acc + (s.memoryCapacity || 0), 0);
  const totalStorage = servers.reduce((acc: number, s: EdgeServer) => acc + (s.storageCapacity || 0), 0);
  const totalUsers = servers.reduce((acc: number, s: EdgeServer) => acc + (s.connectedUsers || 0), 0);

  const columns: Column<EdgeServer>[] = [
    {
      header: 'Node ID',
      accessor: 'edgeServerId',
      className: 'font-mono text-slate-400 font-bold',
      render: s => `#${s.edgeServerId}`,
    },
    {
      header: 'Edge Server Node',
      accessor: 'serverName',
      render: s => (
        <div className="flex items-center gap-2.5">
          <div className="p-1.5 rounded bg-blue-950 text-blue-400 border border-blue-900/50">
            <Server className="w-3.5 h-3.5" />
          </div>
          <div>
            <div className="font-bold text-slate-100">{s.serverName}</div>
            <div className="text-[10px] text-slate-400 font-mono">Location: {s.location}</div>
          </div>
        </div>
      ),
    },
    {
      header: 'CPU Cores',
      accessor: 'cpuCapacity',
      render: s => (
        <div className="flex items-center gap-1.5 font-mono text-xs text-cyan-300">
          <Cpu className="w-3.5 h-3.5 text-cyan-400" />
          {s.cpuCapacity} vCPUs
        </div>
      ),
    },
    {
      header: 'RAM Memory',
      accessor: 'memoryCapacity',
      render: s => (
        <span className="font-mono text-xs text-purple-300">
          {s.memoryCapacity} GB RAM
        </span>
      ),
    },
    {
      header: 'NVMe Storage',
      accessor: 'storageCapacity',
      render: s => (
        <span className="font-mono text-xs text-slate-300 flex items-center gap-1">
          <HardDrive className="w-3.5 h-3.5 text-slate-400" />
          {(s.storageCapacity / 1000).toFixed(1)} TB
        </span>
      ),
    },
    {
      header: 'Offloaded Workloads',
      accessor: 'connectedUsers',
      render: s => (
        <span className="font-mono text-xs text-slate-200">
          {s.connectedUsers.toLocaleString()} sessions
        </span>
      ),
    },
    {
      header: 'Status',
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
          title="Edge MEC Nodes"
          value={servers.length}
          unit="servers"
          subtext="Distributed Multi-access Edge"
          icon={Server}
          variant="blue"
        />
        <KpiCard
          title="Compute Capacity"
          value={totalCpu}
          unit="vCPUs"
          subtext="Total distributed cores"
          icon={Cpu}
          variant="cyan"
        />
        <KpiCard
          title="Memory Pool"
          value={`${(totalMemory / 1024).toFixed(1)} TB`}
          subtext={`${totalMemory} GB physical RAM`}
          icon={CheckCircle2}
          variant="purple"
        />
        <KpiCard
          title="NVMe Storage Pool"
          value={`${(totalStorage / 1000).toFixed(1)} TB`}
          subtext={`${totalUsers.toLocaleString()} offloaded user sessions`}
          icon={HardDrive}
          variant="emerald"
        />
      </div>

      <DataTable
        title="Multi-Access Edge Computing (MEC) Infrastructure"
        subtitle="Manage edge servers, low-latency offloading, and distributed compute pools"
        columns={columns}
        data={servers}
        searchPlaceholder="Search edge server name, location, capacity..."
        statusFilterKey="status"
        searchFilter={(s, q) =>
          s.serverName.toLowerCase().includes(q) ||
          s.location.toLowerCase().includes(q) ||
          String(s.edgeServerId).includes(q)
        }
      />
    </div>
  );
};

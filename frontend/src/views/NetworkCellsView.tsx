import React from 'react';
import { useData } from '../context/DataContext';
import { NetworkCell } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Radio, CheckCircle2, AlertOctagon, Wrench } from 'lucide-react';

export const NetworkCellsView: React.FC = () => {
  const { data } = useData();
  const cells = data.cells;

  const activeCells = cells.filter((c: NetworkCell) => c.status === 'Active').length;
  const maintenanceCells = cells.filter((c: NetworkCell) => c.status === 'Maintenance').length;
  const totalCapacity = cells.reduce((acc: number, c: NetworkCell) => acc + (c.totalCapacity || 0), 0);
  const totalUsers = cells.reduce((acc: number, c: NetworkCell) => acc + (c.currentUsers || 0), 0);
  const avgUtilization = totalCapacity > 0 ? Math.round((totalUsers / totalCapacity) * 100) : 0;

  const columns: Column<NetworkCell>[] = [
    {
      header: 'Cell ID',
      accessor: 'cellId',
      className: 'font-mono text-slate-400 font-bold',
      render: c => `#${c.cellId}`,
    },
    {
      header: 'Cell Station Name',
      accessor: 'cellName',
      className: 'font-semibold text-slate-100',
      render: c => (
        <div className="flex items-center gap-2">
          <div className="p-1.5 rounded bg-blue-950 text-blue-400 border border-blue-900/50">
            <Radio className="w-3.5 h-3.5" />
          </div>
          <div>
            <div className="font-bold text-slate-100">{c.cellName}</div>
            <div className="text-[10px] text-slate-400 font-mono">BTS-ID: {c.cellId}</div>
          </div>
        </div>
      ),
    },
    {
      header: 'Location / Sector',
      accessor: 'location',
      render: c => <span className="text-slate-300">{c.location || 'Unassigned'}</span>,
    },
    {
      header: 'Total Capacity',
      accessor: 'totalCapacity',
      render: c => (
        <span className="font-mono text-slate-200">
          {c.totalCapacity.toLocaleString()} users
        </span>
      ),
    },
    {
      header: 'Current Load',
      accessor: 'currentUsers',
      render: c => {
        const util = Math.round((c.currentUsers / (c.totalCapacity || 1)) * 100);
        const barColor =
          util >= 85 ? 'bg-rose-500' : util >= 70 ? 'bg-amber-500' : 'bg-emerald-500';

        return (
          <div className="w-36">
            <div className="flex justify-between text-[11px] mb-1 font-mono">
              <span className="text-slate-200">{c.currentUsers.toLocaleString()}</span>
              <span className={util >= 85 ? 'text-rose-400 font-bold' : 'text-slate-400'}>
                {util}%
              </span>
            </div>
            <div className="w-full bg-navy-950 h-1.5 rounded-full overflow-hidden border border-navy-800">
              <div
                className={`h-full ${barColor} rounded-full transition-all duration-300`}
                style={{ width: `${Math.min(util, 100)}%` }}
              />
            </div>
          </div>
        );
      },
    },
    {
      header: 'Operational Status',
      accessor: 'status',
      render: c => {
        const statusMap: Record<string, { bg: string; dot: string; icon: React.ElementType }> = {
          Active: {
            bg: 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60',
            dot: 'bg-emerald-400',
            icon: CheckCircle2,
          },
          Maintenance: {
            bg: 'bg-amber-950/80 text-amber-300 border-amber-800/60',
            dot: 'bg-amber-400',
            icon: Wrench,
          },
          Inactive: {
            bg: 'bg-slate-900/80 text-slate-400 border-slate-700/60',
            dot: 'bg-slate-500',
            icon: AlertOctagon,
          },
        };

        const config = statusMap[c.status] || {
          bg: 'bg-blue-950 text-blue-300 border-blue-800',
          dot: 'bg-blue-400',
          icon: CheckCircle2,
        };

        const Icon = config.icon;

        return (
          <span
            className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[11px] font-semibold border ${config.bg}`}
          >
            <span className={`w-1.5 h-1.5 rounded-full ${config.dot}`} />
            <Icon className="w-3 h-3" />
            {c.status}
          </span>
        );
      },
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Total Base Stations"
          value={cells.length}
          unit="cells"
          subtext="Configured macro & micro cells"
          icon={Radio}
          variant="blue"
        />
        <KpiCard
          title="Active Online"
          value={activeCells}
          unit="stations"
          subtext={`${Math.round((activeCells / (cells.length || 1)) * 100)}% operational availability`}
          icon={CheckCircle2}
          variant="emerald"
        />
        <KpiCard
          title="Scheduled Maintenance"
          value={maintenanceCells}
          unit="cells"
          subtext="Under scheduled calibration"
          icon={Wrench}
          variant="amber"
        />
        <KpiCard
          title="Network Load Ratio"
          value={`${avgUtilization}%`}
          subtext={`${totalUsers.toLocaleString()} / ${totalCapacity.toLocaleString()} capacity`}
          icon={Radio}
          variant={avgUtilization > 80 ? 'rose' : 'cyan'}
        />
      </div>

      <DataTable
        title="Registered Network Cell Base Stations"
        subtitle="Live telemetry and user capacity metrics for active cellular base stations"
        columns={columns}
        data={cells}
        searchPlaceholder="Filter by cell name, location, ID..."
        statusFilterKey="status"
        searchFilter={(c, q) =>
          c.cellName.toLowerCase().includes(q) ||
          c.location.toLowerCase().includes(q) ||
          String(c.cellId).includes(q)
        }
      />
    </div>
  );
};

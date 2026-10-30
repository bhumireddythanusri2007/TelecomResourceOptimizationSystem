import React from 'react';
import { useData } from '../context/DataContext';
import { Alert } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Bell, AlertTriangle, ShieldCheck, Radio, Info } from 'lucide-react';

export const AlertsView: React.FC = () => {
  const { data } = useData();
  const alerts = data.alerts;

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
  const openCount = alerts.filter(a => (a.status || '').toUpperCase() === 'OPEN').length;
  const resolvedCount = alerts.filter(a => (a.status || '').toUpperCase() === 'RESOLVED').length;

  const columns: Column<Alert>[] = [
    {
      header: 'Alert ID',
      accessor: 'alertId',
      className: 'font-mono text-slate-400 font-bold',
      render: a => `#${a.alertId}`,
    },
    {
      header: 'Cell Station',
      accessor: 'cellId',
      render: a => (
        <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded font-mono text-xs bg-navy-950 border border-navy-800 text-blue-300 font-bold">
          <Radio className="w-3.5 h-3.5 text-blue-400" />
          Cell-{a.cellId}
        </span>
      ),
    },
    {
      header: 'Category / Incident Type',
      accessor: 'alertType',
      render: a => (
        <span className="px-2.5 py-1 rounded text-xs font-mono font-semibold bg-navy-950 border border-navy-800 text-slate-200">
          {a.alertType}
        </span>
      ),
    },
    {
      header: 'Diagnostic Alert Message',
      accessor: 'alertMessage',
      className: 'text-slate-200 font-medium',
      render: a => a.alertMessage,
    },
    {
      header: 'Severity',
      accessor: 'severity',
      render: a => {
        const s = (a.severity || '').toUpperCase().trim();
        const severityStyle =
          s === 'CRITICAL' || s === 'HIGH'
            ? 'bg-rose-950 text-rose-300 border-rose-800'
            : s === 'WARNING' || s === 'MEDIUM'
            ? 'bg-amber-950 text-amber-300 border-amber-800'
            : 'bg-blue-950 text-blue-300 border-blue-800';

        return (
          <span className={`px-2.5 py-0.5 rounded text-[11px] font-bold border ${severityStyle}`}>
            {a.severity}
          </span>
        );
      },
    },
    {
      header: 'Status',
      accessor: 'status',
      render: a => (
        <span
          className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[11px] font-semibold border ${
            a.status === 'OPEN'
              ? 'bg-rose-950/80 text-rose-300 border-rose-800/60'
              : a.status === 'INVESTIGATING'
              ? 'bg-amber-950/80 text-amber-300 border-amber-800/60'
              : 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60'
          }`}
        >
          <span
            className={`w-1.5 h-1.5 rounded-full ${
              a.status === 'OPEN'
                ? 'bg-rose-400 animate-ping'
                : a.status === 'INVESTIGATING'
                ? 'bg-amber-400'
                : 'bg-emerald-400'
            }`}
          />
          {a.status}
        </span>
      ),
    },
    {
      header: 'Logged Time',
      accessor: 'createdTime',
      render: a => <span className="font-mono text-xs text-slate-400">{a.createdTime}</span>,
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Total Alerts Logged"
          value={alerts.length}
          unit="events"
          subtext={`${warningCount} warning • ${infoCount} low/info`}
          icon={Bell}
          variant="blue"
        />
        <KpiCard
          title="Active Critical Alerts"
          value={criticalCount}
          unit="critical"
          subtext="Requires immediate NOC attention"
          icon={AlertTriangle}
          variant="rose"
        />
        <KpiCard
          title="Unresolved Incidents"
          value={openCount}
          unit="open"
          subtext="Pending operator triage"
          icon={Info}
          variant="amber"
        />
        <KpiCard
          title="Resolved Incidents"
          value={resolvedCount}
          unit="events"
          subtext="Restored to operational SLA"
          icon={ShieldCheck}
          variant="emerald"
        />
      </div>

      <DataTable
        title="Network Operations Alerts & Threshold Triggers"
        subtitle="Real-time incident log from the alerts database table"
        columns={columns}
        data={alerts}
        searchPlaceholder="Search alerts by message, type, Cell ID, severity..."
        statusFilterKey="severity"
        searchFilter={(a, q) =>
          a.alertMessage.toLowerCase().includes(q) ||
          a.alertType.toLowerCase().includes(q) ||
          String(a.cellId).includes(q) ||
          a.severity.toLowerCase().includes(q)
        }
      />
    </div>
  );
};

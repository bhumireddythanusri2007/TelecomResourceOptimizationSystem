import React from 'react';
import { useData } from '../context/DataContext';
import { KpiCard } from '../components/common/KpiCard';
import { FileText, Printer, CheckCircle2, Activity, Clock, ShieldCheck, Database } from 'lucide-react';
import { NetworkCell } from '../types';

export const ReportsView: React.FC = () => {
  const { data, mode, lastSyncTime } = useData();
  const { report, cells, alerts, stats } = data;

  const avgTraffic = report?.averageTrafficLoad != null ? Number(report.averageTrafficLoad).toFixed(2) : '0.00';
  const avgLatency = report?.averageLatency != null ? Number(report.averageLatency).toFixed(2) : '0.00';
  const avgAvailability = report?.averageAvailability != null ? Number(report.averageAvailability).toFixed(2) : '0.00';

  const handlePrint = () => {
    window.print();
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-navy-900/60 border border-blue-900/30 p-5 rounded-2xl backdrop-blur-md">
        <div>
          <h2 className="text-xl font-bold text-white tracking-tight flex items-center gap-2">
            <FileText className="w-5 h-5 text-blue-400" />
            Executive Telecom Network Performance Summary
          </h2>
          <p className="text-xs text-slate-400 mt-1">
            Aggregated system report compiled from database records and performance monitoring tables.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={handlePrint}
            className="inline-flex items-center gap-2 px-3.5 py-2 rounded-xl bg-navy-800 hover:bg-navy-700 border border-navy-700 text-slate-200 text-xs font-semibold transition"
          >
            <Printer className="w-4 h-4" />
            Print / Export PDF
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <KpiCard
          title="Average Traffic Load"
          value={`${avgTraffic}%`}
          subtext="Calculated from network_traffic load metric"
          icon={Activity}
          variant="cyan"
        />
        <KpiCard
          title="Average Radio Latency"
          value={`${avgLatency} ms`}
          subtext="Average latency recorded in network_kpis"
          icon={Clock}
          variant="blue"
        />
        <KpiCard
          title="System Availability"
          value={`${avgAvailability}%`}
          subtext="Calculated availability from KPI telemetry"
          icon={ShieldCheck}
          variant="emerald"
        />
      </div>

      <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-6 shadow-xl backdrop-blur-md space-y-6">
        <div className="border-b border-navy-800 pb-4 flex flex-col sm:flex-row sm:items-center justify-between gap-2">
          <div>
            <span className="text-[10px] font-bold uppercase tracking-wider text-blue-400 font-mono">
              AUDIT REPORT REF: TRM-2026-Q4
            </span>
            <h3 className="text-lg font-bold text-slate-100 mt-1">
              Network Infrastructure & Optimization Telemetry Ledger
            </h3>
          </div>
          <div className="text-xs text-slate-400 font-mono">
            Data Source: <strong className="text-slate-200">{mode}</strong> • Synced:{' '}
            {lastSyncTime || 'N/A'}
          </div>
        </div>

        <div>
          <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider mb-3 flex items-center gap-2">
            <Database className="w-4 h-4 text-blue-400" />
            Enterprise Table Aggregates
          </h4>
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
            <div className="p-3.5 rounded-xl bg-navy-950/80 border border-navy-800">
              <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                Network Cells
              </span>
              <span className="text-xl font-bold text-blue-300 mt-1 block">
                {stats.networkCells}
              </span>
            </div>
            <div className="p-3.5 rounded-xl bg-navy-950/80 border border-navy-800">
              <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                Traffic Records
              </span>
              <span className="text-xl font-bold text-emerald-300 mt-1 block">
                {stats.trafficRecords}
              </span>
            </div>
            <div className="p-3.5 rounded-xl bg-navy-950/80 border border-navy-800">
              <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                Resource Allocations
              </span>
              <span className="text-xl font-bold text-purple-300 mt-1 block">
                {report.resourceAllocations}
              </span>
            </div>
            <div className="p-3.5 rounded-xl bg-navy-950/80 border border-navy-800">
              <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                Optimization Results
              </span>
              <span className="text-xl font-bold text-amber-300 mt-1 block">
                {stats.optimizationResults}
              </span>
            </div>
            <div className="p-3.5 rounded-xl bg-navy-950/80 border border-navy-800">
              <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                KPI Rows
              </span>
              <span className="text-xl font-bold text-cyan-300 mt-1 block">
                {stats.kpiRecords}
              </span>
            </div>
            <div className="p-3.5 rounded-xl bg-navy-950/80 border border-navy-800">
              <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                Total Alerts
              </span>
              <span className="text-xl font-bold text-rose-300 mt-1 block">
                {stats.alerts}
              </span>
            </div>
          </div>
        </div>

        <div className="border-t border-navy-800 pt-5">
          <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider mb-3 flex items-center gap-2">
            <CheckCircle2 className="w-4 h-4 text-emerald-400" />
            Operational Health & Incident Resolution
          </h4>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="p-4 rounded-xl bg-navy-950/60 border border-navy-800 space-y-3">
              <div className="flex justify-between items-center text-xs">
                <span className="text-slate-400">Alert Resolution Rate</span>
                <span className="font-bold text-emerald-300 font-mono">
                  {alerts.length > 0
                    ? Math.round((report.resolvedAlerts / alerts.length) * 100)
                    : 100}
                  %
                </span>
              </div>
              <div className="w-full bg-navy-900 h-2 rounded-full overflow-hidden">
                <div
                  className="bg-emerald-500 h-full rounded-full"
                  style={{
                    width: `${
                      alerts.length > 0
                        ? (report.resolvedAlerts / alerts.length) * 100
                        : 100
                    }%`,
                  }}
                />
              </div>
              <div className="flex justify-between text-[11px] text-slate-400 pt-1">
                <span>Resolved: {report.resolvedAlerts}</span>
                <span>Open Incidents: {report.openAlerts}</span>
              </div>
            </div>

            <div className="p-4 rounded-xl bg-navy-950/60 border border-navy-800 space-y-3">
              <div className="flex justify-between items-center text-xs">
                <span className="text-slate-400">Carrier Station Readiness</span>
                <span className="font-bold text-blue-300 font-mono">
                  {cells.length > 0
                    ? Math.round(
                        (cells.filter((c: NetworkCell) => c.status === 'Active').length / cells.length) * 100
                      )
                    : 100}
                  %
                </span>
              </div>
              <div className="w-full bg-navy-900 h-2 rounded-full overflow-hidden">
                <div
                  className="bg-blue-500 h-full rounded-full"
                  style={{
                    width: `${
                      cells.length > 0
                        ? (cells.filter((c: NetworkCell) => c.status === 'Active').length / cells.length) * 100
                        : 100
                    }%`,
                  }}
                />
              </div>
              <div className="flex justify-between text-[11px] text-slate-400 pt-1">
                <span>Active: {cells.filter((c: NetworkCell) => c.status === 'Active').length}</span>
                <span>Maintenance: {cells.filter((c: NetworkCell) => c.status === 'Maintenance').length}</span>
              </div>
            </div>
          </div>
        </div>

        <div className="border-t border-navy-800 pt-5 text-xs text-slate-400 space-y-2">
          <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider mb-2">
            Academic Project Architecture Attestation
          </h4>
          <p>
            This enterprise dashboard interfaces directly with the Jakarta EE 10 Servlet application deployed on embedded Jetty 12. All data-access calls map directly to existing backend servlets:
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/dashboard</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/network-cells</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/devices</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/subscribers</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/traffic-flows</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/resource-allocation</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/network-slices</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/edge-servers</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/optimization</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/kpi-monitoring</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/users</code>,
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/alerts</code>, and
            <code className="text-blue-300 font-mono bg-navy-950 px-1.5 py-0.5 rounded mx-1">/reports</code>.
          </p>
        </div>
      </div>
    </div>
  );
};

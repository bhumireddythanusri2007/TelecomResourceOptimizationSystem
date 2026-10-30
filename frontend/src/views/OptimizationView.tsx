import React, { useState } from 'react';
import { useData } from '../context/DataContext';
import { OptimizationResult, OptimizationJob } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Zap, Play, CheckCircle2, TrendingDown, Radio, AlertCircle } from 'lucide-react';
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

export const OptimizationView: React.FC = () => {
  const { data, runOptimization } = useData();
  const { results, jobs } = data;
  const [running, setRunning] = useState(false);
  const [notification, setNotification] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const handleRunOptimization = async () => {
    setRunning(true);
    setNotification(null);
    try {
      const res = await runOptimization();
      if (res.success) {
        setNotification({ type: 'success', text: res.message });
      } else {
        setNotification({ type: 'error', text: res.message });
      }
    } catch (e: any) {
      setNotification({ type: 'error', text: e.message || 'Execution failed' });
    } finally {
      setRunning(false);
    }
  };

  const comparisonData = results.slice(0, 6).map((r: OptimizationResult) => ({
    name: `Cell-${r.cellId}`,
    'Previous Load (%)': r.previousLoad,
    'Optimized Load (%)': r.optimizedLoad,
    'Bandwidth (MHz)': r.allocatedBandwidth,
  }));

  const avgPrevLoad =
    results.length > 0
      ? results.reduce((a: number, b: OptimizationResult) => a + (b.previousLoad || 0), 0) / results.length
      : 0;
  const avgOptLoad =
    results.length > 0
      ? results.reduce((a: number, b: OptimizationResult) => a + (b.optimizedLoad || 0), 0) / results.length
      : 0;
  const avgScore =
    results.length > 0
      ? results.reduce((a: number, b: OptimizationResult) => a + (b.performanceScore || 0), 0) / results.length
      : 0;
  const loadReduction = (avgPrevLoad - avgOptLoad).toFixed(1);

  const resultColumns: Column<OptimizationResult>[] = [
    {
      header: 'Result ID',
      accessor: 'resultId',
      className: 'font-mono text-slate-400 font-bold',
      render: r => `#${r.resultId}`,
    },
    {
      header: 'Cell Station',
      accessor: 'cellId',
      render: r => (
        <span className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded font-mono text-xs bg-navy-950 border border-navy-800 text-blue-300 font-bold">
          <Radio className="w-3.5 h-3.5 text-blue-400" />
          Cell-{r.cellId}
        </span>
      ),
    },
    {
      header: 'Previous Load (%)',
      accessor: 'previousLoad',
      render: r => (
        <span className="font-mono text-xs font-semibold text-rose-300">
          {r.previousLoad.toFixed(1)}%
        </span>
      ),
    },
    {
      header: 'Simulated Load (%)',
      accessor: 'optimizedLoad',
      render: r => (
        <span className="font-mono text-xs font-semibold text-emerald-300">
          {r.optimizedLoad.toFixed(1)}%
        </span>
      ),
    },
    {
      header: 'Reallocated Bandwidth',
      accessor: 'allocatedBandwidth',
      render: r => (
        <span className="font-mono text-xs text-blue-200">
          {r.allocatedBandwidth} MHz
        </span>
      ),
    },
    {
      header: 'Illustrative Performance Score',
      accessor: 'performanceScore',
      render: r => (
        <div className="flex items-center gap-2">
          <div className="w-16 bg-navy-950 h-1.5 rounded-full overflow-hidden border border-navy-800">
            <div
              className="h-full bg-emerald-400 rounded-full"
              style={{ width: `${Math.min(r.performanceScore, 100)}%` }}
            />
          </div>
          <span className="font-mono text-xs font-bold text-emerald-400">
            {r.performanceScore.toFixed(1)}
          </span>
        </div>
      ),
    },
    {
      header: 'Timestamp',
      accessor: 'optimizationTime',
      render: r => (
        <span className="font-mono text-xs text-slate-400">
          {r.optimizationTime}
        </span>
      ),
    },
  ];

  const jobColumns: Column<OptimizationJob>[] = [
    {
      header: 'Job ID',
      accessor: 'jobId',
      className: 'font-mono text-slate-400 font-bold',
      render: j => `#${j.jobId}`,
    },
    {
      header: 'Job Name',
      accessor: 'jobName',
      render: j => <span className="font-semibold text-slate-100">{j.jobName}</span>,
    },
    {
      header: 'Algorithm Type',
      accessor: 'optimizationType',
      render: j => (
        <span className="px-2 py-0.5 rounded text-[11px] font-mono bg-navy-950 border border-navy-800 text-cyan-300">
          {j.optimizationType}
        </span>
      ),
    },
    {
      header: 'Target Cell',
      accessor: 'targetCellId',
      render: j => (
        <span className="font-mono text-xs text-slate-300">
          {j.targetCellId ? `Cell-${j.targetCellId}` : 'All Active Cells'}
        </span>
      ),
    },
    {
      header: 'Input Load',
      accessor: 'inputLoad',
      render: j => (
        <span className="font-mono text-xs text-slate-200">
          {j.inputLoad ? `${j.inputLoad.toFixed(1)}%` : 'Dynamic'}
        </span>
      ),
    },
    {
      header: 'Execution Status',
      accessor: 'status',
      render: j => (
        <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[11px] font-semibold bg-emerald-950/80 text-emerald-300 border border-emerald-800/60">
          <CheckCircle2 className="w-3 h-3 text-emerald-400" />
          {j.status}
        </span>
      ),
    },
    {
      header: 'Created Time',
      accessor: 'createdTime',
      render: j => (
        <span className="font-mono text-xs text-slate-400">{j.createdTime}</span>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="bg-navy-900/90 border border-blue-900/40 p-6 rounded-2xl shadow-xl backdrop-blur-md">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="max-w-3xl">
            <div className="flex items-center gap-2">
              <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider bg-blue-950 text-blue-300 border border-blue-800">
                Rule-Based Simulation Engine
              </span>
              <span className="text-xs text-slate-400 font-mono">
                Service: ResourceOptimizationService.java
              </span>
            </div>
            <h2 className="text-xl font-bold text-white tracking-tight mt-1.5">
              Network Cell Resource Rebalance & Load Optimization
            </h2>
            <p className="text-xs text-slate-300 mt-1 leading-relaxed">
              Executes rule-based heuristics to evaluate active cellular stations against congestion thresholds (load ≥ 70% and ≥ 80%). The algorithm reallocates carrier bandwidth (+10 to +20 MHz) and estimates a relative reduction in operating load.
            </p>
          </div>

          <button
            onClick={handleRunOptimization}
            disabled={running}
            className="inline-flex items-center justify-center gap-2 px-5 py-3 rounded-xl bg-blue-600 hover:bg-blue-500 disabled:bg-blue-800 disabled:opacity-60 text-white font-bold text-xs uppercase tracking-wider shadow-[0_0_20px_rgba(37,99,235,0.4)] transition shrink-0"
          >
            <Play className={`w-4 h-4 fill-current ${running ? 'animate-spin' : ''}`} />
            {running ? 'Simulating Engine...' : 'Run Optimization Job'}
          </button>
        </div>

        <div className="mt-4 p-3 rounded-xl bg-navy-950/80 border border-navy-800 text-xs text-slate-400 flex items-start gap-2.5">
          <AlertCircle className="w-4 h-4 text-cyan-400 shrink-0 mt-0.5" />
          <div>
            <strong className="text-slate-300 font-semibold">Honest Representation Notice:</strong>{' '}
            This system utilizes an algorithmic rule-based load-balancing heuristic as implemented in the Java backend. It does not employ quantum computing, QUBO solvers, or real-world cellular transmission hardware control. Performance scores represent illustrative indicators.
          </div>
        </div>

        {notification && (
          <div
            className={`mt-4 p-3 rounded-xl text-xs font-medium flex items-center gap-2 ${
              notification.type === 'success'
                ? 'bg-emerald-950/80 border border-emerald-800/60 text-emerald-200'
                : 'bg-rose-950/80 border border-rose-800/60 text-rose-200'
            }`}
          >
            <CheckCircle2 className="w-4 h-4" />
            {notification.text}
          </div>
        )}
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Optimization Runs"
          value={results.length}
          unit="cycles"
          subtext="Recorded optimization passes"
          icon={Zap}
          variant="amber"
        />
        <KpiCard
          title="Simulated Load Reduction"
          value={`-${loadReduction}%`}
          subtext="Average relative relief across cells"
          icon={TrendingDown}
          variant="emerald"
        />
        <KpiCard
          title="Avg Optimized Load"
          value={`${avgOptLoad.toFixed(1)}%`}
          subtext={`Down from ${avgPrevLoad.toFixed(1)}% baseline`}
          icon={Radio}
          variant="cyan"
        />
        <KpiCard
          title="Avg Performance Score"
          value={avgScore.toFixed(1)}
          unit="/ 100"
          subtext="Illustrative heuristic score"
          icon={CheckCircle2}
          variant="blue"
        />
      </div>

      <div className="bg-navy-900/80 border border-blue-900/30 rounded-2xl p-5 shadow-lg backdrop-blur-md">
        <h3 className="text-sm font-bold text-slate-100 uppercase tracking-wider mb-1">
          Cell Operating Load: Baseline vs Simulated Optimized (%)
        </h3>
        <p className="text-xs text-slate-400 mb-4">
          Visual comparison showing the impact of simulated bandwidth expansion on cell saturation
        </p>
        <div className="h-56 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={comparisonData}>
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
              <Bar dataKey="Previous Load (%)" fill="#ef4444" radius={[4, 4, 0, 0]} />
              <Bar dataKey="Optimized Load (%)" fill="#10b981" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <DataTable
        title="Optimization Results"
        subtitle="Historical execution outcomes from the optimization_results database table"
        columns={resultColumns}
        data={results}
        searchPlaceholder="Filter results by Result ID, Cell ID, scores..."
        searchFilter={(r, q) =>
          String(r.resultId).includes(q) ||
          String(r.cellId).includes(q) ||
          String(r.previousLoad).includes(q)
        }
      />

      <DataTable
        title="Optimization Jobs"
        subtitle="Recent jobs recorded in the optimization_jobs database table"
        columns={jobColumns}
        data={jobs}
        searchPlaceholder="Filter jobs by Job ID, Name, Algorithm..."
        statusFilterKey="status"
        searchFilter={(j, q) =>
          String(j.jobId).includes(q) ||
          j.jobName.toLowerCase().includes(q) ||
          j.optimizationType.toLowerCase().includes(q)
        }
      />
    </div>
  );
};

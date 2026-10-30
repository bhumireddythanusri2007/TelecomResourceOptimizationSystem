import React from 'react';
import { useData } from '../context/DataContext';
import { Subscriber } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Users, UserCheck, HardDrive, Mail, Phone } from 'lucide-react';

export const SubscribersView: React.FC = () => {
  const { data } = useData();
  const subscribers = data.subscribers;

  const totalQuota = subscribers.reduce((acc: number, s: Subscriber) => acc + (s.monthlyDataLimit || 0), 0);
  const totalConsumed = subscribers.reduce((acc: number, s: Subscriber) => acc + (s.currentDataUsage || 0), 0);
  const activeSubs = subscribers.filter((s: Subscriber) => s.status.toLowerCase() === 'active').length;

  const columns: Column<Subscriber>[] = [
    {
      header: 'Sub ID',
      accessor: 'subscriberId',
      className: 'font-mono text-slate-400 font-bold',
      render: s => `#${s.subscriberId}`,
    },
    {
      header: 'Subscriber Profile',
      accessor: 'subscriberName',
      render: s => (
        <div>
          <div className="font-bold text-slate-100 flex items-center gap-2">
            {s.subscriberName}
          </div>
          <div className="text-[11px] text-slate-400 flex items-center gap-3 mt-0.5">
            <span className="flex items-center gap-1 font-mono">
              <Phone className="w-3 h-3 text-slate-500" />
              {s.phoneNumber}
            </span>
            <span className="flex items-center gap-1">
              <Mail className="w-3 h-3 text-slate-500" />
              {s.email}
            </span>
          </div>
        </div>
      ),
    },
    {
      header: 'Subscription Tier',
      accessor: 'subscriptionType',
      render: s => (
        <div>
          <span className="px-2.5 py-0.5 rounded text-[11px] font-semibold bg-blue-950/70 border border-blue-900/50 text-blue-300">
            {s.subscriptionType}
          </span>
          <div className="text-[11px] text-slate-400 mt-1 font-medium">{s.dataPlan}</div>
        </div>
      ),
    },
    {
      header: 'Monthly Quota Utilization',
      accessor: 'currentDataUsage',
      render: s => {
        const pct = Math.round((s.currentDataUsage / (s.monthlyDataLimit || 1)) * 100);
        const barColor =
          pct >= 90 ? 'bg-rose-500' : pct >= 75 ? 'bg-amber-500' : 'bg-blue-500';

        return (
          <div className="w-40">
            <div className="flex justify-between text-[11px] font-mono mb-1">
              <span className="text-slate-200">
                {s.currentDataUsage.toFixed(1)} / {s.monthlyDataLimit} GB
              </span>
              <span className={pct >= 90 ? 'text-rose-400 font-bold' : 'text-slate-400'}>
                {pct}%
              </span>
            </div>
            <div className="w-full bg-navy-950 h-1.5 rounded-full overflow-hidden border border-navy-800">
              <div
                className={`h-full ${barColor} rounded-full transition-all duration-300`}
                style={{ width: `${Math.min(pct, 100)}%` }}
              />
            </div>
          </div>
        );
      },
    },
    {
      header: 'Account Status',
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
              s.status.toLowerCase() === 'active' ? 'bg-emerald-400' : 'bg-slate-400'
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
          title="Total Subscribers"
          value={subscribers.length}
          unit="accounts"
          subtext="Enterprise & Consumer tiers"
          icon={Users}
          variant="purple"
        />
        <KpiCard
          title="Active Accounts"
          value={activeSubs}
          unit="profiles"
          subtext="Authorized for network access"
          icon={UserCheck}
          variant="emerald"
        />
        <KpiCard
          title="Total Data Consumed"
          value={Math.round(totalConsumed).toLocaleString()}
          unit="GB"
          subtext="Current billing cycle usage"
          icon={HardDrive}
          variant="blue"
        />
        <KpiCard
          title="Total Allocated Quota"
          value={Math.round(totalQuota).toLocaleString()}
          unit="GB"
          subtext="Cumulative monthly allowance"
          icon={HardDrive}
          variant="cyan"
        />
      </div>

      <DataTable
        title="Telecom Subscriber Accounts & Data Plans"
        subtitle="Manage QoS priority, monthly bandwidth limits, and data utilization"
        columns={columns}
        data={subscribers}
        searchPlaceholder="Search subscriber name, email, phone, plan..."
        statusFilterKey="status"
        searchFilter={(s, q) =>
          s.subscriberName.toLowerCase().includes(q) ||
          s.email.toLowerCase().includes(q) ||
          s.phoneNumber.toLowerCase().includes(q) ||
          s.subscriptionType.toLowerCase().includes(q) ||
          s.dataPlan.toLowerCase().includes(q)
        }
      />
    </div>
  );
};

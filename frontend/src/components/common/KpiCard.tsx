import React from 'react';
import { LucideIcon } from 'lucide-react';

interface KpiCardProps {
  title: string;
  value: string | number;
  unit?: string;
  subtext?: string;
  icon: LucideIcon;
  trend?: {
    value: string;
    isPositive?: boolean;
    label?: string;
  };
  variant?: 'blue' | 'emerald' | 'amber' | 'rose' | 'cyan' | 'purple';
  loading?: boolean;
}

export const KpiCard: React.FC<KpiCardProps> = ({
  title,
  value,
  unit,
  subtext,
  icon: Icon,
  trend,
  variant = 'blue',
  loading = false,
}) => {
  const variantStyles = {
    blue: {
      bg: 'bg-navy-900/90 hover:bg-navy-850/90 border-blue-900/40 hover:border-blue-700/50',
      iconBg: 'bg-blue-950 text-blue-400 border-blue-800/40',
      glow: 'group-hover:shadow-[0_0_20px_rgba(37,99,235,0.15)]',
      valColor: 'text-blue-100',
    },
    emerald: {
      bg: 'bg-navy-900/90 hover:bg-navy-850/90 border-emerald-900/40 hover:border-emerald-700/50',
      iconBg: 'bg-emerald-950 text-emerald-400 border-emerald-800/40',
      glow: 'group-hover:shadow-[0_0_20px_rgba(16,185,129,0.15)]',
      valColor: 'text-emerald-100',
    },
    amber: {
      bg: 'bg-navy-900/90 hover:bg-navy-850/90 border-amber-900/40 hover:border-amber-700/50',
      iconBg: 'bg-amber-950 text-amber-400 border-amber-800/40',
      glow: 'group-hover:shadow-[0_0_20px_rgba(245,158,11,0.15)]',
      valColor: 'text-amber-100',
    },
    rose: {
      bg: 'bg-navy-900/90 hover:bg-navy-850/90 border-rose-900/40 hover:border-rose-700/50',
      iconBg: 'bg-rose-950 text-rose-400 border-rose-800/40',
      glow: 'group-hover:shadow-[0_0_20px_rgba(239,68,68,0.15)]',
      valColor: 'text-rose-100',
    },
    cyan: {
      bg: 'bg-navy-900/90 hover:bg-navy-850/90 border-cyan-900/40 hover:border-cyan-700/50',
      iconBg: 'bg-cyan-950 text-cyan-400 border-cyan-800/40',
      glow: 'group-hover:shadow-[0_0_20px_rgba(6,182,212,0.15)]',
      valColor: 'text-cyan-100',
    },
    purple: {
      bg: 'bg-navy-900/90 hover:bg-navy-850/90 border-purple-900/40 hover:border-purple-700/50',
      iconBg: 'bg-purple-950 text-purple-400 border-purple-800/40',
      glow: 'group-hover:shadow-[0_0_20px_rgba(139,92,246,0.15)]',
      valColor: 'text-purple-100',
    },
  };

  const style = variantStyles[variant];

  return (
    <div
      className={`group relative p-5 rounded-xl border backdrop-blur-md transition-all duration-300 shadow-md ${style.bg} ${style.glow}`}
    >
      <div className="flex items-center justify-between mb-3">
        <span className="text-xs font-semibold uppercase tracking-wider text-slate-400">
          {title}
        </span>
        <div className={`p-2.5 rounded-lg border ${style.iconBg}`}>
          <Icon className="w-5 h-5" />
        </div>
      </div>

      <div className="flex items-baseline gap-2">
        {loading ? (
          <div className="h-8 w-24 bg-navy-800 animate-pulse rounded my-1" />
        ) : (
          <div className={`text-3xl font-extrabold tracking-tight ${style.valColor}`}>
            {value}
          </div>
        )}
        {unit && !loading && (
          <span className="text-sm font-medium text-slate-400">{unit}</span>
        )}
      </div>

      <div className="mt-3 flex items-center justify-between text-xs text-slate-400">
        <span>{subtext}</span>
        {trend && !loading && (
          <span
            className={`font-semibold inline-flex items-center gap-0.5 ${
              trend.isPositive ? 'text-emerald-400' : 'text-amber-400'
            }`}
          >
            {trend.value} {trend.label}
          </span>
        )}
      </div>
    </div>
  );
};

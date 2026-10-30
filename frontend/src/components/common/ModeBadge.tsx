import React from 'react';
import { DataSourceMode } from '../../types';
import { Radio, Database, HardDrive, AlertTriangle } from 'lucide-react';

interface ModeBadgeProps {
  mode: DataSourceMode;
  className?: string;
}

export const ModeBadge: React.FC<ModeBadgeProps> = ({ mode, className = '' }) => {
  switch (mode) {
    case 'LIVE':
      return (
        <span
          className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold tracking-wider uppercase bg-emerald-950/80 text-emerald-300 border border-emerald-500/40 shadow-[0_0_12px_rgba(16,185,129,0.25)] ${className}`}
        >
          <span className="w-2 h-2 rounded-full bg-emerald-400 animate-ping" />
          <Radio className="w-3.5 h-3.5 text-emerald-400" />
          LIVE BACKEND
        </span>
      );

    case 'DEMO':
      return (
        <span
          className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold tracking-wider uppercase bg-amber-950/80 text-amber-300 border border-amber-500/40 shadow-[0_0_12px_rgba(245,158,11,0.2)] ${className}`}
        >
          <Database className="w-3.5 h-3.5 text-amber-400" />
          DEMONSTRATION DATA
        </span>
      );

    case 'CACHED':
      return (
        <span
          className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold tracking-wider uppercase bg-sky-950/80 text-sky-300 border border-sky-500/40 shadow-[0_0_12px_rgba(56,189,248,0.2)] ${className}`}
        >
          <HardDrive className="w-3.5 h-3.5 text-sky-400" />
          CACHED SNAPSHOT
        </span>
      );

    case 'OFFLINE':
      return (
        <span
          className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold tracking-wider uppercase bg-rose-950/80 text-rose-300 border border-rose-500/40 shadow-[0_0_12px_rgba(239,68,68,0.25)] ${className}`}
        >
          <AlertTriangle className="w-3.5 h-3.5 text-rose-400 animate-pulse" />
          OFFLINE / DB UNAVAILABLE
        </span>
      );
  }
};

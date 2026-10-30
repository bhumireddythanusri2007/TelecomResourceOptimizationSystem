import React from 'react';
import { DataSourceMode } from '../../types';
import { AlertCircle, RefreshCw, Database, Info } from 'lucide-react';

interface BannerProps {
  mode: DataSourceMode;
  error: string | null;
  lastSyncTime: string | null;
  onRetry: () => void;
  onSwitchToDemo: () => void;
  onSwitchToCached?: () => void;
  hasCachedData?: boolean;
}

export const Banner: React.FC<BannerProps> = ({
  mode,
  error,
  lastSyncTime,
  onRetry,
  onSwitchToDemo,
  onSwitchToCached,
  hasCachedData,
}) => {
  if (mode === 'OFFLINE' || error) {
    return (
      <div className="mb-6 p-4 rounded-xl bg-rose-950/60 border border-rose-500/40 text-rose-200 shadow-lg backdrop-blur-sm">
        <div className="flex items-start gap-3">
          <AlertCircle className="w-5 h-5 text-rose-400 mt-0.5 shrink-0" />
          <div className="flex-1">
            <h4 className="text-sm font-bold text-rose-200 uppercase tracking-wide">
              Database Connection Unavailable
            </h4>
            <p className="text-xs text-rose-300 mt-1 leading-relaxed">
              {error || 'Unable to retrieve live telemetry records from the MySQL database (e.g. Aiven MySQL free tier power-off or network timeout).'}
            </p>
            <div className="mt-3 flex flex-wrap gap-2">
              <button
                onClick={onRetry}
                className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-rose-800 hover:bg-rose-700 text-white text-xs font-medium transition"
              >
                <RefreshCw className="w-3.5 h-3.5" />
                Retry Connection
              </button>
              <button
                onClick={onSwitchToDemo}
                className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-amber-800/80 hover:bg-amber-700 text-amber-100 text-xs font-medium transition border border-amber-600/40"
              >
                <Database className="w-3.5 h-3.5" />
                Switch to Demo Mode
              </button>
              {hasCachedData && onSwitchToCached && (
                <button
                  onClick={onSwitchToCached}
                  className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-sky-900/80 hover:bg-sky-800 text-sky-200 text-xs font-medium transition border border-sky-700/40"
                >
                  View Cached Snapshot
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (mode === 'DEMO') {
    return (
      <div className="mb-6 p-3.5 rounded-xl bg-amber-950/40 border border-amber-500/30 text-amber-200 shadow-sm backdrop-blur-sm">
        <div className="flex items-center justify-between flex-wrap gap-3">
          <div className="flex items-center gap-2.5">
            <Info className="w-4 h-4 text-amber-400 shrink-0" />
            <p className="text-xs text-amber-200">
              <strong className="font-semibold text-amber-300">DEMONSTRATION MODE ACTIVE:</strong> Showing deterministic telecom simulation dataset. No database write operations will occur.
            </p>
          </div>
          <button
            onClick={onRetry}
            className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded bg-amber-900/60 hover:bg-amber-800/80 text-amber-200 text-xs font-medium border border-amber-600/30 transition"
          >
            <RefreshCw className="w-3 h-3" />
            Attempt Live Sync
          </button>
        </div>
      </div>
    );
  }

  if (mode === 'CACHED') {
    return (
      <div className="mb-6 p-3.5 rounded-xl bg-sky-950/40 border border-sky-500/30 text-sky-200 shadow-sm backdrop-blur-sm">
        <div className="flex items-center justify-between flex-wrap gap-3">
          <div className="flex items-center gap-2.5">
            <Info className="w-4 h-4 text-sky-400 shrink-0" />
            <p className="text-xs text-sky-200">
              <strong className="font-semibold text-sky-300">CACHED SNAPSHOT:</strong> Showing locally persisted snapshot from {lastSyncTime || 'prior session'}. Live database is currently resting.
            </p>
          </div>
          <button
            onClick={onRetry}
            className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded bg-sky-900/60 hover:bg-sky-800/80 text-sky-200 text-xs font-medium border border-sky-600/30 transition"
          >
            <RefreshCw className="w-3 h-3" />
            Sync with Live
          </button>
        </div>
      </div>
    );
  }

  return null;
};

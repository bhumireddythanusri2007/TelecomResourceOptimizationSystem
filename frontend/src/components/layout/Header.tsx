import React from 'react';
import { useData } from '../../context/DataContext';
import { ModeBadge } from '../common/ModeBadge';
import { RefreshCw, Radio, HardDrive, Database, Server } from 'lucide-react';

export const Header: React.FC = () => {
  const { mode, setMode, loading, lastSyncTime, refresh, hasCachedData } = useData();

  return (
    <header className="sticky top-0 z-30 bg-navy-950/95 backdrop-blur-md border-b border-blue-900/40 px-6 py-3.5">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        {/* Left: Branding & Subtitle */}
        <div className="flex items-center gap-3">
          <div className="p-2 rounded-lg bg-blue-600/20 border border-blue-500/30 text-blue-400">
            <Radio className="w-5 h-5 animate-pulse" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-base font-extrabold text-white tracking-tight">
                Telecom Network Resource Management & Monitoring System
              </h1>
              <span className="hidden md:inline-block px-2 py-0.5 text-[10px] font-semibold bg-blue-900/60 text-blue-200 border border-blue-700/50 rounded">
                Jakarta EE 10 • Jetty 12
              </span>
            </div>
            <p className="text-xs text-slate-400">
              Enterprise Resource Optimization • Base Station & Slice Telemetry
            </p>
          </div>
        </div>

        {/* Right: Data Mode Controller & Sync Status */}
        <div className="flex items-center flex-wrap gap-3">
          {/* Active Data Mode Indicator */}
          <ModeBadge mode={mode} />

          {/* Mode Switcher Buttons */}
          <div className="inline-flex rounded-lg p-0.5 bg-navy-900 border border-navy-800 text-xs">
            <button
              onClick={() => setMode('LIVE')}
              title="Connect to live Java Backend & MySQL"
              className={`flex items-center gap-1 px-2.5 py-1 rounded-md transition font-medium ${
                mode === 'LIVE'
                  ? 'bg-blue-600 text-white shadow-sm'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Server className="w-3 h-3" />
              Live
            </button>
            <button
              onClick={() => setMode('DEMO')}
              title="Use deterministic demonstration dataset"
              className={`flex items-center gap-1 px-2.5 py-1 rounded-md transition font-medium ${
                mode === 'DEMO'
                  ? 'bg-amber-600 text-white shadow-sm'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Database className="w-3 h-3" />
              Demo
            </button>
            <button
              onClick={() => setMode('CACHED')}
              title="Load saved snapshot from local storage"
              disabled={!hasCachedData}
              className={`flex items-center gap-1 px-2.5 py-1 rounded-md transition font-medium ${
                mode === 'CACHED'
                  ? 'bg-sky-600 text-white shadow-sm'
                  : 'text-slate-400 hover:text-slate-200 disabled:opacity-30 disabled:cursor-not-allowed'
              }`}
            >
              <HardDrive className="w-3 h-3" />
              Cached
            </button>
          </div>

          {/* Refresh Action */}
          <button
            onClick={refresh}
            disabled={loading}
            title={`Last synced: ${lastSyncTime || 'N/A'}`}
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-navy-900 hover:bg-navy-800 border border-navy-800 text-slate-300 hover:text-white text-xs font-medium transition"
          >
            <RefreshCw className={`w-3.5 h-3.5 text-blue-400 ${loading ? 'animate-spin' : ''}`} />
            <span className="hidden sm:inline">Sync</span>
          </button>
        </div>
      </div>
    </header>
  );
};

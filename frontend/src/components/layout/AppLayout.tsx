import React, { useState } from 'react';
import { NavModule } from '../../types';
import { Header } from './Header';
import { Sidebar } from './Sidebar';
import { Banner } from '../common/Banner';
import { useData } from '../../context/DataContext';
import { Menu, X } from 'lucide-react';

interface AppLayoutProps {
  currentModule: NavModule;
  onSelectModule: (module: NavModule) => void;
  children: React.ReactNode;
}

export const AppLayout: React.FC<AppLayoutProps> = ({
  currentModule,
  onSelectModule,
  children,
}) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { mode, setMode, error, lastSyncTime, refresh, hasCachedData } = useData();

  return (
    <div className="min-h-screen bg-navy-950 text-slate-100 flex flex-col font-sans telecom-grid-bg">
      {/* Top Header */}
      <Header />

      <div className="flex-1 flex overflow-hidden">
        {/* Mobile Sidebar Overlay */}
        {sidebarOpen && (
          <div
            onClick={() => setSidebarOpen(false)}
            className="fixed inset-0 bg-black/60 z-30 lg:hidden backdrop-blur-sm"
          />
        )}

        {/* Sidebar */}
        <Sidebar
          currentModule={currentModule}
          onSelectModule={onSelectModule}
          isOpen={sidebarOpen}
          onToggle={() => setSidebarOpen(!sidebarOpen)}
        />

        {/* Main Workspace Area */}
        <main className="flex-1 flex flex-col min-w-0 overflow-y-auto">
          {/* Mobile Top Bar with Toggle */}
          <div className="lg:hidden p-3 bg-navy-900/80 border-b border-navy-800 flex items-center justify-between">
            <button
              onClick={() => setSidebarOpen(!sidebarOpen)}
              className="p-1.5 rounded-lg bg-navy-950 text-slate-300 hover:text-white border border-navy-800"
            >
              {sidebarOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
            </button>
            <span className="text-xs font-semibold text-slate-300 uppercase tracking-wider">
              {currentModule.replace('-', ' ')}
            </span>
            <div className="w-5" />
          </div>

          <div className="flex-1 p-4 md:p-6 lg:p-8 max-w-7xl w-full mx-auto">
            {/* Global Context Banner (Offline warning / Demo Mode label) */}
            <Banner
              mode={mode}
              error={error}
              lastSyncTime={lastSyncTime}
              onRetry={refresh}
              onSwitchToDemo={() => setMode('DEMO')}
              onSwitchToCached={() => setMode('CACHED')}
              hasCachedData={hasCachedData}
            />

            {/* Dynamic Module Content */}
            {children}
          </div>

          {/* Footer */}
          <footer className="mt-auto border-t border-navy-900 bg-navy-950/80 py-4 px-6 text-center text-xs text-slate-400 flex flex-col sm:flex-row items-center justify-between gap-2">
            <div>
              Design and Development of a Telecom Network Resource Management and Monitoring System using Java Enterprise Technologies
            </div>
            <div className="font-mono text-slate-400">
              Academic Project • Operations Console
            </div>
          </footer>
        </main>
      </div>
    </div>
  );
};

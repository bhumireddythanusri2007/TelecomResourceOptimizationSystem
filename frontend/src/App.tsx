import React, { useState, useEffect } from 'react';
import { NavModule } from './types';
import { DataProvider, useData } from './context/DataContext';
import { AppLayout } from './components/layout/AppLayout';
import { OverviewDashboardView } from './views/OverviewDashboardView';
import { NetworkCellsView } from './views/NetworkCellsView';
import { DevicesView } from './views/DevicesView';
import { SubscribersView } from './views/SubscribersView';
import { TrafficManagementView } from './views/TrafficManagementView';
import { ResourceAllocationView } from './views/ResourceAllocationView';
import { NetworkSlicesView } from './views/NetworkSlicesView';
import { EdgeServersView } from './views/EdgeServersView';
import { OptimizationView } from './views/OptimizationView';
import { KpiMonitoringView } from './views/KpiMonitoringView';
import { UserManagementView } from './views/UserManagementView';
import { AlertsView } from './views/AlertsView';
import { ReportsView } from './views/ReportsView';
import { RefreshCw } from 'lucide-react';

export const AppContent: React.FC = () => {
  const [currentModule, setCurrentModule] = useState<NavModule>('overview');
  const { ensureModuleLoaded, moduleLoading } = useData();

  useEffect(() => {
    ensureModuleLoaded(currentModule);
  }, [currentModule]);

  const renderModule = () => {
    switch (currentModule) {
      case 'overview':
        return <OverviewDashboardView onNavigate={setCurrentModule} />;
      case 'network-cells':
        return <NetworkCellsView />;
      case 'devices':
        return <DevicesView />;
      case 'subscribers':
        return <SubscribersView />;
      case 'traffic':
        return <TrafficManagementView />;
      case 'resource-allocation':
        return <ResourceAllocationView />;
      case 'network-slices':
        return <NetworkSlicesView />;
      case 'edge-servers':
        return <EdgeServersView />;
      case 'optimization':
        return <OptimizationView />;
      case 'kpi-monitoring':
        return <KpiMonitoringView />;
      case 'users':
        return <UserManagementView />;
      case 'alerts':
        return <AlertsView />;
      case 'reports':
        return <ReportsView />;
      default:
        return <OverviewDashboardView onNavigate={setCurrentModule} />;
    }
  };

  return (
    <AppLayout currentModule={currentModule} onSelectModule={setCurrentModule}>
      {moduleLoading && (
        <div className="flex items-center gap-2 mb-4 p-2.5 rounded-lg bg-blue-950/60 border border-blue-500/30 text-blue-300 text-xs animate-pulse">
          <RefreshCw className="w-3.5 h-3.5 animate-spin text-blue-400" />
          <span>Fetching live telemetry data from backend for {currentModule}...</span>
        </div>
      )}
      {renderModule()}
    </AppLayout>
  );
};

export const App: React.FC = () => {
  return (
    <DataProvider>
      <AppContent />
    </DataProvider>
  );
};

export default App;

import React, { createContext, useContext, useState, useEffect, useRef, ReactNode } from 'react';
import { DataSourceMode, SystemSnapshot, NavModule } from '../types';
import { DEMO_SNAPSHOT, EMPTY_SNAPSHOT } from '../data/demoData';
import { TelecomApiService } from '../services/apiService';

interface DataContextType {
  mode: DataSourceMode;
  setMode: (mode: DataSourceMode) => void;
  data: SystemSnapshot;
  loading: boolean;
  moduleLoading: boolean;
  error: string | null;
  lastSyncTime: string | null;
  refresh: () => Promise<void>;
  ensureModuleLoaded: (module: NavModule) => Promise<void>;
  runOptimization: () => Promise<{ success: boolean; message: string }>;
  hasCachedData: boolean;
}

const DataContext = createContext<DataContextType | undefined>(undefined);

const PREF_MODE_KEY = 'telecom_preferred_mode_v1';

export const DataProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [mode, setModeState] = useState<DataSourceMode>('DEMO');
  const [data, setData] = useState<SystemSnapshot>(DEMO_SNAPSHOT);
  const [loading, setLoading] = useState<boolean>(false);
  const [moduleLoading, setModuleLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [lastSyncTime, setLastSyncTime] = useState<string | null>(null);

  const loadedModulesRef = useRef<Set<NavModule>>(new Set());

  const cached = TelecomApiService.loadCachedSnapshot();
  const hasCachedData = cached !== null;

  const loadData = async (targetMode: DataSourceMode) => {
    setLoading(true);
    setError(null);

    if (targetMode === 'DEMO') {
      setData(DEMO_SNAPSHOT);
      setModeState('DEMO');
      setLastSyncTime(new Date().toLocaleTimeString());
      localStorage.setItem(PREF_MODE_KEY, 'DEMO');
      setLoading(false);
      return;
    }

    if (targetMode === 'CACHED') {
      const cachedSnapshot = TelecomApiService.loadCachedSnapshot();
      if (cachedSnapshot) {
        setData(cachedSnapshot);
        setModeState('CACHED');
        setLastSyncTime(new Date(cachedSnapshot.timestamp).toLocaleTimeString());
        localStorage.setItem(PREF_MODE_KEY, 'CACHED');
      } else {
        setError('No cached snapshot found in local storage.');
      }
      setLoading(false);
      return;
    }

    if (targetMode === 'LIVE') {
      localStorage.setItem(PREF_MODE_KEY, 'LIVE');
      const { stats, alerts, cells, slices, kpis, error: liveError } = await TelecomApiService.fetchDashboardOverview();
      if (stats && !liveError) {
        setData(prev => {
          const next: SystemSnapshot = {
            ...prev,
            stats,
            alerts: alerts.length > 0 ? alerts : prev.alerts,
            cells: cells.length > 0 ? cells : prev.cells,
            slices: slices.length > 0 ? slices : prev.slices,
            kpis: kpis.length > 0 ? kpis : prev.kpis,
            timestamp: new Date().toISOString(),
          };
          TelecomApiService.saveCachedSnapshot(next);
          return next;
        });
        setModeState('LIVE');
        setLastSyncTime(new Date().toLocaleTimeString());
        setError(null);
        loadedModulesRef.current = new Set(['overview', 'alerts', 'network-cells', 'network-slices', 'kpi-monitoring']);
      } else {
        // Strict requirement: Do not silently substitute demo data when a live request fails
        setData(EMPTY_SNAPSHOT);
        setError(liveError || 'Unable to connect to live backend');
        setModeState('OFFLINE');
        loadedModulesRef.current.clear();
      }
      setLoading(false);
    }
  };

  const setMode = (newMode: DataSourceMode) => {
    loadData(newMode);
  };

  const refresh = async () => {
    if (mode === 'LIVE' || mode === 'OFFLINE') {
      await loadData('LIVE');
    } else if (mode === 'DEMO') {
      await loadData('DEMO');
    } else if (mode === 'CACHED') {
      await loadData('CACHED');
    }
  };

  const ensureModuleLoaded = async (module: NavModule) => {
    if (mode !== 'LIVE') return;
    if (module === 'overview') return;
    if (loadedModulesRef.current.has(module)) return;

    setModuleLoading(true);
    const res = await TelecomApiService.fetchModuleData(module);
    if (res.error) {
      setError(`Failed to load ${module}: ${res.error}`);
    } else if (res.data) {
      setData(prev => {
        const next = { ...prev };
        switch (module) {
          case 'network-cells':
            next.cells = res.data;
            break;
          case 'devices':
            next.devices = res.data;
            break;
          case 'subscribers':
            next.subscribers = res.data;
            break;
          case 'traffic':
            next.trafficFlows = res.data;
            break;
          case 'resource-allocation':
            next.allocations = res.data;
            break;
          case 'network-slices':
            next.slices = res.data;
            break;
          case 'edge-servers':
            next.edgeServers = res.data;
            break;
          case 'optimization':
            next.results = res.data.results || [];
            next.jobs = res.data.jobs || [];
            break;
          case 'kpi-monitoring':
            next.kpis = res.data;
            break;
          case 'users':
            next.users = res.data;
            break;
          case 'alerts':
            next.alerts = res.data;
            break;
          case 'reports':
            if (res.data) next.report = res.data;
            break;
        }
        next.timestamp = new Date().toISOString();
        TelecomApiService.saveCachedSnapshot(next);
        return next;
      });
      loadedModulesRef.current.add(module);
    }
    setModuleLoading(false);
  };

  const runOptimization = async (): Promise<{ success: boolean; message: string }> => {
    setLoading(true);
    const res = await TelecomApiService.runOptimizationSimulation(mode);
    if (res.success) {
      if (mode === 'DEMO') {
        setData(prev => ({
          ...prev,
          results: [
            {
              resultId: 1006,
              cellId: 101,
              previousLoad: 78.3,
              optimizedLoad: 68.3,
              allocatedBandwidth: 120,
              performanceScore: 91.2,
              optimizationTime: new Date().toLocaleTimeString(),
            },
            ...prev.results,
          ],
          jobs: [
            {
              jobId: 905,
              jobName: 'Rule-Based Load Rebalance',
              optimizationType: 'RULE_BASED_SIMULATION',
              targetCellId: null,
              objective: 'Triggered from Dashboard',
              inputLoad: 76.5,
              status: 'Completed',
              createdTime: new Date().toLocaleTimeString(),
            },
            ...prev.jobs,
          ],
        }));
      } else if (mode === 'LIVE') {
        loadedModulesRef.current.delete('optimization');
        await ensureModuleLoaded('optimization');
        const overview = await TelecomApiService.fetchDashboardOverview();
        if (overview.stats) {
          setData(prev => ({ ...prev, stats: overview.stats! }));
        }
      }
    }
    setLoading(false);
    return res;
  };

  useEffect(() => {
    // Check initial preference; default attempt is LIVE
    const savedPref = localStorage.getItem(PREF_MODE_KEY) as DataSourceMode | null;
    if (savedPref === 'DEMO') {
      loadData('DEMO');
    } else if (savedPref === 'CACHED' && TelecomApiService.loadCachedSnapshot()) {
      loadData('CACHED');
    } else {
      loadData('LIVE');
    }
  }, []);

  return (
    <DataContext.Provider
      value={{
        mode,
        setMode,
        data,
        loading,
        moduleLoading,
        error,
        lastSyncTime,
        refresh,
        ensureModuleLoaded,
        runOptimization,
        hasCachedData,
      }}
    >
      {children}
    </DataContext.Provider>
  );
};

export const useData = () => {
  const context = useContext(DataContext);
  if (!context) {
    throw new Error('useData must be used within a DataProvider');
  }
  return context;
};

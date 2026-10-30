import React from 'react';
import { NavModule } from '../../types';
import { useData } from '../../context/DataContext';
import {
  LayoutDashboard,
  Radio,
  Smartphone,
  Users,
  Activity,
  Layers,
  Network,
  Server,
  Zap,
  Gauge,
  UserCheck,
  Bell,
  FileText,
  ChevronRight
} from 'lucide-react';

interface SidebarProps {
  currentModule: NavModule;
  onSelectModule: (module: NavModule) => void;
  isOpen: boolean;
  onToggle: () => void;
}

interface NavItem {
  id: NavModule;
  label: string;
  icon: React.ElementType;
  badge?: number;
  badgeColor?: string;
  category?: string;
}

export const Sidebar: React.FC<SidebarProps> = ({
  currentModule,
  onSelectModule,
  isOpen,
  onToggle,
}) => {
  const { data } = useData();

  const openAlertsCount = data.alerts.filter(a => a.status === 'OPEN').length;

  const navItems: NavItem[] = [
    { id: 'overview', label: 'Network Overview', icon: LayoutDashboard, category: 'CORE' },
    { id: 'network-cells', label: 'Network Cells', icon: Radio, category: 'INFRASTRUCTURE' },
    { id: 'devices', label: 'Devices', icon: Smartphone, category: 'INFRASTRUCTURE' },
    { id: 'subscribers', label: 'Subscribers', icon: Users, category: 'INFRASTRUCTURE' },
    { id: 'traffic', label: 'Traffic Flows', icon: Activity, category: 'TELEMETRY' },
    { id: 'resource-allocation', label: 'Resource Allocation', icon: Layers, category: 'TELEMETRY' },
    { id: 'network-slices', label: 'Network Slices', icon: Network, category: '5G ARCHITECTURE' },
    { id: 'edge-servers', label: 'Edge Servers', icon: Server, category: '5G ARCHITECTURE' },
    { id: 'optimization', label: 'Optimization Engine', icon: Zap, category: 'ANALYTICS' },
    { id: 'kpi-monitoring', label: 'KPI Monitoring', icon: Gauge, category: 'ANALYTICS' },
    {
      id: 'alerts',
      label: 'Network Alerts',
      icon: Bell,
      badge: openAlertsCount > 0 ? openAlertsCount : undefined,
      badgeColor: 'bg-rose-500 text-white',
      category: 'MONITORING'
    },
    { id: 'reports', label: 'Reports & Audits', icon: FileText, category: 'MONITORING' },
    { id: 'users', label: 'User Directory', icon: UserCheck, category: 'SYSTEM' },
  ];

  let currentCategory = '';

  return (
    <aside
      className={`fixed lg:static top-0 bottom-0 left-0 z-40 w-64 bg-navy-950/90 border-r border-blue-950 flex flex-col transition-transform duration-300 backdrop-blur-xl ${
        isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
      }`}
    >
      {/* Brand Header inside Sidebar */}
      <div className="p-4 border-b border-navy-800/80 flex items-center justify-between">
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-lg bg-blue-600 flex items-center justify-center text-white shadow-[0_0_15px_rgba(37,99,235,0.4)]">
            <Radio className="w-5 h-5" />
          </div>
          <div>
            <div className="text-xs font-bold text-white tracking-wider uppercase">
              Telecom NOC
            </div>
            <div className="text-[10px] text-blue-400 font-mono">
              NetOps v2.4 Enterprise
            </div>
          </div>
        </div>
      </div>

      {/* Navigation Links Scrollable */}
      <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-1">
        {navItems.map(item => {
          const isSelected = currentModule === item.id;
          const showCategory = item.category && item.category !== currentCategory;
          if (showCategory) {
            currentCategory = item.category!;
          }

          return (
            <React.Fragment key={item.id}>
              {showCategory && (
                <div className="pt-3 pb-1.5 px-3 text-[10px] font-bold uppercase tracking-wider text-slate-400">
                  {item.category}
                </div>
              )}
              <button
                onClick={() => {
                  onSelectModule(item.id);
                  if (window.innerWidth < 1024) onToggle();
                }}
                className={`w-full flex items-center justify-between px-3 py-2.5 rounded-lg text-xs font-semibold transition-all duration-150 group ${
                  isSelected
                    ? 'bg-blue-600 text-white shadow-[0_0_16px_rgba(37,99,235,0.3)]'
                    : 'text-slate-300 hover:text-white hover:bg-navy-900/80'
                }`}
              >
                <div className="flex items-center gap-3">
                  <item.icon
                    className={`w-4 h-4 transition-colors ${
                      isSelected ? 'text-white' : 'text-slate-400 group-hover:text-blue-400'
                    }`}
                  />
                  <span>{item.label}</span>
                </div>

                <div className="flex items-center gap-1.5">
                  {item.badge !== undefined && (
                    <span
                      className={`px-1.5 py-0.5 rounded-full text-[10px] font-bold leading-none ${
                        item.badgeColor || 'bg-blue-500 text-white'
                      }`}
                    >
                      {item.badge}
                    </span>
                  )}
                  {isSelected && <ChevronRight className="w-3.5 h-3.5 opacity-80" />}
                </div>
              </button>
            </React.Fragment>
          );
        })}
      </nav>

      {/* Footer Info inside Sidebar */}
      <div className="p-3.5 border-t border-navy-800/80 text-[11px] text-slate-400">
        <div className="flex items-center justify-between">
          <span className="font-mono text-slate-400">Backend Port</span>
          <span className="font-mono text-emerald-400 font-semibold">:8080 (Jetty)</span>
        </div>
        <div className="flex items-center justify-between mt-1">
          <span className="font-mono text-slate-400">Database</span>
          <span className="font-mono text-blue-400 font-semibold">MySQL 9.4</span>
        </div>
      </div>
    </aside>
  );
};

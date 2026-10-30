import React from 'react';
import { useData } from '../context/DataContext';
import { Device } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { Smartphone, Wifi, Cpu, CheckCircle2, ShieldCheck } from 'lucide-react';

export const DevicesView: React.FC = () => {
  const { data } = useData();
  const devices = data.devices;

  const onlineDevices = devices.filter(
    d => d.status.toLowerCase() === 'online' || d.status.toLowerCase() === 'active'
  ).length;

  const columns: Column<Device>[] = [
    {
      header: 'Device ID',
      accessor: 'deviceId',
      className: 'font-mono text-slate-400 font-bold',
      render: d => `#${d.deviceId}`,
    },
    {
      header: 'Device Name & Unit',
      accessor: 'deviceName',
      render: d => (
        <div className="flex items-center gap-2.5">
          <div className="p-1.5 rounded bg-cyan-950 text-cyan-400 border border-cyan-900/50">
            <Cpu className="w-3.5 h-3.5" />
          </div>
          <div>
            <div className="font-bold text-slate-100">{d.deviceName}</div>
            <div className="text-[10px] text-slate-400 font-mono">Assigned User #{d.userId}</div>
          </div>
        </div>
      ),
    },
    {
      header: 'Hardware / Device Type',
      accessor: 'deviceType',
      render: d => (
        <span className="px-2.5 py-1 rounded-md text-xs font-medium bg-navy-950 border border-navy-800 text-slate-300">
          {d.deviceType}
        </span>
      ),
    },
    {
      header: 'IP Address',
      accessor: 'ipAddress',
      render: d => (
        <span className="font-mono text-xs text-blue-300 bg-blue-950/50 px-2 py-0.5 rounded border border-blue-900/40">
          {d.ipAddress}
        </span>
      ),
    },
    {
      header: 'Connection Interface',
      accessor: 'connectionType',
      render: d => (
        <span className="flex items-center gap-1.5 text-slate-300">
          <Wifi className="w-3.5 h-3.5 text-slate-400" />
          {d.connectionType}
        </span>
      ),
    },
    {
      header: 'Status',
      accessor: 'status',
      render: d => {
        const isOnline =
          d.status.toLowerCase() === 'online' || d.status.toLowerCase() === 'active';
        return (
          <span
            className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[11px] font-semibold border ${
              isOnline
                ? 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60'
                : 'bg-amber-950/80 text-amber-300 border-amber-800/60'
            }`}
          >
            <span
              className={`w-1.5 h-1.5 rounded-full ${
                isOnline ? 'bg-emerald-400 animate-pulse' : 'bg-amber-400'
              }`}
            />
            {d.status}
          </span>
        );
      },
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Total Devices"
          value={devices.length}
          unit="units"
          subtext="Base station controllers & gateways"
          icon={Smartphone}
          variant="cyan"
        />
        <KpiCard
          title="Online & Connected"
          value={onlineDevices}
          unit="devices"
          subtext="Active telemetry streams"
          icon={CheckCircle2}
          variant="emerald"
        />
        <KpiCard
          title="Standby / Idle"
          value={devices.length - onlineDevices}
          unit="devices"
          subtext="Available reserve units"
          icon={Cpu}
          variant="amber"
        />
        <KpiCard
          title="Security Perimeter"
          value="100%"
          subtext="IPsec / TLS Encrypted"
          icon={ShieldCheck}
          variant="blue"
        />
      </div>

      <DataTable
        title="Network Devices & Hardware Inventory"
        subtitle="Catalog of registered routers, baseband aggregators, and IoT gateways"
        columns={columns}
        data={devices}
        searchPlaceholder="Search by device name, type, IP address..."
        statusFilterKey="status"
        searchFilter={(d, q) =>
          d.deviceName.toLowerCase().includes(q) ||
          d.deviceType.toLowerCase().includes(q) ||
          d.ipAddress.toLowerCase().includes(q) ||
          d.connectionType.toLowerCase().includes(q)
        }
      />
    </div>
  );
};

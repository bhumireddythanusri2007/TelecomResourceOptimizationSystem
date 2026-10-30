import React from 'react';
import { useData } from '../context/DataContext';
import { UserSummary } from '../types';
import { DataTable, Column } from '../components/common/DataTable';
import { KpiCard } from '../components/common/KpiCard';
import { UserCheck, Shield, CheckCircle2, Lock } from 'lucide-react';

export const UserManagementView: React.FC = () => {
  const { data } = useData();
  const users = data.users;

  const activeUsers = users.filter((u: UserSummary) => u.status.toLowerCase() === 'active').length;
  const adminCount = users.filter((u: UserSummary) => u.role.toLowerCase() === 'admin').length;

  const columns: Column<UserSummary>[] = [
    {
      header: 'User ID',
      accessor: 'userId',
      className: 'font-mono text-slate-400 font-bold',
      render: u => `#${u.userId}`,
    },
    {
      header: 'Operator Username',
      accessor: 'username',
      render: u => (
        <div className="flex items-center gap-2.5">
          <div className="w-7 h-7 rounded-full bg-blue-950 border border-blue-800 text-blue-300 flex items-center justify-center font-bold text-xs uppercase">
            {u.username.slice(0, 2)}
          </div>
          <div>
            <div className="font-bold text-slate-100">{u.username}</div>
            <div className="text-[10px] text-slate-400 font-mono">{u.email}</div>
          </div>
        </div>
      ),
    },
    {
      header: 'System Role',
      accessor: 'role',
      render: u => {
        const roleStyle =
          u.role.toUpperCase() === 'ADMIN'
            ? 'bg-purple-950 text-purple-300 border-purple-800'
            : u.role.toUpperCase().includes('ENGINEER')
            ? 'bg-blue-950 text-blue-300 border-blue-800'
            : 'bg-cyan-950 text-cyan-300 border-cyan-800';

        return (
          <span className={`px-2.5 py-1 rounded text-xs font-semibold border ${roleStyle}`}>
            {u.role}
          </span>
        );
      },
    },
    {
      header: 'Account Status',
      accessor: 'status',
      render: u => (
        <span
          className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[11px] font-semibold border ${
            u.status.toLowerCase() === 'active'
              ? 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60'
              : 'bg-slate-900 text-slate-400 border-slate-700'
          }`}
        >
          <span
            className={`w-1.5 h-1.5 rounded-full ${
              u.status.toLowerCase() === 'active' ? 'bg-emerald-400' : 'bg-slate-400'
            }`}
          />
          {u.status}
        </span>
      ),
    },
    {
      header: 'Credential Security',
      render: () => (
        <span className="inline-flex items-center gap-1 text-slate-400 text-xs font-mono">
          <Lock className="w-3.5 h-3.5 text-emerald-400" />
          Encrypted (Argon2/BCrypt)
        </span>
      ),
    },
    {
      header: 'Registered Date',
      accessor: 'createdAt',
      render: u => <span className="font-mono text-xs text-slate-400">{u.createdAt}</span>,
    },
  ];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <KpiCard
          title="Total Users"
          value={users.length}
          unit="accounts"
          subtext="Configured operations accounts"
          icon={UserCheck}
          variant="purple"
        />
        <KpiCard
          title="Active Operators"
          value={activeUsers}
          unit="active"
          subtext="Currently authorized"
          icon={CheckCircle2}
          variant="emerald"
        />
        <KpiCard
          title="System Administrators"
          value={adminCount}
          unit="admins"
          subtext="Superuser privileges"
          icon={Shield}
          variant="blue"
        />
        <KpiCard
          title="Security Enforcement"
          value="Role-Based"
          subtext="RBAC access control enabled"
          icon={Lock}
          variant="cyan"
        />
      </div>

      <DataTable
        title="Telecom Operations User Directory & RBAC Roles"
        subtitle="Manage engineers, network operators, and administrators (credentials securely masked)"
        columns={columns}
        data={users}
        searchPlaceholder="Search username, role, email..."
        statusFilterKey="status"
        searchFilter={(u, q) =>
          u.username.toLowerCase().includes(q) ||
          u.role.toLowerCase().includes(q) ||
          u.email.toLowerCase().includes(q)
        }
      />
    </div>
  );
};

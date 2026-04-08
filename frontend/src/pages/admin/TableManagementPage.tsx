import { useState } from 'react';
import { useTables, useCreateTable } from '../../hooks/useTables';
import type { TableCreateRequest } from '../../types/table';

export default function TableManagementPage() {
  const { data: tables = [], isLoading } = useTables();
  const createTable = useCreateTable();

  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<TableCreateRequest>({ number: 0, name: '', password: '' });

  const handleCreate = () => {
    if (!form.number || !form.name.trim() || !form.password.trim()) return;
    createTable.mutate(form, {
      onSuccess: () => {
        setShowForm(false);
        setForm({ number: 0, name: '', password: '' });
      },
    });
  };

  if (isLoading) {
    return <div className="flex items-center justify-center p-12"><p className="text-gray-400">로딩 중...</p></div>;
  }

  return (
    <div>
      <div className="flex items-center justify-between">
        <h1 className="text-xl font-bold text-gray-900">테이블 관리</h1>
        <button
          onClick={() => setShowForm(!showForm)}
          className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
          data-testid="table-add-button"
        >
          + 테이블 추가
        </button>
      </div>

      {showForm && (
        <div className="mt-4 rounded-lg border bg-white p-4">
          <div className="grid grid-cols-3 gap-3">
            <input type="number" min={1} placeholder="번호" value={form.number || ''} onChange={(e) => setForm({ ...form, number: Number(e.target.value) })} className="rounded-lg border px-3 py-2 text-sm" data-testid="table-form-number" />
            <input type="text" placeholder="이름" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} className="rounded-lg border px-3 py-2 text-sm" data-testid="table-form-name" />
            <input type="password" placeholder="비밀번호" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} className="rounded-lg border px-3 py-2 text-sm" data-testid="table-form-password" />
          </div>
          <button onClick={handleCreate} disabled={createTable.isPending} className="mt-3 rounded-lg bg-green-600 px-4 py-2 text-sm font-semibold text-white hover:bg-green-700 disabled:bg-gray-300" data-testid="table-form-submit">
            {createTable.isPending ? '생성 중...' : '생성'}
          </button>
        </div>
      )}

      <div className="mt-4 space-y-3">
        {tables.map((table) => (
          <div key={table.id} className="flex items-center justify-between rounded-lg border bg-white p-4" data-testid={`table-row-${table.id}`}>
            <div>
              <span className="text-lg font-bold">테이블 {table.number}</span>
              <span className="ml-2 text-sm text-gray-500">{table.name}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

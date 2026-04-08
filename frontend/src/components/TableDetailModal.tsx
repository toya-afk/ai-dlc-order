import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteOrder } from '../api/orderApi';
import { useTableOrders } from '../hooks/useOrders';
import type { TableDashboardResponse } from '../types/dashboard';

interface TableDetailModalProps {
  table: TableDashboardResponse;
  onClose: () => void;
}

export default function TableDetailModal({ table, onClose }: TableDetailModalProps) {
  const queryClient = useQueryClient();
  const { data: orders = [] } = useTableOrders(table.tableId);
  const [confirmDelete, setConfirmDelete] = useState<number | null>(null);

  const deleteMutation = useMutation({
    mutationFn: (orderId: number) => deleteOrder(orderId),
    onSuccess: () => {
      setConfirmDelete(null);
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50" onClick={onClose}>
      <div
        className="mx-4 max-h-[80vh] w-full max-w-lg overflow-y-auto rounded-xl bg-white p-6 shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-center justify-between">
          <h2 className="text-xl font-bold">테이블 {table.tableNumber}</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600" data-testid="table-detail-close">
            ✕
          </button>
        </div>
        <p className="mt-1 text-lg font-bold text-blue-600">{table.totalAmount.toLocaleString()}원</p>

        <div className="mt-4 space-y-3">
          {orders.map((order) => (
            <div key={order.orderId} className="rounded-lg border p-3" data-testid={`table-detail-order-${order.orderId}`}>
              <div className="flex items-center justify-between">
                <span className="text-sm font-bold">#{order.orderNumber}</span>
                <span className="text-xs text-gray-500">
                  {new Date(order.createdAt).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}
                </span>
              </div>

              <div className="mt-2 text-sm text-gray-600">
                {order.items.map((item) => (
                  <div key={item.menuId}>
                    {item.menuName} x{item.quantity} — {(item.unitPrice * item.quantity).toLocaleString()}원
                  </div>
                ))}
              </div>

              <div className="mt-2 flex justify-between items-center">
                <span className="text-sm font-bold">{order.totalAmount.toLocaleString()}원</span>
                <button
                  onClick={() => setConfirmDelete(order.orderId)}
                  className="text-xs text-red-500 hover:text-red-700"
                  data-testid={`order-delete-${order.orderId}`}
                >
                  삭제
                </button>
              </div>

              {confirmDelete === order.orderId && (
                <div className="mt-2 flex items-center gap-2 rounded bg-red-50 p-2">
                  <span className="text-xs text-red-700">정말 삭제하시겠습니까?</span>
                  <button
                    onClick={() => deleteMutation.mutate(order.orderId)}
                    className="rounded bg-red-500 px-2 py-1 text-xs text-white"
                    data-testid={`order-delete-confirm-${order.orderId}`}
                  >
                    확인
                  </button>
                  <button
                    onClick={() => setConfirmDelete(null)}
                    className="rounded bg-gray-200 px-2 py-1 text-xs text-gray-700"
                  >
                    취소
                  </button>
                </div>
              )}
            </div>
          ))}

          {orders.length === 0 && (
            <p className="py-8 text-center text-gray-400">주문이 없습니다</p>
          )}
        </div>
      </div>
    </div>
  );
}

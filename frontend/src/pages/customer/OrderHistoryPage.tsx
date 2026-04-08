import { useSessionOrders } from '../../hooks/useOrders';
import { useTableStore } from '../../stores/useTableStore';
import OrderCard from '../../components/OrderCard';

export default function OrderHistoryPage() {
  const { tableId } = useTableStore();
  const { data: orders = [], isLoading } = useSessionOrders(tableId);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center p-12">
        <p className="text-gray-400">주문 내역을 불러오는 중...</p>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="flex items-center justify-center p-12">
        <p className="text-gray-400">주문 내역이 없습니다</p>
      </div>
    );
  }

  return (
    <div className="p-4">
      <h1 className="mb-4 text-lg font-bold text-gray-900">주문 내역</h1>
      <div className="space-y-3">
        {orders.map((order) => (
          <OrderCard key={order.orderId} order={order} />
        ))}
      </div>
    </div>
  );
}

import type { OrderResponse } from '../types/order';

interface OrderCardProps {
  order: OrderResponse;
}

export default function OrderCard({ order }: OrderCardProps) {
  const time = new Date(order.createdAt).toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <div className="rounded-lg border bg-white p-4" data-testid={`order-card-${order.orderId}`}>
      <div className="flex items-center justify-between">
        <span className="text-sm font-bold text-gray-900">#{order.orderNumber}</span>
        <span className="text-xs text-gray-500">{time}</span>
      </div>
      <div className="mt-2 text-sm text-gray-600">
        {order.items.map((item) => (
          <span key={item.menuId} className="mr-2">
            {item.menuName} x{item.quantity}
          </span>
        ))}
      </div>
      <div className="mt-2 text-right text-sm font-bold text-gray-900">
        {order.totalAmount.toLocaleString()}원
      </div>
    </div>
  );
}

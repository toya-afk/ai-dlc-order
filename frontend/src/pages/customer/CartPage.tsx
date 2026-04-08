import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useCartStore, useCartTotalAmount } from '../../stores/useCartStore';
import { useTableStore } from '../../stores/useTableStore';
import { useCreateOrder } from '../../hooks/useCreateOrder';

export default function CartPage() {
  const navigate = useNavigate();
  const { items, updateQuantity, removeItem, clear } = useCartStore();
  const totalAmount = useCartTotalAmount();
  const { tableId } = useTableStore();
  const createOrder = useCreateOrder();
  const [error, setError] = useState<string | null>(null);

  const handleOrder = () => {
    if (!tableId || items.length === 0) return;
    setError(null);

    createOrder.mutate(
      {
        tableId,
        items: items.map((i) => ({ menuId: i.menuId, quantity: i.quantity })),
      },
      {
        onSuccess: (data) => {
          clear();
          navigate(`/customer/order-success?orderId=${data.orderId}&orderNumber=${data.orderNumber}`, { replace: true });
        },
        onError: () => {
          setError('주문에 실패했습니다. 다시 시도해주세요.');
        },
      },
    );
  };

  if (items.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center gap-4 p-12">
        <p className="text-lg text-gray-400">장바구니가 비어있습니다</p>
        <Link
          to="/customer/menu"
          className="rounded-lg bg-blue-600 px-6 py-3 text-sm font-semibold text-white hover:bg-blue-700"
          data-testid="cart-go-to-menu"
        >
          메뉴 보러가기
        </Link>
      </div>
    );
  }

  return (
    <div className="pb-28">
      <div className="px-4 pt-4">
        <h1 className="text-lg font-bold text-gray-900">장바구니</h1>
      </div>

      {/* 장바구니 아이템 목록 */}
      <ul className="mt-3 divide-y">
        {items.map((item) => (
          <li key={item.menuId} className="flex items-center gap-3 px-4 py-3" data-testid={`cart-item-${item.menuId}`}>
            {item.imageUrl ? (
              <img src={item.imageUrl} alt={item.menuName} className="h-16 w-16 rounded-lg object-cover" />
            ) : (
              <div className="flex h-16 w-16 items-center justify-center rounded-lg bg-gray-100 text-xs text-gray-400">
                No Img
              </div>
            )}

            <div className="flex-1">
              <p className="text-sm font-semibold text-gray-900">{item.menuName}</p>
              <p className="text-sm text-gray-500">{item.price.toLocaleString()}원</p>
            </div>

            {/* 수량 조절 */}
            <div className="flex items-center gap-2">
              <button
                onClick={() =>
                  item.quantity > 1
                    ? updateQuantity(item.menuId, item.quantity - 1)
                    : removeItem(item.menuId)
                }
                className="flex h-8 w-8 items-center justify-center rounded-full border text-sm font-bold text-gray-600"
                data-testid={`cart-item-minus-${item.menuId}`}
              >
                -
              </button>
              <span className="min-w-[1.5rem] text-center text-sm font-bold">{item.quantity}</span>
              <button
                onClick={() => updateQuantity(item.menuId, item.quantity + 1)}
                className="flex h-8 w-8 items-center justify-center rounded-full border text-sm font-bold text-gray-600"
                data-testid={`cart-item-plus-${item.menuId}`}
              >
                +
              </button>
            </div>

            {/* 소계 + 삭제 */}
            <div className="flex flex-col items-end gap-1">
              <span className="text-sm font-bold text-gray-900">
                {(item.price * item.quantity).toLocaleString()}원
              </span>
              <button
                onClick={() => removeItem(item.menuId)}
                className="text-xs text-red-500 hover:text-red-700"
                data-testid={`cart-item-remove-${item.menuId}`}
              >
                삭제
              </button>
            </div>
          </li>
        ))}
      </ul>

      {error && (
        <p className="mx-4 mt-3 text-sm text-red-600" data-testid="cart-order-error">{error}</p>
      )}

      {/* 하단 고정 — 총 금액 + 주문하기 */}
      <div className="fixed bottom-0 left-0 right-0 border-t bg-white px-4 py-4 shadow-lg">
        <div className="mb-2 flex items-center justify-between">
          <span className="text-base font-medium text-gray-700">총 금액</span>
          <span className="text-xl font-bold text-gray-900">{totalAmount.toLocaleString()}원</span>
        </div>
        <button
          onClick={handleOrder}
          disabled={createOrder.isPending}
          className="w-full rounded-xl bg-blue-600 py-4 text-base font-semibold text-white hover:bg-blue-700 disabled:bg-gray-300"
          style={{ minHeight: '48px' }}
          data-testid="cart-order-button"
        >
          {createOrder.isPending ? '주문 중...' : `${totalAmount.toLocaleString()}원 주문하기`}
        </button>
      </div>
    </div>
  );
}

'use client';
import React from 'react';
import { TrashIcon } from '@heroicons/react/24/outline';
import { useCart } from '@/hooks/useCart';

export default function BagPage() {
  const { cartItems, removeFromCart, updateQuantity, getTotalPrice } = useCart();

  const handleUpdateQuantity = (productId: number, newQuantity: number) => {
    if (newQuantity < 1) return;
    updateQuantity(productId, newQuantity);
  };

  const total = getTotalPrice();

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-6">장바구니</h1>
      
      {cartItems.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600">장바구니가 비어있습니다.</p>
        </div>
      ) : (
        <>
          <div className="space-y-4">
            {cartItems.map((item) => (
              <div key={item.productId} className="flex items-center space-x-4 p-4 bg-white rounded-lg shadow">
                <div className="w-20 h-20 bg-gray-100 rounded-md">
                  {item.imageUrl && (
                    <img 
                      src={item.imageUrl} 
                      alt={item.productName}
                      className="w-full h-full object-cover rounded-md"
                    />
                  )}
                </div>
                <div className="flex-1">
                  <h3 className="font-medium">{item.productName}</h3>
                  <p className="font-medium">₩{item.price.toLocaleString()}</p>
                </div>
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => handleUpdateQuantity(item.productId, item.quantity - 1)}
                    className="w-8 h-8 flex items-center justify-center border rounded-md"
                  >
                    -
                  </button>
                  <span className="w-8 text-center">{item.quantity}</span>
                  <button
                    onClick={() => handleUpdateQuantity(item.productId, item.quantity + 1)}
                    className="w-8 h-8 flex items-center justify-center border rounded-md"
                  >
                    +
                  </button>
                  <button
                    onClick={() => removeFromCart(item.productId)}
                    className="ml-4 p-2 text-red-500"
                  >
                    <TrashIcon className="w-5 h-5" />
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="mt-6 p-4 bg-white rounded-lg shadow">
            <div className="flex justify-between mb-2">
              <span>상품 금액</span>
              <span>₩{total.toLocaleString()}</span>
            </div>
            <div className="flex justify-between mb-2">
              <span>배송비</span>
              <span>₩3,000</span>
            </div>
            <div className="border-t pt-2 mt-2">
              <div className="flex justify-between font-bold">
                <span>총 결제금액</span>
                <span>₩{(total + 3000).toLocaleString()}</span>
              </div>
            </div>
          </div>

          <button className="w-full bg-blue-600 text-white py-3 rounded-md mt-6 hover:bg-blue-700">
            주문하기
          </button>
        </>
      )}
    </div>
  );
} 
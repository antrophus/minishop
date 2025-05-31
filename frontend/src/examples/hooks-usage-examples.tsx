/**
 * API 기반 useCart와 useFavorites 훅 사용 예제
 * 
 * 이 파일은 새롭게 업데이트된 useCart와 useFavorites 훅의 사용법을 보여줍니다.
 * localStorage 대신 실제 백엔드 API를 사용하여 데이터를 관리합니다.
 */

import { useCart, useFavorites } from '@/hooks';
import type { Product } from '@/lib/api/types';

// 예제 상품 데이터
const sampleProduct: Product = {
  id: 1,
  name: "샘플 상품",
  price: 25000,
  stockQuantity: 10,
  mainImage: {
    url: "/images/sample.jpg",
    alt: "샘플 상품 이미지"
  }
};

/**
 * 장바구니 사용 예제 컴포넌트
 */
export function CartExample() {
  const {
    cartItems,
    isLoading,
    error,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getTotalItems,
    getTotalPrice,
    refreshCart
  } = useCart();

  const handleAddToCart = async () => {
    try {
      await addToCart(sampleProduct, 2);
      console.log('상품이 장바구니에 추가되었습니다.');
    } catch (error) {
      console.error('장바구니 추가 실패:', error);
    }
  };

  const handleUpdateQuantity = async (cartItemId: number) => {
    try {
      await updateQuantity(cartItemId, 3);
      console.log('수량이 업데이트되었습니다.');
    } catch (error) {
      console.error('수량 업데이트 실패:', error);
    }
  };

  const handleRemoveItem = async (cartItemId: number) => {
    try {
      await removeFromCart(cartItemId);
      console.log('상품이 장바구니에서 제거되었습니다.');
    } catch (error) {
      console.error('상품 제거 실패:', error);
    }
  };
  const handleClearCart = async () => {
    try {
      await clearCart();
      console.log('장바구니가 비워졌습니다.');
    } catch (error) {
      console.error('장바구니 비우기 실패:', error);
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">장바구니 예제</h2>
      
      {/* 로딩 상태 */}
      {isLoading && <div className="text-blue-500">로딩 중...</div>}
      
      {/* 에러 메시지 */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}
      
      {/* 장바구니 정보 */}
      <div className="mb-4">
        <p>총 상품 수: {getTotalItems()}개</p>
        <p>총 가격: ₩{getTotalPrice().toLocaleString()}</p>
      </div>
      
      {/* 버튼들 */}
      <div className="space-x-2 mb-4">
        <button
          onClick={handleAddToCart}
          disabled={isLoading}
          className="px-4 py-2 bg-blue-500 text-white rounded disabled:opacity-50"
        >
          샘플 상품 추가
        </button>
        <button
          onClick={handleClearCart}
          disabled={isLoading}
          className="px-4 py-2 bg-red-500 text-white rounded disabled:opacity-50"
        >
          장바구니 비우기
        </button>
        <button
          onClick={refreshCart}
          disabled={isLoading}
          className="px-4 py-2 bg-gray-500 text-white rounded disabled:opacity-50"
        >
          새로고침
        </button>
      </div>
      
      {/* 장바구니 아이템 목록 */}
      <div className="space-y-2">
        {cartItems.map((item) => (
          <div key={item.id} className="border p-3 rounded">
            <div className="flex justify-between items-center">
              <div>
                <h3 className="font-medium">{item.productName}</h3>
                <p className="text-sm text-gray-600">
                  ₩{item.price.toLocaleString()} × {item.quantity}
                </p>
              </div>

              <div className="space-x-2">
                <button
                  onClick={() => handleUpdateQuantity(item.id)}
                  disabled={isLoading}
                  className="px-2 py-1 bg-yellow-500 text-white rounded text-sm disabled:opacity-50"
                >
                  수량 변경 (3개)
                </button>
                <button
                  onClick={() => handleRemoveItem(item.id)}
                  disabled={isLoading}
                  className="px-2 py-1 bg-red-500 text-white rounded text-sm disabled:opacity-50"
                >
                  제거
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

/**
 * 즐겨찾기 사용 예제 컴포넌트
 */
export function FavoritesExample() {
  const {
    favorites,
    isLoading,
    error,
    addToFavorites,
    removeFromFavorites,
    toggleFavorite,
    isFavorite,
    refreshFavorites
  } = useFavorites();

  const handleAddToFavorites = async () => {
    try {
      await addToFavorites(sampleProduct.id);
      console.log('상품이 즐겨찾기에 추가되었습니다.');
    } catch (error) {
      console.error('즐겨찾기 추가 실패:', error);
    }
  };

  const handleRemoveFromFavorites = async () => {
    try {
      await removeFromFavorites(sampleProduct.id);
      console.log('상품이 즐겨찾기에서 제거되었습니다.');
    } catch (error) {
      console.error('즐겨찾기 제거 실패:', error);
    }
  };

  const handleToggleFavorite = async () => {
    try {
      await toggleFavorite(sampleProduct.id);
      console.log('즐겨찾기 상태가 토글되었습니다.');
    } catch (error) {
      console.error('즐겨찾기 토글 실패:', error);
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">즐겨찾기 예제</h2>
      
      {/* 로딩 상태 */}
      {isLoading && <div className="text-blue-500">로딩 중...</div>}
      
      {/* 에러 메시지 */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}
      
      {/* 즐겨찾기 정보 */}
      <div className="mb-4">
        <p>총 즐겨찾기 수: {favorites.length}개</p>
        <p>샘플 상품 즐겨찾기 상태: {isFavorite(sampleProduct.id) ? '❤️ 즐겨찾기' : '🤍 일반'}</p>
      </div>
      
      {/* 버튼들 */}
      <div className="space-x-2 mb-4">
        <button
          onClick={handleAddToFavorites}
          disabled={isLoading}
          className="px-4 py-2 bg-red-500 text-white rounded disabled:opacity-50"
        >
          샘플 상품 즐겨찾기 추가
        </button>
        <button
          onClick={handleRemoveFromFavorites}
          disabled={isLoading}
          className="px-4 py-2 bg-gray-500 text-white rounded disabled:opacity-50"
        >
          샘플 상품 즐겨찾기 제거
        </button>
        <button
          onClick={handleToggleFavorite}
          disabled={isLoading}
          className="px-4 py-2 bg-purple-500 text-white rounded disabled:opacity-50"
        >
          즐겨찾기 토글
        </button>
        <button
          onClick={refreshFavorites}
          disabled={isLoading}
          className="px-4 py-2 bg-gray-500 text-white rounded disabled:opacity-50"
        >
          새로고침
        </button>
      </div>
      
      {/* 즐겨찾기 목록 */}
      <div className="space-y-2">
        {favorites.map((item) => (
          <div key={item.id} className="border p-3 rounded">
            <div className="flex justify-between items-center">
              <div>
                <p className="font-medium">상품 ID: {item.productId}</p>
                <p className="text-sm text-gray-600">추가일: {new Date(item.addedAt).toLocaleDateString()}</p>
              </div>
              <button
                onClick={() => removeFromFavorites(item.productId)}
                disabled={isLoading}
                className="px-2 py-1 bg-red-500 text-white rounded text-sm disabled:opacity-50"
              >
                제거
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

'use client';
import React, { useState, useEffect } from 'react';
import { TrashIcon, ShoppingBagIcon } from '@heroicons/react/24/outline';
import { CategoryTabs } from '@/components/categories';
import { CategoryUtils } from '@/lib/categories';
import { useFavorites } from '@/hooks/useFavorites';
import { useCart } from '@/hooks/useCart';
import { productsApi, type Product } from '@/lib/api';

export default function WishlistPage() {
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const { favorites, removeFromFavorites } = useFavorites();
  const { addToCart } = useCart();

  // 즐겨찾기된 상품들 로드
  useEffect(() => {
    loadFavoriteProducts();
  }, [favorites]);

  const loadFavoriteProducts = async () => {
    if (favorites.length === 0) {
      setProducts([]);
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      const allProducts: Product[] = [];
      
      // 각 즐겨찾기 상품에 대해 API 호출
      for (const favorite of favorites) {
        try {
          const response = await productsApi.getProduct(favorite.productId);
          if (response.success && response.data) {
            allProducts.push(response.data);
          }
        } catch (error) {
          console.error(`상품 ${favorite.productId} 로딩 실패:`, error);
        }
      }
      
      setProducts(allProducts);
    } catch (error) {
      console.error('즐겨찾기 상품 로딩 오류:', error);
    } finally {
      setLoading(false);
    }
  };

  // 선택된 카테고리에 따라 상품 필터링
  const filteredProducts = selectedCategory === 'all' 
    ? products 
    : products.filter(product => {
        const categoryId = mapBackendCategoryToFrontend(product.category?.id);
        return categoryId === selectedCategory;
      });

  // 백엔드 카테고리 ID를 프론트엔드 카테고리 ID로 매핑
  const mapBackendCategoryToFrontend = (backendCategoryId?: number): string => {
    const mapping: Record<number, string> = {
      1: 'skincare',
      2: 'haircare', 
      3: 'health',
      4: 'lifestyle',
    };
    return backendCategoryId ? mapping[backendCategoryId] || 'all' : 'all';
  };

  const handleAddToCart = (product: Product) => {
    addToCart(product, 1);
    alert(`${product.name}이(가) 장바구니에 추가되었습니다.`);
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-6">위시리스트</h1>

      {/* 카테고리 필터 */}
      <CategoryTabs
        selectedCategory={selectedCategory}
        onCategoryChange={setSelectedCategory}
        className="mb-6"
      />

      {loading ? (
        <div className="flex justify-center items-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          <span className="ml-2">즐겨찾기 상품을 불러오는 중...</span>
        </div>
      ) : filteredProducts.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600">
            {selectedCategory === 'all' 
              ? '위시리스트가 비어있습니다.' 
              : `${CategoryUtils.getCategoryName(selectedCategory)} 카테고리에 저장된 상품이 없습니다.`
            }
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-2 gap-4">
          {filteredProducts.map((product) => (
            <div key={product.id} className="bg-white rounded-lg overflow-hidden shadow">
              <div className="aspect-square bg-gray-100">
                {product.mainImage ? (
                  <img 
                    src={product.mainImage.url} 
                    alt={product.mainImage.alt}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <div className="w-full h-full flex items-center justify-center text-gray-400">
                    No Image
                  </div>
                )}
              </div>
              <div className="p-4">
                <p className="text-xs text-gray-500 mb-1">
                  {product.category?.name || '미분류'}
                </p>
                <h3 className="font-medium">{product.name}</h3>
                <p className="text-sm text-gray-600 mt-1">₩{(product.price || 0).toLocaleString()}</p>
                <div className="mt-4 flex space-x-2">
                  <button
                    onClick={() => handleAddToCart(product)}
                    className="flex-1 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 flex items-center justify-center"
                  >
                    <ShoppingBagIcon className="w-5 h-5 mr-1" />
                    <span>담기</span>
                  </button>
                  <button
                    onClick={() => removeFromFavorites(product.id)}
                    className="p-2 text-red-500 border border-red-500 rounded-md hover:bg-red-50"
                  >
                    <TrashIcon className="w-5 h-5" />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
} 
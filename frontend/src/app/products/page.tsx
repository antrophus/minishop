'use client';

import React, { useState, useEffect } from 'react';
import { CategoryTabs } from '@/components/categories';
import { CategoryUtils } from '@/lib/categories';
import { productsApi, type Product } from '@/lib/api';
import ProductCard from '@/components/ProductCard';

export default function ProductListPage() {
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  // 상품 목록 로딩
  useEffect(() => {
    loadProducts();
  }, [selectedCategory]);

  const loadProducts = async () => {
    try {
      setLoading(true);
      setError('');

      const params: any = {
        page: 0,
        size: 20,
        sortBy: 'createdAt',
        sortDir: 'desc',
        activeOnly: true,
      };

      // 카테고리가 'all'이 아닌 경우에만 categoryId 추가
      if (selectedCategory !== 'all') {
        // 프론트엔드 카테고리 ID를 백엔드 카테고리 ID로 매핑
        const categoryId = mapFrontendCategoryToBackend(selectedCategory);
        if (categoryId) {
          params.categoryId = categoryId;
        }
      }

      const response = await productsApi.getProducts(params);

      if (response.success && response.data) {
        // 백엔드 응답 구조에 맞게 수정
        const products = response.data.data?.products || response.data.products;
        setProducts(products);
      } else {
        setError(response.message || '상품을 불러오는데 실패했습니다.');
      }
    } catch (err) {
      setError('네트워크 오류가 발생했습니다.');
      console.error('상품 로딩 오류:', err);
    } finally {
      setLoading(false);
    }
  };

  // 프론트엔드 카테고리 ID를 백엔드 카테고리 ID로 매핑
  const mapFrontendCategoryToBackend = (frontendCategoryId: string): number | null => {
    const mapping: Record<string, number> = {
      'skincare': 1,
      'haircare': 2,
      'health': 3,
      'lifestyle': 4,
    };
    return mapping[frontendCategoryId] || null;
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">제품 목록</h1>
      
      {/* 카테고리 탭 */}
      <CategoryTabs
        selectedCategory={selectedCategory}
        onCategoryChange={setSelectedCategory}
        className="mb-8"
      />
      
      {/* 선택된 카테고리 표시 */}
      <p className="mb-4 text-gray-600">
        현재 카테고리: {CategoryUtils.getCategoryName(selectedCategory)}
      </p>
      
      {/* 로딩 상태 */}
      {loading && (
        <div className="flex justify-center items-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          <span className="ml-2">상품을 불러오는 중...</span>
        </div>
      )}

      {/* 에러 상태 */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}
      
      {/* 제품 목록 */}
      {!loading && !error && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {products && products.length > 0 ? (
            products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <p className="text-gray-600">해당 카테고리에 상품이 없습니다.</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

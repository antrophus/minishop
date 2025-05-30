'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { CategoryTabs, SearchBar } from '@/components/categories';
import { CategoryUtils } from '@/lib/categories';
import { productsApi, type Product } from '@/lib/api';

export default function ShopPage() {
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  // 페이지 로드 시 초기 데이터 로딩
  useEffect(() => {
    loadProducts();
  }, []);

  // 카테고리 변경 시 상품 재로딩
  useEffect(() => {
    if (!searchQuery) { // 검색 중이 아닐 때만
      loadProducts();
    }
  }, [selectedCategory]);

  // 상품 목록 로딩
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

      // 카테고리 필터 적용
      if (selectedCategory !== 'all') {
        const categoryId = mapFrontendCategoryToBackend(selectedCategory);
        if (categoryId) {
          params.categoryId = categoryId;
        }
      }

      const response = await productsApi.getProducts(params);
      console.log('상품 API 응답:', response);

      if (response.success && response.data) {
        // 백엔드 응답 구조에 맞게 수정
        const products = response.data.data?.products || response.data.products;
        console.log('상품 데이터:', products);
        setProducts(products);
      } else {
        console.error('상품 API 실패:', response);
        setError(response.message || '상품을 불러오는데 실패했습니다.');
      }
    } catch (err) {
      setError('네트워크 오류가 발생했습니다.');
      console.error('상품 로딩 오류:', err);
    } finally {
      setLoading(false);
    }
  };
  // 검색 처리
  const handleSearch = async (query: string, category: string) => {
    try {
      setLoading(true);
      setError('');
      setSearchQuery(query);
      setSelectedCategory(category);

      if (query.trim()) {
        // 검색 API 호출
        const response = await productsApi.searchProducts(query.trim());
        if (response.success && response.data) {
          let searchResults = response.data.products;
          
          // 카테고리 필터 적용 (검색 결과에서)
          if (category !== 'all') {
            const categoryId = mapFrontendCategoryToBackend(category);
            if (categoryId) {
              searchResults = searchResults.filter(product => 
                product.category && product.category.id === categoryId
              );
            }
          }
          
          setProducts(searchResults);
        } else {
          setError(response.message || '검색에 실패했습니다.');
        }
      } else {
        // 검색어가 없으면 일반 상품 목록 로딩
        await loadProducts();
      }
    } catch (err) {
      setError('검색 중 오류가 발생했습니다.');
      console.error('검색 오류:', err);
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
    <div className="min-h-screen bg-white">
      {/* 검색 바 */}
      <SearchBar
        onSearch={handleSearch}
        placeholder="상품 검색"
        showCategoryFilter={false}
        className="sticky top-0 z-10"
      />

      {/* 카테고리 필터 */}
      <div className="px-4 py-3">
        <CategoryTabs
          selectedCategory={selectedCategory}
          onCategoryChange={setSelectedCategory}
          className="mb-4"
        />
      </div>
      {/* 상품 목록 */}
      <div className="grid grid-cols-2 gap-4 p-4">
        {loading ? (
          <div className="col-span-2 flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            <span className="ml-2">상품을 불러오는 중...</span>
          </div>
        ) : error ? (
          <div className="col-span-2 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
          </div>
        ) : !products || products.length === 0 ? (
          <div className="col-span-2 text-center py-8">
            <p className="text-gray-600">검색 결과가 없습니다.</p>
          </div>
        ) : (
          (products || []).map((product) => (
            <Link key={product.id} href={`/product/${product.id}`}>
              <div className="bg-white rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-shadow">
                <div className="aspect-square bg-gray-100 relative">
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
                  {product.discount && (
                    <div className="absolute top-2 left-2 bg-red-500 text-white px-2 py-1 rounded text-sm">
                      -{product.discount.rate}%
                    </div>
                  )}
                </div>
                <div className="p-3">
                  <p className="text-xs text-gray-500">
                    {product.category?.name || '미분류'}
                  </p>
                  <h3 className="font-medium mt-1 line-clamp-2">{product.name}</h3>
                  <div className="mt-1">
                    <p className="text-sm font-semibold">₩{(product.price || 0).toLocaleString()}</p>
                    {product.originalPrice && (
                      <p className="text-xs text-gray-500 line-through">
                        ₩{product.originalPrice.toLocaleString()}
                      </p>
                    )}
                  </div>
                  <div className="mt-1 flex gap-1">
                    {product.featured && (
                      <span className="bg-yellow-100 text-yellow-800 text-xs px-1 py-0.5 rounded">추천</span>
                    )}
                    {product.bestseller && (
                      <span className="bg-green-100 text-green-800 text-xs px-1 py-0.5 rounded">베스트</span>
                    )}
                    {product.newArrival && (
                      <span className="bg-blue-100 text-blue-800 text-xs px-1 py-0.5 rounded">신상품</span>
                    )}
                  </div>
                </div>
              </div>
            </Link>
          ))
        )}
      </div>
    </div>
  );
}
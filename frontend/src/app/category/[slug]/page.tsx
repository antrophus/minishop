'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { CategoryTabs } from '@/components/categories';
import { ChevronLeftIcon } from '@heroicons/react/24/outline';
import { productsApi, categoriesApi, type Product, type Category } from '@/lib/api';

export default function CategoryPage() {
  const params = useParams();
  const categorySlug = params.slug as string;
  
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [products, setProducts] = useState<Product[]>([]);
  const [category, setCategory] = useState<Category | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadCategoryAndProducts();
  }, [categorySlug]);

  useEffect(() => {
    if (selectedCategory !== 'all') {
      loadProductsByCategory();
    }
  }, [selectedCategory]);

  const loadCategoryAndProducts = async () => {
    try {
      setLoading(true);
      setError('');

      const categoryId = mapSlugToCategoryId(categorySlug);
      
      if (categoryId) {
        const response = await productsApi.getProductsByCategory(categoryId, {
          page: 0,
          size: 20,
          sortBy: 'createdAt',
          sortDir: 'desc'
        });

        if (response.success && response.data) {
          setCategory(response.data.category);
          setProducts(response.data.products);
          const frontendCategoryId = mapBackendCategoryToFrontend(categoryId);
          if (frontendCategoryId) {
            setSelectedCategory(frontendCategoryId);
          }
        } else {
          setError(response.message || '카테고리를 찾을 수 없습니다.');
        }
      } else if (categorySlug === 'all') {
        const response = await productsApi.getProducts({
          page: 0,
          size: 20,
          activeOnly: true
        });

        if (response.success && response.data) {
          setProducts(response.data.products);
          setCategory({ id: 0, name: '전체 상품' } as Category);
          setSelectedCategory('all');
        }
      }
    } catch (err) {
      setError('데이터를 불러오는 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };
  const loadProductsByCategory = async () => {
    try {
      setLoading(true);
      const categoryId = mapFrontendCategoryToBackend(selectedCategory);
      
      if (categoryId) {
        const response = await productsApi.getProductsByCategory(categoryId);
        if (response.success && response.data) {
          setProducts(response.data.products);
          setCategory(response.data.category);
        }
      }
    } catch (err) {
      console.error('카테고리별 상품 로딩 오류:', err);
    } finally {
      setLoading(false);
    }
  };

  const mapSlugToCategoryId = (slug: string): number | null => {
    const mapping: Record<string, number> = {
      'skincare': 1, 'haircare': 2, 'health': 3, 'lifestyle': 4,
    };
    return mapping[slug] || null;
  };

  const mapBackendCategoryToFrontend = (backendId: number): string | null => {
    const mapping: Record<number, string> = {
      1: 'skincare', 2: 'haircare', 3: 'health', 4: 'lifestyle',
    };
    return mapping[backendId] || null;
  };

  const mapFrontendCategoryToBackend = (frontendId: string): number | null => {
    const mapping: Record<string, number> = {
      'skincare': 1, 'haircare': 2, 'health': 3, 'lifestyle': 4,
    };
    return mapping[frontendId] || null;
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        <span className="ml-4">데이터를 불러오는 중...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-gray-800 mb-4">{error}</h1>
          <Link href="/shop" className="text-blue-600 hover:underline">
            쇼핑 페이지로 돌아가기
          </Link>
        </div>
      </div>
    );
  }
  return (
    <div className="min-h-screen bg-white">
      {/* 헤더 */}
      <div className="sticky top-0 bg-white border-b border-gray-200 z-10">
        <div className="flex items-center justify-between p-4">
          <div className="flex items-center space-x-4">
            <Link href="/shop" className="p-2">
              <ChevronLeftIcon className="w-6 h-6" />
            </Link>
            <div>
              <h1 className="text-xl font-bold">
                {category ? category.name : '전체 상품'}
              </h1>
            </div>
          </div>
        </div>
      </div>

      {/* 카테고리 탭 */}
      <div className="px-4 py-3 bg-white border-b border-gray-100">
        <CategoryTabs
          selectedCategory={selectedCategory}
          onCategoryChange={setSelectedCategory}
        />
      </div>

      {/* 상품 목록 */}
      <div className="p-4">
        {!products || products.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-4xl mb-4">📦</div>
            <h3 className="text-lg font-medium text-gray-800 mb-2">상품이 없습니다</h3>
            <p className="text-gray-600">이 카테고리에 등록된 상품이 없습니다.</p>
          </div>
        ) : (
          <>
            <div className="flex items-center justify-between mb-4">
              <p className="text-sm text-gray-600">총 {products.length}개의 상품</p>
            </div>
            
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
              {(products || []).map((product) => (
                <Link key={product.id} href={`/product/${product.id}`}>
                  <div className="bg-white rounded-lg overflow-hidden border hover:shadow-md transition-shadow">
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
                    <div className="p-3">
                      <p className="text-xs text-gray-500 mb-1">{product.category?.name || '미분류'}</p>
                      <h3 className="font-medium text-sm line-clamp-2">{product.name}</h3>
                      <p className="text-sm font-semibold mt-2">₩{(product.price || 0).toLocaleString()}</p>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
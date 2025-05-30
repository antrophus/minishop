// 카테고리 타입 정의
export interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  icon?: string;
  color?: string;
}

// 제품 타입 (기본)
export interface Product {
  id: number;
  name: string;
  price: number;
  categoryId: string;
  image?: string;
  description?: string;
  inStock?: boolean;
}

// 카테고리 상수 정의
export const CATEGORIES: Category[] = [
  {
    id: 'all',
    name: '전체',
    slug: 'all',
    description: '모든 제품',
    icon: '🛍️',
    color: '#6B7280'
  },
  {
    id: 'skincare',
    name: '스킨케어',
    slug: 'skincare',
    description: '피부 관리 제품',
    icon: '🧴',
    color: '#F59E0B'
  },
  {
    id: 'haircare',
    name: '헤어케어',
    slug: 'haircare',
    description: '모발 관리 제품',
    icon: '💇‍♀️',
    color: '#8B5CF6'
  },
  {
    id: 'health',
    name: '건강',
    slug: 'health',
    description: '건강 관리 제품',
    icon: '💊',
    color: '#10B981'
  },
  {
    id: 'lifestyle',
    name: '생활',
    slug: 'lifestyle',
    description: '생활용품',
    icon: '🏠',
    color: '#3B82F6'
  }
];

// 카테고리 ID만 추출
export const CATEGORY_IDS = CATEGORIES.map(cat => cat.id);

// 카테고리 이름만 추출
export const CATEGORY_NAMES = CATEGORIES.map(cat => cat.name);

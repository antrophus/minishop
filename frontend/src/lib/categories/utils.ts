import { CATEGORIES, Category, Product } from './types';

/**
 * 카테고리 관련 유틸리티 함수들
 */
export class CategoryUtils {
  
  /**
   * 모든 카테고리 반환
   */
  static getAllCategories(): Category[] {
    return CATEGORIES;
  }

  /**
   * ID로 카테고리 찾기
   */
  static getCategoryById(id: string): Category | undefined {
    return CATEGORIES.find(cat => cat.id === id);
  }

  /**
   * slug로 카테고리 찾기
   */
  static getCategoryBySlug(slug: string): Category | undefined {
    return CATEGORIES.find(cat => cat.slug === slug);
  }

  /**
   * 카테고리 이름 반환
   */
  static getCategoryName(id: string): string {
    const category = this.getCategoryById(id);
    return category?.name || '알 수 없음';
  }

  /**
   * 제품을 카테고리별로 필터링
   */
  static filterProductsByCategory(products: Product[], categoryId: string): Product[] {
    if (categoryId === 'all') {
      return products;
    }
    return products.filter(product => product.categoryId === categoryId);
  }

  /**
   * 카테고리별 제품 개수 계산
   */
  static getProductCountByCategory(products: Product[], categoryId: string): number {
    return this.filterProductsByCategory(products, categoryId).length;
  }

  /**
   * 유효한 카테고리 ID인지 확인
   */
  static isValidCategoryId(id: string): boolean {
    return CATEGORIES.some(cat => cat.id === id);
  }

  /**
   * 카테고리 옵션 (select box용)
   */
  static getCategoryOptions() {
    return CATEGORIES.map(cat => ({
      value: cat.id,
      label: cat.name
    }));
  }
}

import { useState, useEffect, useCallback } from 'react';
import { wishlistApi } from '@/lib/api';
import { authUtils } from '@/lib/api/auth';
import type { WishlistItem as ApiWishlistItem } from '@/lib/api/types';

export interface FavoriteItem {
  id: number;
  productId: number;
  addedAt: string;
}

export interface UseFavoritesReturn {
  favorites: FavoriteItem[];
  isLoading: boolean;
  error: string | null;
  addToFavorites: (productId: number) => Promise<void>;
  removeFromFavorites: (productId: number) => Promise<void>;
  toggleFavorite: (productId: number) => Promise<void>;
  isFavorite: (productId: number) => boolean;
  refreshFavorites: () => Promise<void>;
}

// API WishlistItem을 local FavoriteItem으로 변환
const transformApiWishlistItem = (apiItem: ApiWishlistItem): FavoriteItem => ({
  id: apiItem.id,
  productId: apiItem.product.id,
  addedAt: apiItem.createdAt,
});

export const useFavorites = (): UseFavoritesReturn => {
  const [favorites, setFavorites] = useState<FavoriteItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // 현재 사용자 정보 가져오기
  const getCurrentUser = useCallback(() => {
    return authUtils.getUserFromToken();
  }, []);

  // 에러 처리 헬퍼
  const handleError = useCallback((error: any, defaultMessage: string) => {
    console.error('즐겨찾기 오류:', error);
    const errorMessage = error?.response?.data?.message || error?.message || defaultMessage;
    setError(errorMessage);
  }, []);

  // 즐겨찾기 데이터 로드
  const loadFavorites = useCallback(async () => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setFavorites([]);
      return;
    }

    // userId가 유효한 숫자인지 확인
    const userId = parseInt(user.userId);
    if (isNaN(userId)) {
      console.error('유효하지 않은 사용자 ID:', user.userId);
      setFavorites([]);
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await wishlistApi.getWishlist(userId);
      
      // 응답 데이터 안전하게 처리
      if (response.success && response.data && Array.isArray(response.data)) {
        const transformedItems = response.data.map(transformApiWishlistItem);
        setFavorites(transformedItems);
      } else {
        console.warn('유효하지 않은 위시리스트 응답:', response);
        setFavorites([]);
      }
    } catch (error) {
      handleError(error, '즐겨찾기를 불러오는데 실패했습니다.');
      setFavorites([]);
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, handleError]);

  // 컴포넌트 마운트 시 즐겨찾기 로드
  useEffect(() => {
    loadFavorites();
  }, [loadFavorites]);

  // 즐겨찾기 새로고침
  const refreshFavorites = useCallback(async () => {
    await loadFavorites();
  }, [loadFavorites]);

  // 즐겨찾기 추가
  const addToFavorites = useCallback(async (productId: number) => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setError('로그인이 필요합니다.');
      return;
    }

    const userId = parseInt(user.userId);
    if (isNaN(userId)) {
      setError('유효하지 않은 사용자 정보입니다.');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      await wishlistApi.addToWishlist(userId, productId);
      await loadFavorites();
    } catch (error) {
      handleError(error, '즐겨찾기에 추가하는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, loadFavorites, handleError]);

  // 즐겨찾기 제거
  const removeFromFavorites = useCallback(async (productId: number) => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setError('로그인이 필요합니다.');
      return;
    }

    const userId = parseInt(user.userId);
    if (isNaN(userId)) {
      setError('유효하지 않은 사용자 정보입니다.');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      await wishlistApi.removeFromWishlist(userId, productId);
      await loadFavorites();
    } catch (error) {
      handleError(error, '즐겨찾기에서 제거하는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, loadFavorites, handleError]);

  // 즐겨찾기 토글
  const toggleFavorite = useCallback(async (productId: number) => {
    if (isFavorite(productId)) {
      await removeFromFavorites(productId);
    } else {
      await addToFavorites(productId);
    }
  }, [addToFavorites, removeFromFavorites]);

  // 즐겨찾기 여부 확인
  const isFavorite = useCallback((productId: number) => {
    return favorites.some(item => item.productId === productId);
  }, [favorites]);

  return {
    favorites,
    isLoading,
    error,
    addToFavorites,
    removeFromFavorites,
    toggleFavorite,
    isFavorite,
    refreshFavorites,
  };
};

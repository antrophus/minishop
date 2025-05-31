/**
 * 장바구니 & 위시리스트 API 사용 예제
 * 개발 및 테스트용 예제 코드
 */

import { cartApi, wishlistApi, getCurrentUserId, withApiWrapper } from '../lib/api';

/**
 * 장바구니 API 사용 예제
 */
export const cartExamples = {
  /**
   * 장바구니에 상품 추가 예제
   */
  addToCart: async (productId: number, quantity: number = 1) => {
    const userId = getCurrentUserId();
    if (!userId) {
      console.error('로그인이 필요합니다.');
      return;
    }

    const result = await withApiWrapper(
      () => cartApi.addItem(userId, productId, quantity),
      {
        requireAuth: true,
        errorMessage: '상품을 장바구니에 추가하는 중 오류가 발생했습니다.',
        onSuccess: (data) => {
          console.log('✅ 장바구니 추가 성공:', data);
        },
        onError: (error) => {
          console.error('❌ 장바구니 추가 실패:', error);
        }
      }
    );

    return result;
  },

  /**
   * 장바구니 조회 예제
   */
  getCart: async () => {
    const userId = getCurrentUserId();
    if (!userId) {
      console.error('로그인이 필요합니다.');
      return;
    }

    const result = await withApiWrapper(
      () => cartApi.getCart(userId),
      {
        requireAuth: true,
        onSuccess: (data) => {
          console.log('✅ 장바구니 조회 성공:', data);
          console.log(`📦 총 ${data.length}개의 상품이 있습니다.`);
        },
        onError: (error) => {
          console.error('❌ 장바구니 조회 실패:', error);
        }
      }
    );

    return result;
  },
};
  /**
   * 장바구니 수량 수정 예제
   */
  updateQuantity: async (cartItemId: number, quantity: number) => {
    const result = await withApiWrapper(
      () => cartApi.updateQuantity(cartItemId, quantity),
      {
        requireAuth: true,
        onSuccess: (data) => {
          console.log('✅ 수량 업데이트 성공:', data);
        },
        onError: (error) => {
          console.error('❌ 수량 업데이트 실패:', error);
        }
      }
    );

    return result;
  },

  /**
   * 장바구니 아이템 삭제 예제
   */
  removeItem: async (cartItemId: number) => {
    const result = await withApiWrapper(
      () => cartApi.removeItem(cartItemId),
      {
        requireAuth: true,
        onSuccess: () => {
          console.log('✅ 장바구니 아이템 삭제 성공');
        },
        onError: (error) => {
          console.error('❌ 장바구니 아이템 삭제 실패:', error);
        }
      }
    );

    return result;
  },

  /**
   * 장바구니 총 개수 조회 예제
   */
  getItemCount: async () => {
    const userId = getCurrentUserId();
    if (!userId) return { data: 0, error: null, loading: false };

    return await withApiWrapper(
      () => cartApi.getItemCount(userId),
      {
        requireAuth: true,
        onSuccess: (count) => {
          console.log(`🛒 장바구니에 ${count}개의 상품이 있습니다.`);
        }
      }
    );
  },

  /**
   * 장바구니 비우기 예제
   */
  clearCart: async () => {
    const userId = getCurrentUserId();
    if (!userId) return;

    return await withApiWrapper(
      () => cartApi.clearCart(userId),
      {
        requireAuth: true,
        onSuccess: () => {
          console.log('🗑️ 장바구니가 비워졌습니다.');
        }
      }
    );
  }
};

/**
 * 위시리스트 API 사용 예제
 */
export const wishlistExamples = {
  /**
   * 위시리스트에 상품 추가 예제
   */
  addToWishlist: async (productId: number) => {
    const userId = getCurrentUserId();
    if (!userId) {
      console.error('로그인이 필요합니다.');
      return;
    }

    const result = await withApiWrapper(
      () => wishlistApi.addToWishlist(userId, productId),
      {
        requireAuth: true,
        errorMessage: '위시리스트에 추가하는 중 오류가 발생했습니다.',
        onSuccess: (data) => {
          console.log('💖 위시리스트 추가 성공:', data);
        },
        onError: (error) => {
          console.error('❌ 위시리스트 추가 실패:', error);
        }
      }
    );

    return result;
  },

  /**
   * 위시리스트 조회 예제
   */
  getWishlist: async () => {
    const userId = getCurrentUserId();
    if (!userId) {
      console.error('로그인이 필요합니다.');
      return;
    }

    const result = await withApiWrapper(
      () => wishlistApi.getWishlist(userId),
      {
        requireAuth: true,
        onSuccess: (data) => {
          console.log('✅ 위시리스트 조회 성공:', data);
          console.log(`💝 총 ${data.length}개의 찜한 상품이 있습니다.`);
        },
        onError: (error) => {
          console.error('❌ 위시리스트 조회 실패:', error);
        }
      }
    );

    return result;
  },

  /**
   * 위시리스트에서 상품 제거 예제
   */
  removeFromWishlist: async (productId: number) => {
    const userId = getCurrentUserId();
    if (!userId) return;

    return await withApiWrapper(
      () => wishlistApi.removeFromWishlist(userId, productId),
      {
        requireAuth: true,
        onSuccess: () => {
          console.log('💔 위시리스트에서 제거되었습니다.');
        },
        onError: (error) => {
          console.error('❌ 위시리스트 제거 실패:', error);
        }
      }
    );
  },

  /**
   * 상품이 위시리스트에 있는지 확인 예제
   */
  checkInWishlist: async (productId: number) => {
    const userId = getCurrentUserId();
    if (!userId) return { data: false, error: null, loading: false };

    return await withApiWrapper(
      () => wishlistApi.isInWishlist(userId, productId),
      {
        requireAuth: true,
        onSuccess: (isInWishlist) => {
          console.log(`${isInWishlist ? '💖' : '🤍'} 상품이 위시리스트에 ${isInWishlist ? '있습니다' : '없습니다'}.`);
        }
      }
    );
  }
};

/**
 * 통합 사용 예제 - 실제 시나리오
 */
export const integratedExamples = {
  /**
   * 상품 상세페이지에서 사용할 수 있는 통합 함수
   */
  handleProductActions: async (productId: number) => {
    console.log('=== 상품 액션 시작 ===');
    
    // 1. 위시리스트 상태 확인
    const wishlistCheck = await wishlistExamples.checkInWishlist(productId);
    
    // 2. 장바구니에 추가
    const cartResult = await cartExamples.addToCart(productId, 1);
    
    // 3. 장바구니 개수 업데이트
    if (cartResult.data) {
      await cartExamples.getItemCount();
    }
    
    console.log('=== 상품 액션 완료 ===');
  },

  /**
   * 위시리스트 토글 함수
   */
  toggleWishlist: async (productId: number) => {
    const checkResult = await wishlistExamples.checkInWishlist(productId);
    
    if (checkResult.data) {
      // 이미 위시리스트에 있으면 제거
      return await wishlistExamples.removeFromWishlist(productId);
    } else {
      // 없으면 추가
      return await wishlistExamples.addToWishlist(productId);
    }
  },

  /**
   * 장바구니에서 위시리스트로 이동
   */
  moveToWishlist: async (cartItemId: number, productId: number) => {
    console.log('🔄 장바구니에서 위시리스트로 이동 중...');
    
    // 1. 장바구니에서 제거
    const removeResult = await cartExamples.removeItem(cartItemId);
    
    if (removeResult.data !== null) {
      // 2. 위시리스트에 추가
      const addResult = await wishlistExamples.addToWishlist(productId);
      
      if (addResult.data) {
        console.log('✅ 상품이 위시리스트로 이동되었습니다.');
      }
    }
  }
};

/**
 * 디버깅 및 테스트용 함수들
 */
export const debugHelpers = {
  /**
   * 모든 장바구니 & 위시리스트 정보 출력
   */
  showAllData: async () => {
    console.log('=== 전체 데이터 조회 ===');
    
    await cartExamples.getCart();
    await cartExamples.getItemCount();
    await wishlistExamples.getWishlist();
    
    console.log('=== 조회 완료 ===');
  },

  /**
   * 테스트 데이터 추가
   */
  addTestData: async () => {
    console.log('🧪 테스트 데이터 추가 중...');
    
    // 테스트용 상품 ID들 (실제 상품 ID로 변경 필요)
    const testProductIds = [1, 2, 3];
    
    for (const productId of testProductIds) {
      await cartExamples.addToCart(productId, 1);
      await wishlistExamples.addToWishlist(productId);
    }
    
    console.log('✅ 테스트 데이터 추가 완료');
  }
};

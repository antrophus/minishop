스트 기본 기능
      console.log('\n❤️ 3. 위시리스트 기본 기능 테스트');
      await this.testWishlistBasicOperations();
      
      // 4. 복합 시나리오
      console.log('\n🔄 4. 복합 시나리오 테스트');
      await this.testComplexScenarios();
      
      // 5. 에러 처리
      console.log('\n⚠️ 5. 에러 처리 테스트');
      await this.testErrorHandling();
      
    } finally {
      // 테스트 데이터 정리 (종료 후)
      await this.cleanup();
    }
    
    // 결과 출력
    this.printResults();
  }

  /**
   * 테스트 데이터 정리
   */
  private async cleanup(): Promise<void> {
    try {
      console.log('🧹 테스트 데이터 정리 중...');
      await cartApi.clearCart(TEST_CONFIG.userId);
      // 위시리스트는 clearWishlist API가 있다면 사용
      console.log('✅ 테스트 데이터 정리 완료');
    } catch (error) {
      console.warn('⚠️ 테스트 데이터 정리 중 오류:', error);
    }
  }

  /**
   * 테스트 결과 출력
   */
  private printResults(): void {
    console.log('\n' + '='.repeat(60));
    console.log('📊 통합 테스트 결과');
    console.log('='.repeat(60));
    
    console.log(`총 테스트: ${this.totalTests}`);
    console.log(`성공: ${this.passedTests}`);
    console.log(`실패: ${this.totalTests - this.passedTests}`);
    console.log(`성공률: ${((this.passedTests / this.totalTests) * 100).toFixed(1)}%`);
    
    console.log('\n📋 상세 결과:');
    this.results.forEach((result, index) => {
      const status = result.passed ? '✅' : '❌';
      console.log(`${index + 1}. ${status} ${result.testName} (${result.duration}ms)`);
      if (!result.passed && result.error) {
        console.log(`   오류: ${result.error}`);
      }
    });
    
    if (this.passedTests === this.totalTests) {
      console.log('\n🎉 모든 테스트가 성공했습니다!');
      console.log('✅ 장바구니 & 위시리스트 기능이 정상적으로 작동합니다.');
    } else {
      console.log('\n⚠️ 일부 테스트가 실패했습니다.');
      console.log('🔧 실패한 테스트를 확인하고 수정이 필요합니다.');
    }
    
    console.log('='.repeat(60));
  }
}

// 테스트 실행 함수
export async function runIntegrationTests(): Promise<void> {
  const tester = new IntegrationTester();
  await tester.runAllTests();
}

// 브라우저 콘솔에서 직접 실행할 수 있도록 전역 함수로 등록
if (typeof window !== 'undefined') {
  (window as any).runIntegrationTests = runIntegrationTests;
  console.log('💡 브라우저 콘솔에서 runIntegrationTests()를 실행하여 테스트를 시작할 수 있습니다.');
}

# AI Backend

## 구조
- app/main.py: FastAPI 엔트리포인트
- app/api/routes/: 라우트 모듈
- app/core/: 환경설정, 보안 등
- app/services/: 외부 AI 서비스 연동
- app/models/: 데이터 모델
- app/utils/: 유틸리티

## 실행 방법
```bash
conda activate shop-ai
cd ai-backend
uvicorn app.main:app --reload
```

- 환경 변수는 .env 파일에 저장 (예시는 .env.example 참고)
- http://localhost:8000/docs 에서 API 문서 확인

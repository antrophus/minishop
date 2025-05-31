# MyLittleShop API 통합 테스트 스크립트
$baseUrl = "http://localhost:8080/api"
$userId = 1
$productId = 1

Write-Host "🧪 MyLittleShop 통합 테스트 시작..." -ForegroundColor Green

# 1. 서버 연결 테스트
Write-Host "`n📡 1. 서버 연결 테스트..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/products" -Method GET
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ 서버 연결 성공!" -ForegroundColor Green
        Write-Host "📊 상품 목록 조회 성공 (HTTP $($response.StatusCode))" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ 서버 연결 실패: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. 장바구니 조회 테스트
Write-Host "`n🛒 2. 장바구니 조회 테스트..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/cart/$userId" -Method GET
    Write-Host "✅ 장바구니 조회 성공 (HTTP $($response.StatusCode))" -ForegroundColor Green
    $cartData = $response.Content | ConvertFrom-Json
    Write-Host "📦 현재 장바구니 아이템 수: $($cartData.data.length)개" -ForegroundColor Cyan
} catch {
    Write-Host "❌ 장바구니 조회 실패: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. 장바구니 상품 추가 테스트
Write-Host "`n➕ 3. 장바구니 상품 추가 테스트..." -ForegroundColor Yellow
$addItemBody = @{
    productId = $productId
    quantity = 2
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$baseUrl/cart/$userId/items" -Method POST -Body $addItemBody -ContentType "application/json"
    Write-Host "✅ 장바구니 상품 추가 성공 (HTTP $($response.StatusCode))" -ForegroundColor Green
    $addedItem = $response.Content | ConvertFrom-Json
    Write-Host "📦 추가된 아이템 ID: $($addedItem.data.id)" -ForegroundColor Cyan
    $global:cartItemId = $addedItem.data.id
} catch {
    Write-Host "❌ 장바구니 상품 추가 실패: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. 위시리스트 조회 테스트
Write-Host "`n❤️ 4. 위시리스트 조회 테스트..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/wishlist/$userId" -Method GET
    Write-Host "✅ 위시리스트 조회 성공 (HTTP $($response.StatusCode))" -ForegroundColor Green
    $wishlistData = $response.Content | ConvertFrom-Json
    Write-Host "💝 현재 위시리스트 아이템 수: $($wishlistData.data.length)개" -ForegroundColor Cyan
} catch {
    Write-Host "❌ 위시리스트 조회 실패: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. 위시리스트 상품 추가 테스트
Write-Host "`n💖 5. 위시리스트 상품 추가 테스트..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/wishlist/$userId/items/$productId" -Method POST
    Write-Host "✅ 위시리스트 상품 추가 성공 (HTTP $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "❌ 위시리스트 상품 추가 실패: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 409) {
        Write-Host "⚠️  이미 위시리스트에 있는 상품일 수 있습니다." -ForegroundColor Yellow
    }
}

# 6. 수량 수정 테스트 (장바구니 아이템이 있는 경우)
if ($global:cartItemId) {
    Write-Host "`n🔢 6. 장바구니 수량 수정 테스트..." -ForegroundColor Yellow
    $updateQuantityBody = @{
        quantity = 5
    } | ConvertTo-Json
    
    try {
        $response = Invoke-WebRequest -Uri "$baseUrl/cart/items/$($global:cartItemId)" -Method PUT -Body $updateQuantityBody -ContentType "application/json"
        Write-Host "✅ 수량 수정 성공 (HTTP $($response.StatusCode))" -ForegroundColor Green
    } catch {
        Write-Host "❌ 수량 수정 실패: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 7. 최종 상태 확인
Write-Host "`n📋 7. 최종 상태 확인..." -ForegroundColor Yellow
try {
    $cartResponse = Invoke-WebRequest -Uri "$baseUrl/cart/$userId" -Method GET
    $cartData = $cartResponse.Content | ConvertFrom-Json
    Write-Host "🛒 최종 장바구니 아이템 수: $($cartData.data.length)개" -ForegroundColor Cyan
    
    $wishlistResponse = Invoke-WebRequest -Uri "$baseUrl/wishlist/$userId" -Method GET
    $wishlistData = $wishlistResponse.Content | ConvertFrom-Json
    Write-Host "❤️  최종 위시리스트 아이템 수: $($wishlistData.data.length)개" -ForegroundColor Cyan
} catch {
    Write-Host "❌ 최종 상태 확인 실패: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 통합 테스트 완료!" -ForegroundColor Green
Write-Host "=" * 50 -ForegroundColor Blue

# API Test (curl)

Base URL: `http://localhost:8080`

---

## 1. Auth

### 관리자 로그인
```bash
curl -s -X POST http://localhost:8080/api/v1/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","username":"admin","password":"1234"}'
```

### 테이블 디바이스 로그인
```bash
curl -s -X POST http://localhost:8080/api/v1/auth/table/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","tableNumber":1,"tablePassword":"0000"}'
```

### 로그인 실패 (잘못된 비밀번호)
```bash
curl -s -X POST http://localhost:8080/api/v1/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","username":"admin","password":"wrong"}'
```

---

## 2. Categories

### 카테고리 목록 조회
```bash
curl -s http://localhost:8080/api/v1/categories
```

---

## 3. Menus

### 전체 메뉴 조회
```bash
curl -s http://localhost:8080/api/v1/menus
```

### 카테고리별 메뉴 조회
```bash
curl -s "http://localhost:8080/api/v1/menus?categoryId=1"
```

> 아래 API는 관리자 토큰 필요. 먼저 토큰 저장:
> ```bash
> TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/admin/login -H "Content-Type: application/json" -d '{"storeCode":"STORE01","username":"admin","password":"1234"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
> ```

### 카테고리 등록
```bash
curl -s -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"디저트","displayOrder":3}'
```

### 카테고리 수정
```bash
curl -s -X PUT http://localhost:8080/api/v1/categories/1 \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"메인요리","displayOrder":0}'
```

### 카테고리 삭제
```bash
curl -s -X DELETE http://localhost:8080/api/v1/categories/4 \
  -H "Authorization: Bearer $TOKEN" -w "\nHTTP %{http_code}"
```

### 메뉴 등록
```bash
curl -s -X POST http://localhost:8080/api/v1/menus \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"비빔밥","price":8500,"description":"야채 비빔밥","imageUrl":null,"categoryId":1,"displayOrder":3}'
```

### 메뉴 수정
```bash
curl -s -X PUT http://localhost:8080/api/v1/menus/1 \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"김치찌개","price":9500,"description":"진한 김치찌개","imageUrl":null,"categoryId":1,"displayOrder":0}'
```

### 메뉴 삭제
```bash
curl -s -X DELETE http://localhost:8080/api/v1/menus/7 \
  -H "Authorization: Bearer $TOKEN" -w "\nHTTP %{http_code}"
```

### 메뉴 순서 변경
```bash
curl -s -X PUT http://localhost:8080/api/v1/menus/order \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '[{"menuId":1,"displayOrder":1},{"menuId":2,"displayOrder":0}]' -w "\nHTTP %{http_code}"
```

---

## 4. Tables

### 테이블 목록 조회
```bash
curl -s http://localhost:8080/api/v1/tables -H "Authorization: Bearer $TOKEN"
```

### 테이블 등록
```bash
curl -s -X POST http://localhost:8080/api/v1/tables \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"number":4,"name":"테이블 4","password":"0000"}'
```

### 테이블 수정
```bash
curl -s -X PUT http://localhost:8080/api/v1/tables/1 \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"VIP 테이블","password":"1234"}'
```

---

## 5. Orders

### 주문 생성
```bash
curl -s -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"tableId":1,"items":[{"menuId":1,"quantity":2},{"menuId":3,"quantity":1}]}'
```

### 테이블별 주문 조회
```bash
curl -s "http://localhost:8080/api/v1/orders?tableId=1" -H "Authorization: Bearer $TOKEN"
```

### 주문 상세 조회
```bash
curl -s http://localhost:8080/api/v1/orders/1 -H "Authorization: Bearer $TOKEN"
```

### 주문 삭제
```bash
curl -s -X DELETE http://localhost:8080/api/v1/orders/1 \
  -H "Authorization: Bearer $TOKEN" -w "\nHTTP %{http_code}"
```

### 주문 이력 조회
```bash
curl -s "http://localhost:8080/api/v1/orders/history?tableId=1&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"
```

### 대시보드
```bash
curl -s http://localhost:8080/api/v1/orders/dashboard -H "Authorization: Bearer $TOKEN"
```

---

## 6. Ratings

### 별점 등록
```bash
curl -s -X POST http://localhost:8080/api/v1/ratings \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"orderId":1,"ratings":[{"menuId":1,"score":5},{"menuId":3,"score":4}]}' -w "\nHTTP %{http_code}"
```

### 메뉴별 평균 별점 조회
```bash
curl -s http://localhost:8080/api/v1/ratings/menus -H "Authorization: Bearer $TOKEN"
```

---

## 7. Recommendations

### 인구통계 데이터 저장
```bash
curl -s -X POST http://localhost:8080/api/v1/recommendations/demographic \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"orderId":1,"gender":"MALE","ageGroup":"TWENTIES"}' -w "\nHTTP %{http_code}"
```

### 추천 메뉴 조회
```bash
curl -s "http://localhost:8080/api/v1/recommendations?gender=MALE&ageGroup=TWENTIES" \
  -H "Authorization: Bearer $TOKEN"
```

# 테스트 데이터 세트

BE/FE 개발 및 QA 검증 시 사용할 공통 샘플 데이터입니다.
BE 초기 데이터 (인메모리, 서버 재시작 시 초기화) 기준으로 작성되었습니다.

---

## 1. 매장 (Store)

| id | name | code | 비고 |
|---|---|---|---|
| 1 | 테스트 매장 | STORE01 | BE 하드코딩 (AuthService) |

---

## 2. 관리자 계정 (Admin)

| storeCode | username | password | 비고 |
|---|---|---|---|
| STORE01 | admin | 1234 | 정상 계정 (BE 하드코딩) |
| INVALID | hacker | wrong123 | 실패 테스트용 |

---

## 3. 테이블 (Table) — BE 초기 데이터

| id | number | name | password | 비고 |
|---|---|---|---|---|
| 1 | 1 | 테이블 1 | 0000 | BE 초기 데이터 |
| 2 | 2 | 테이블 2 | 0000 | BE 초기 데이터 |
| 3 | 3 | 테이블 3 | 0000 | BE 초기 데이터 |

---

## 4. 카테고리 (Category) — BE 초기 데이터

| id | name | displayOrder |
|---|---|---|
| 1 | 메인 | 0 |
| 2 | 사이드 | 1 |
| 3 | 음료 | 2 |

---

## 5. 메뉴 (Menu) — BE 초기 데이터

### 메인 (categoryId: 1)
| id | name | price | description | displayOrder |
|---|---|---|---|---|
| 1 | 김치찌개 | 9000 | 얼큰한 김치찌개 | 0 |
| 2 | 된장찌개 | 8000 | 구수한 된장찌개 | 1 |
| 3 | 제육볶음 | 10000 | 매콤한 제육볶음 | 2 |

### 사이드 (categoryId: 2)
| id | name | price | description | displayOrder |
|---|---|---|---|---|
| 4 | 계란말이 | 5000 | 부드러운 계란말이 | 0 |
| 5 | 김치전 | 6000 | 바삭한 김치전 | 1 |

### 음료 (categoryId: 3)
| id | name | price | description | displayOrder |
|---|---|---|---|---|
| 6 | 콜라 | 2000 | 코카콜라 355ml | 0 |
| 7 | 사이다 | 2000 | 칠성사이다 355ml | 1 |

> 모든 메뉴의 imageUrl = null, averageRating = 0.0 (초기)

---

## 6. 주문 시나리오 (Order)

> 주문 번호 형식: `ORD-YYYY-MM-DD-###` (BE 자동 생성)

### 시나리오 A: 테이블 1 — 일반 주문
| 주문 | 메뉴 | 수량 | 금액 |
|---|---|---|---|
| 주문 1 | 김치찌개 x2, 콜라 x2 | 4 | 22,000원 |
| 주문 2 | 제육볶음 x1, 계란말이 x1 | 2 | 15,000원 |
| **합계** | | | **37,000원** |

### 시나리오 B: 테이블 2 — 추가 주문
| 주문 | 메뉴 | 수량 | 금액 |
|---|---|---|---|
| 주문 1 | 된장찌개 x2, 김치전 x1 | 3 | 22,000원 |
| 주문 2 | 사이다 x3 | 3 | 6,000원 |
| **합계** | | | **28,000원** |

### 시나리오 C: 테이블 3 — 주문 삭제 테스트
| 주문 | 메뉴 | 수량 | 금액 | 비고 |
|---|---|---|---|---|
| 주문 1 | 제육볶음 x2, 콜라 x1 | 3 | 22,000원 | 관리자 삭제 예정 |
| 주문 2 | 김치전 x1, 사이다 x2 | 3 | 10,000원 | 유지 |

---

## 7. 인구통계 데이터 (Demographic)

> POST /recommendations/demographic 으로 저장
> BE 저장 형식: "gender:ageGroup" → menuId → orderCount

| orderId | gender | ageGroup | 용도 |
|---|---|---|---|
| 1 | MALE | TWENTIES | 추천 데이터 누적 |
| 2 | FEMALE | THIRTIES | 추천 데이터 누적 |
| 3 | MALE | TWENTIES | 동일 그룹 누적 (추천 정확도 테스트) |
| 4 | MALE | TWENTIES | 동일 그룹 누적 |
| 5 | FEMALE | TEENS | 데이터 부족 그룹 (폴백 테스트) |

---

## 8. 별점 데이터 (Rating)

> POST /ratings 로 저장
> BE 저장 형식: menuId → List<Integer> (scores)
> 평균 계산: 소수점 1자리 반올림

| orderId | menuId | score | 용도 |
|---|---|---|---|
| 1 | 1 (김치찌개) | 5 | 높은 평점 |
| 1 | 6 (콜라) | 3 | 보통 평점 |
| 2 | 3 (제육볶음) | 4 | |
| 2 | 4 (계란말이) | 5 | |
| 3 | 2 (된장찌개) | 2 | 낮은 평점 |

**기대 평균 별점 (GET /ratings/menus):**
- 김치찌개 (menuId=1): 5.0
- 된장찌개 (menuId=2): 2.0
- 제육볶음 (menuId=3): 4.0
- 계란말이 (menuId=4): 5.0
- 콜라 (menuId=6): 3.0

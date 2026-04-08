# 화면 플로우 — 테이블오더 서비스

FE 팀원 참고용 화면 이동 흐름도입니다.

---

## 1. 고객용 (Customer) 플로우

```
[앱 진입]
    |
    v
저장된 로그인 정보 있음? ──No──> /customer/setup (테이블 초기 설정)
    |                                    |
   Yes                          매장코드 + 테이블번호 + 비밀번호 입력
    |                                    |
    v                                    v
자동 로그인 시도                    POST /auth/table/login
    |                                    |
 성공?──No──> /customer/setup       성공?──No──> 에러 표시, 재입력
    |                                    |
   Yes                                  Yes (토큰+정보 localStorage 저장)
    |                                    |
    +<───────────────────────────────────+
    |
    v
/customer/menu (메뉴 조회 — 기본 화면) <─────────────────────┐
    |                                                         |
    |── 카테고리 탭 클릭 → 해당 카테고리 메뉴 표시            |
    |── 메뉴 카드 클릭 → 메뉴 상세 (이미지, 설명, 별점)      |
    |── "장바구니 담기" → 장바구니에 추가                      |
    |── 1분 후 → [추천 모달] 성별/나이대 입력 → 추천 메뉴     |
    |                                                         |
    v                                                         |
/customer/cart (장바구니)                                      |
    |                                                         |
    |── 수량 +/- 조절                                         |
    |── 메뉴 삭제                                             |
    |── 장바구니 비우기                                        |
    |── "주문하기" 클릭 → POST /orders                        |
    |                                                         |
    v                                                         |
 성공?──No──> 에러 표시, 장바구니 유지                         |
    |                                                         |
   Yes                                                        |
    |                                                         |
    v                                                         |
/customer/order-success (주문 완료)                            |
    |                                                         |
    |── 주문 번호 표시                                         |
    |── [인구통계 모달] 성별/나이대 선택 (건너뛰기 가능)       |
    |── 5초 후 자동 리다이렉트 ─────────────────────────────>─┘
    |                                                    장바구니 클리어
    |── 10초 후 → [별점 평가 모달] 메뉴별 1~5점 (건너뛰기 가능)
    |
    v
/customer/orders (주문 내역 — 내비게이션에서 접근)
    |
    |── 테이블별 주문 표시 (GET /orders?tableId=)
    |── 주문 시간 역순
    |── 페이지네이션
```

---

## 2. 관리자용 (Admin) 플로우

```
[앱 진입]
    |
    v
JWT 토큰 유효? ──No──> /admin/login
    |                       |
   Yes               매장코드 + 사용자명 + 비밀번호
    |                       |
    |                  POST /auth/admin/login
    |                       |
    |                  성공?──No──> 에러 표시
    |                       |
    +<─────────────────Yes──+
    |
    v
/admin/dashboard (실시간 주문 모니터링 — 기본 화면)
    |
    |── 테이블별 카드 그리드 레이아웃 (GET /orders/dashboard)
    |── 신규 주문 시 카드 강조 (hasNewOrder)
    |── 테이블별 필터링
    |
    |── 테이블 카드 클릭
    |       |
    |       v
    |   [TableDetailModal]
    |       |── 주문 상세 목록 (GET /orders?tableId=)
    |       |── 주문 삭제 → [ConfirmDialog] → DELETE /orders/{id}
    |       |── 닫기 → 대시보드로 복귀
    |
    |── 내비게이션
    |       |
    |       +──> /admin/tables (테이블 관리)
    |       |       |── 테이블 목록 (GET /tables)
    |       |       |── 테이블 등록 (POST /tables — 번호, 이름, 비밀번호)
    |       |       |── 테이블 수정 (PUT /tables/{id} — 이름, 비밀번호)
    |       |
    |       +──> /admin/history (과거 주문 내역)
    |       |       |── 테이블별 과거 주문 (GET /orders/history?tableId=&page=&size=)
    |       |       |── 주문 상세: 번호, 메뉴(이름/수량/단가), 총 금액, 완료 시각
    |       |
    |       +──> /admin/menus (메뉴 관리)
    |               |── 카테고리별 메뉴 목록 (GET /menus?categoryId=)
    |               |── 메뉴 등록 (POST /menus — 이름, 가격, 설명, 카테고리, imageUrl)
    |               |── 메뉴 수정/삭제 (PUT/DELETE /menus/{id})
    |               |── 메뉴 노출 순서 변경 (PUT /menus/order)
    |               |── 카테고리 CRUD (GET/POST/PUT/DELETE /categories)
    |
    |── 토큰 만료 시 → ReLoginModal 표시 (재로그인 또는 /admin/login 이동)
    |   (현재 BE: 1시간, 목표: 16시간)
```

---

## 3. 주문 완료 후 모달 순서 (PO 확정)

```
주문 확정 (POST /orders 성공)
    |
    v
/customer/order-success
    |
    +--> [인구통계 모달] (주문 완료 전후 표시 — FE 구현에 따름)
    |       |── 성별 선택: 남 / 여
    |       |── 나이대 선택: 10대 / 20대 / 30대 / 40대 / 50대이상
    |       |── "건너뛰기" 가능
    |       |── POST /recommendations/demographic
    |       |── ⚠️ 현재 FE 미연결 — 컴포넌트만 완성
    |
    +--> 5초 후 메뉴 화면 자동 리다이렉트
    |       |── 장바구니 자동 클리어
    |
    +--> 10초 후 [별점 평가 모달] (FR-C07)
    |       |── 주문한 메뉴별 1~5점 별점
    |       |── "건너뛰기" 가능
    |       |── POST /ratings
    |       |── ⚠️ 현재 FE 미연결 — 컴포넌트만 완성, 타이머 미연결
    |
    +--> 메뉴 화면 진입 1분 후 [추천 모달] (FR-C06)
            |── 성별/나이대 선택
            |── 추천 메뉴 표시
            |── GET /recommendations?gender=&ageGroup=
```

---

## 4. 라우팅 요약

| 경로 | 페이지 | 접근 권한 |
|---|---|---|
| `/customer/setup` | 테이블 초기 설정 | 비인증 |
| `/customer/menu` | 메뉴 조회 (기본) | 테이블 토큰 |
| `/customer/cart` | 장바구니 | 테이블 토큰 |
| `/customer/order-success` | 주문 완료 | 테이블 토큰 |
| `/customer/orders` | 주문 내역 | 테이블 토큰 |
| `/admin/login` | 관리자 로그인 | 비인증 |
| `/admin/dashboard` | 실시간 모니터링 | 관리자 토큰 |
| `/admin/tables` | 테이블 관리 | 관리자 토큰 |
| `/admin/history` | 과거 주문 내역 | 관리자 토큰 |
| `/admin/menus` | 메뉴 관리 | 관리자 토큰 |

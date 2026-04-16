# 📮 PickyLetter (PineApple)

> 원하는 뉴스레터 플랫폼을 구독하고, 구독한 레터들을 한 곳에서 관리·열람할 수 있는 Android 뉴스레터 큐레이션 앱

---

## 📌 프로젝트 정보

| 항목 | 내용 |
|------|------|
| **프로젝트명** | PickyLetter (내부 모듈명: PineApple) |
| **플랫폼** | Android (Native Java) |
| **패키지명** | `com.makeus.pineapple` |
| **개발 기간** | 2021.03 ~ 2021.04 |
| **최소 SDK** | API 16 (Android 4.1 Jelly Bean) |
| **타겟 SDK** | API 30 (Android 11) |
| **언어** | Java (100%) |
| **빌드 도구** | Gradle + Android Gradle Plugin |

---

## 🛠 기술 스택

- **언어**: Java 8
- **네트워크**: Volley 1.1.0
- **JSON 파싱**: Gson 2.8.5
- **이미지 로딩**: Glide 4.9.0
- **UI 컴포넌트**: RecyclerView, ConstraintLayout, CoordinatorLayout, CircleImageView 3.0.0
- **아키텍처**: Activity / Fragment 기반 화면 구성, Interface 기반 서버 통신 추상화

---

## 🔍 핵심 기술 및 구현 내용

#### 인터페이스 기반 REST API 통신 추상화 (server_controllers)

서버 통신 로직을 HTTP 메서드 단위로 패키지화하여 관리한다. `get`, `post`, `patch`, `delete` 네 개의 디렉토리로 구분되어 있으며, 각 패키지 내부에는 공통 동작을 정의한 Interface와 기능별 구체 클래스가 존재한다.

`GetRequestInterface`는 Java 8의 `default` 메서드를 활용하여 `makeJsonObject()`, `makeRequestUrl()`, `makeJsonRequest()` 등의 공통 로직을 인터페이스 레벨에서 구현하였다. 모든 GET 요청 클래스는 이 인터페이스를 `implements`하여 `tryRequest()`와 `processResponse()`만 구체적으로 정의하면 되므로, 코드 중복 없이 다양한 API 엔드포인트를 균일한 구조로 처리할 수 있다.

인증 토큰 처리는 Volley의 `JsonObjectRequest`를 익명 클래스로 오버라이드하여 `getHeaders()`에서 `x-access-token` 헤더를 자동 주입하는 방식으로 구현하였다. 캐시 방지를 위해 `request.setShouldCache(false)`를 적용하였다.

#### Gson을 활용한 JSON 역직렬화 및 데이터 모델 설계 (server_data)

서버 응답 데이터를 `server_data` 패키지의 POJO 모델 클래스로 매핑한다. `AllPlatformResult`, `SubscribingPlatformResult`, `NewsResult`, `MailboxRequestData` 등 서버 응답 구조를 그대로 반영한 Result/Data 클래스 쌍으로 설계하였다.

`processResponse()` 콜백에서 `Gson.fromJson(response.toString(), TargetClass.class)` 패턴으로 JSON을 자동 역직렬화한 뒤, 이를 즉시 RecyclerView Adapter에 바인딩하여 UI 갱신까지 이어지는 단일 흐름을 구성하였다.

#### RecyclerView 멀티 ViewType 패턴 (home/adapters, mypage_settings)

홈 화면과 마이페이지 설정 화면에서 리스트의 중간 아이템과 마지막 아이템을 각각 다른 뷰 타입으로 렌더링한다. `EditLetterViewCode`에 `VIEW_EDIT_LETTER_MIDDLE`, `VIEW_EDIT_LETTER_END` 상수를 정의하여 인덱스 비교(`i == size - 1`)로 ViewType을 구분하고, `EditLetterAdapter`에서 해당 코드를 기준으로 다른 레이아웃을 inflate하는 방식이다. 이를 통해 리스트 말단의 UI 처리(구분선 제거, 하단 여백 등)를 재사용 가능한 구조로 설계하였다.

#### Fragment 기반 바텀 네비게이션 (main/MainActivity)

`MainActivity`는 바텀 네비게이션 뷰를 통해 홈(`Fragment1_Home`), 북마크, 검색, 마이페이지 네 개의 Fragment를 관리한다. Fragment 전환 시 사용자 세션 토큰을 `MainActivity`의 정적 메서드 `getToken()`으로 공유하여 하위 Fragment와 서버 통신 클래스 모두 동일한 토큰을 참조하도록 설계하였다. 이 방식은 `GetRequestInterface`의 `getHeaders()`에서 `import static`으로 직접 호출된다.

#### 스플래시 화면 및 자동 로그인 흐름 (main/Splash, sign/SignIn)

앱 시작 시 `Splash` Activity가 `LAUNCHER`로 등록되어 1.8초 후 `Handler.postDelayed()`로 `SignIn` Activity로 전환된다. 스플래시 화면 자체는 Activity 스택에서 즉시 제거(`finish()`)되어 뒤로 가기 시 스플래시로 복귀하는 문제를 방지하며, `onBackPressed()` 오버라이드로 전환 중 뒤로가기를 완전히 차단하였다. 로그인 화면에서는 자동 로그인 로직도 구현되어 있다.

#### 단계형 회원가입 프로세스 (sign)

회원가입 화면을 `SignUp_1`, `SignUp_2`, `SignUp_3` 세 단계 Activity로 분리하였다. 각 단계는 단방향으로만 진행되며 Intent를 통해 이전 단계에서 수집된 데이터를 누적 전달하는 구조이다. 회원가입 완료 후 서버 POST 요청으로 계정 생성을 처리하고 즉시 로그인 화면으로 전환된다.

#### 홈 화면 필터 기능 및 팝업 처리 (home/filters, popup)

홈 화면에서 레터를 카테고리나 날짜 범위 기준으로 필터링하는 기능을 제공한다. `PopupFilter`는 `Theme.Dialog`로 등록된 Activity로 구현되어 Activity 위에 오버레이 형태로 표시된다. 날짜 선택은 `PopupStartDatePicker`와 `PopupEndDatePicker` 두 개의 다이얼로그 Activity로 처리하며, 구독/구독 해제(`PopupSub`, `PopupUnSub`), 로그아웃(`PopupLogout`), 로딩 인디케이터(`PopupLoading`) 등 사용자 인터랙션별 전용 팝업을 Activity 기반으로 분리하였다.

#### ISO 8601 날짜 포맷 파싱 유틸리티 (CalStringDate)

서버에서 반환되는 `yyyy-MM-dd'T'HH:mm:ss'Z'` 형식의 ISO 8601 문자열을 `SimpleDateFormat`으로 파싱하여 `MM/dd/yyyy` 형식의 로컬 표시용 문자열로 변환하는 정적 유틸리티 클래스이다. 앱 전체에서 날짜 표시 일관성을 유지하기 위해 `static` 메서드로 설계하여 어디서든 재사용 가능하도록 하였다.

#### 북마크 및 메일함 기능 (bookmark, home/data)

사용자가 레터를 북마크하기 전 레터 상세 정보를 먼저 조회(`GetLetterInformBeforeBookmark`)한 뒤 북마크 처리를 수행하고, 이후 북마크 목록을 다시 불러오는(`GetBookmarkLettersAgain`) 연쇄 API 호출 패턴을 사용한다. 메일함은 상단 요약(`GetMailBoxTop`)과 하단 상세(`GetMailBoxBottom`)를 별도 API로 분리 조회하며, 각각에 대한 재조회 클래스(`Again` suffix)도 구비되어 있다. `SwipeRefreshLayout` 기반의 당겨서 새로고침 기능도 구현되어 있다.

#### 검색 기능 (search)

`GetSearchResult` 클래스를 통해 사용자 입력 키워드로 플랫폼 및 뉴스레터 검색 API를 호출한다. `GetSearchResultAgain`으로 동일 조건 재조회를 지원하며, `GetSubPlatformFilter`를 통해 구독 플랫폼에 한정한 필터 검색도 가능하다.

---

## ✅ 주요 구현 포인트

- Java 8 `default` 인터페이스 메서드를 활용해 Volley HTTP 요청의 공통 로직(헤더 주입, JSON 직렬화, 에러 처리)을 `GetRequestInterface`에 캡슐화하고, 20개 이상의 API 클래스가 이를 재사용하도록 설계하여 코드 중복을 최소화함
- `x-access-token` JWT 인증 헤더를 모든 API 요청에 자동 주입하는 구조를 인터페이스 레벨에서 처리하여 개별 요청 클래스에서 인증 로직을 분리함
- Gson `fromJson()`을 활용해 서버 응답 JSON을 POJO 모델로 즉시 역직렬화하고 RecyclerView에 바인딩하는 단일 데이터 흐름(요청 → 파싱 → UI 갱신)을 구현함
- RecyclerView 멀티 ViewType 패턴으로 리스트 마지막 아이템을 별도 뷰 타입으로 처리하여 구분선·여백 등 UI 엣지 케이스를 Adapter 레벨에서 선언적으로 해결함
- 팝업/다이얼로그 UI를 `Theme.Dialog` 테마의 독립 Activity로 구현하여 화면별 역할 분리 및 재사용성 확보, 총 9개의 팝업 Activity를 기능 단위로 분리함
- `Handler.postDelayed()` + `finish()`를 활용한 스플래시 화면 구현으로 Activity 백스택 오염 없이 앱 진입 흐름을 자연스럽게 처리함
- ISO 8601 날짜 문자열 파싱을 정적 유틸리티 클래스(`CalStringDate`)로 분리하여 앱 전역의 날짜 포맷 일관성을 단일 지점에서 관리함
- `server_controllers` 패키지를 HTTP 메서드(`get`/`post`/`patch`/`delete`) 단위로 분리하고, 기능별 클래스명에 `Again` suffix를 붙여 재조회 케이스를 명시적으로 구분함으로써 API 레이어의 가독성과 유지보수성을 높임

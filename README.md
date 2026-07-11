# 뉴스레터 큐레이션 앱 피키레터 (Picky Letter)

**Java와 Android Studio로 개발해 Google Play Store에 출시한 뉴스레터 구독 및 큐레이션 안드로이드 클라이언트**

[![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)](https://www.java.com/)
[![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white)](https://developer.android.com/studio)
[![Volley](https://img.shields.io/badge/Volley-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/training/volley)

> https://youtu.be/u2X_XooNu3g?si=TAnIL9zTUeXB7dwV

---

## 프로젝트 정보

| 항목 | 내용 |
|---|---|
| 장르 | 뉴스레터 구독 및 큐레이션 앱 |
| 개발 기간 | 2021.01 ~ 2021.04 |
| 팀 구성 | IT 연합동아리, 서버 파트 2인과 협업 |
| 사용 기술 | Java, Android Studio, Volley, RecyclerView, Fragment, SharedPreferences |

**본인 담당 파트:** 안드로이드 클라이언트 전체 (UI, 화면 전환, 서버 통신 모듈)

---

## 프로젝트 개요

여러 매체의 뉴스레터를 한 곳에서 구독하고 모아볼 수 있는 큐레이션 앱입니다. 서버 파트와 역할을 나눠 클라이언트 개발을 전담했으며, REST API 통신 구조를 인터페이스 기반으로 설계해 반복되는 네트워크 코드를 줄이는 데 집중했습니다. 출시 후에도 사용자 피드백을 반영해 해상도 및 그래픽 비율을 개선했습니다.

---

## 코드 상세

### 서버 통신 (`server_controllers/`)

HTTP 메서드별 기본 인터페이스와, 이를 상속해 리소스별 요청 URL과 응답 처리를 구현하는 구조로 설계했습니다.

| 파일 | 역할 |
|---|---|
| [`server_controllers/get/GetRequestInterface.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetRequestInterface.java) | GET 요청 공통 처리. Volley JsonObjectRequest 생성, 인증 헤더(x-access-token) 부착 |
| [`server_controllers/post/PostRequest.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/post/PostRequest.java) | POST 요청 공통 처리 |
| [`server_controllers/patch/PatchRequest.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/patch/PatchRequest.java) | PATCH 요청 공통 처리 |
| [`server_controllers/delete/DeleteRequest.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/delete/DeleteRequest.java) | DELETE 요청 공통 처리 |
| [`server_controllers/get/GetMailboxInterface.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetMailboxInterface.java) | GetRequestInterface 상속, 기간 필터/페이지네이션 쿼리 파라미터로 메일함 URL 생성 |
| [`server_controllers/get/GetMailBoxTop.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetMailBoxTop.java) / `GetMailBoxTopAgain.java` | 홈 상단(최신) 뉴스레터 목록 최초 로딩 / 페이지네이션 추가 로딩 |
| [`server_controllers/get/GetMailBoxBottom.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetMailBoxBottom.java) / `GetMailBoxBottomAgain.java` | 홈 하단(지난) 뉴스레터 목록 최초 로딩 / 추가 로딩 |
| [`server_controllers/get/GetLetterInformHomeMail.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetLetterInformHomeMail.java) | 개별 뉴스레터 상세 정보 조회 |
| [`server_controllers/get/GetLetterInformBeforeBookmark.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetLetterInformBeforeBookmark.java) / `GetLetterInformBeforeBookmarkDel.java` | 북마크 등록/해제 전 레터 정보 조회 |
| [`server_controllers/get/GetAllPlatform.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetAllPlatform.java) / `GetAllPlatform_first.java` | 구독 가능한 전체 매체 목록 조회 |
| [`server_controllers/get/GetSubPlatform.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetSubPlatform.java) / `GetSubPlatformFilter.java` | 구독 중인 매체 목록 및 필터 조건별 조회 |
| [`server_controllers/get/GetSearchResult.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetSearchResult.java) / `GetSearchResultAgain.java` | 검색 결과 최초 로딩 / 추가 로딩 |
| [`server_controllers/get/GetBookmarkLetters.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetBookmarkLetters.java) / `GetBookmarkLettersAgain.java` | 북마크한 레터 목록 최초 로딩 / 추가 로딩 |
| [`server_controllers/get/GetUserData.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/get/GetUserData.java) | 사용자 정보 조회 |
| [`server_controllers/post/PostAddPlatform.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/post/PostAddPlatform.java) | 매체 구독 추가 요청 |
| [`server_controllers/post/PostBookmarkAdd.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/post/PostBookmarkAdd.java) | 레터 북마크 등록 요청 |
| [`server_controllers/patch/PatchNickname.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/patch/PatchNickname.java) | 닉네임 변경 요청 |
| [`server_controllers/delete/DeleteBookmark.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/delete/DeleteBookmark.java) | 북마크 삭제 요청 |
| [`server_controllers/delete/DeleteSubPlatform.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/delete/DeleteSubPlatform.java) | 매체 구독 해제 요청 |
| [`server_controllers/delete/DeleteUser.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/delete/DeleteUser.java) | 회원 탈퇴 요청 |
| [`server_controllers/server_data/NewsData.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/server_data/NewsData.java), `NewsResult.java` | 뉴스레터 응답 데이터 모델 (Gson 매핑) |
| [`server_controllers/server_data/AllPlatformData.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/server_data/AllPlatformData.java), `AllPlatformResult.java` | 전체 매체 목록 응답 데이터 모델 |
| [`server_controllers/server_data/MailboxRequestData.java`](./PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/server_data/MailboxRequestData.java) | 메일함 조회 요청 파라미터(userId, page, 기간) 객체 |

### 인증 (`sign/`)

| 파일 | 역할 |
|---|---|
| [`sign/SignIn.java`](./PineApple/app/src/main/java/com/makeus/pineapple/sign/SignIn.java) | 로그인 처리, SharedPreferences 토큰/닉네임 저장 및 자동 로그인 분기 |
| [`sign/SignUp_1.java`](./PineApple/app/src/main/java/com/makeus/pineapple/sign/SignUp_1.java) / `SignUp_2.java` / `SignUp_3.java` | 회원가입 단계별 화면 (이메일 입력 → 인증 → 닉네임/비밀번호 설정) |
| [`sign/signUp/EmailCheckResult.java`](./PineApple/app/src/main/java/com/makeus/pineapple/sign/signUp/EmailCheckResult.java) | 이메일 중복 확인 응답 모델 |
| [`sign/users/User.java`](./PineApple/app/src/main/java/com/makeus/pineapple/sign/users/User.java), `UserResult.java` | 사용자 정보 데이터 모델 |

### 홈 (`home/`)

| 파일 | 역할 |
|---|---|
| [`home/Fragment1_Home.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/Fragment1_Home.java) | 홈 화면. 최신/지난 레터 리스트, 당겨서 새로고침, 필터 팝업 연동 |
| [`home/adapters/HomeAdapters.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/adapters/HomeAdapters.java) | NewLetterAdapter, OldLetterAdapter가 공통으로 구현하는 인터페이스 |
| [`home/adapters/NewLetterAdapter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/adapters/NewLetterAdapter.java) | 최신 레터 목록 어댑터. NEW / 로딩 / 더보기 뷰타입 분리 |
| [`home/adapters/OldLetterAdapter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/adapters/OldLetterAdapter.java) | 지난 레터 목록 어댑터 |
| [`home/data/HomeLetters.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/data/HomeLetters.java) | 홈 리스트 아이템 데이터 모델 |
| [`home/filters/FilterBrand.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/filters/FilterBrand.java), `FilterBrandAdapter.java` | 매체 필터 목록 및 어댑터 |
| [`home/filters/PopupFilter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/home/filters/PopupFilter.java) | 기간/매체 필터 팝업 |

### 검색 (`search/`)

| 파일 | 역할 |
|---|---|
| [`search/Fragment2_Search.java`](./PineApple/app/src/main/java/com/makeus/pineapple/search/Fragment2_Search.java) | 검색 화면. 무한 스크롤 결과 리스트, 당겨서 새로고침 |
| [`search/SearchViewCode.java`](./PineApple/app/src/main/java/com/makeus/pineapple/search/SearchViewCode.java) | 검색 결과 뷰타입 상수 정의 |
| [`search/adapters/SearchedNewsRankAdapter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/search/adapters/SearchedNewsRankAdapter.java) | 인기 검색 랭킹 어댑터 |
| [`search/adapters/SearchedNewsResultAdapter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/search/adapters/SearchedNewsResultAdapter.java) | 검색 결과 어댑터. 로딩 / 더보기 뷰타입 처리 |
| [`search/searchViewHolders/`](./PineApple/app/src/main/java/com/makeus/pineapple/search/searchViewHolders) | 랭킹, 결과, 블라인드 처리 등 상황별 뷰홀더 모음 |
| [`search/data/SearchedNews.java`](./PineApple/app/src/main/java/com/makeus/pineapple/search/data/SearchedNews.java), `server_data/SearchData.java`, `SearchResult.java` | 검색 결과 데이터 모델 |

### 마이페이지 및 설정 (`mypage_settings/`)

| 파일 | 역할 |
|---|---|
| [`mypage_settings/mypage/Fragment3_MyPage.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/mypage/Fragment3_MyPage.java) | 마이페이지 화면. 북마크한 레터, 사용자 정보 표시 |
| [`mypage_settings/mypage/adapter/BookmarkLetterAdapter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/mypage/adapter/BookmarkLetterAdapter.java) | 북마크한 레터 목록 어댑터 |
| [`mypage_settings/mypage/data/BookmarkLetter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/mypage/data/BookmarkLetter.java), `BookmarkLetterResult.java`, `UserData.java` | 마이페이지 데이터 모델 |
| [`mypage_settings/settings/SettingsMain.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/settings/SettingsMain.java) | 설정 메인 화면 |
| [`mypage_settings/settings/SettingsProfileEdit.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/settings/SettingsProfileEdit.java) | 닉네임 등 프로필 수정 화면 |
| [`mypage_settings/settings/SettingsEditLetter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/settings/SettingsEditLetter.java) | 구독 매체 편집 화면 |
| [`mypage_settings/settings/EditLetterAdapter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/settings/EditLetterAdapter.java), `EditLetterViewCode.java`, `EditedLetter.java` | 구독 매체 편집 리스트 어댑터 및 데이터 모델 |
| [`mypage_settings/mypageSettingsViewHolders/`](./PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/mypageSettingsViewHolders) | 편집 리스트 상단 / 중간 / 하단 뷰홀더 |

### 북마크 (`bookmark/`)

| 파일 | 역할 |
|---|---|
| [`bookmark/BookmarkFuncs.java`](./PineApple/app/src/main/java/com/makeus/pineapple/bookmark/BookmarkFuncs.java) | 북마크 버튼 클릭 시 서버 요청 및 UI 상태 갱신 로직 |
| [`bookmark/AddOrDelBookmark.java`](./PineApple/app/src/main/java/com/makeus/pineapple/bookmark/AddOrDelBookmark.java) | 북마크 추가/삭제 요청 클래스 |
| [`bookmark/BookmakrResult.java`](./PineApple/app/src/main/java/com/makeus/pineapple/bookmark/BookmakrResult.java) | 북마크 요청 응답 모델 |

### 팝업 (`popup/`)

| 파일 | 역할 |
|---|---|
| [`popup/PopupEmpty.java`](./PineApple/app/src/main/java/com/makeus/pineapple/popup/PopupEmpty.java) | 빈 상태(데이터 없음) 안내 팝업 |
| [`popup/PopupLogout.java`](./PineApple/app/src/main/java/com/makeus/pineapple/popup/PopupLogout.java), `PopupOut.java` | 로그아웃 / 앱 종료 확인 팝업 |
| [`popup/PopupSub.java`](./PineApple/app/src/main/java/com/makeus/pineapple/popup/PopupSub.java), `PopupUnSub.java` | 구독 / 구독 해제 확인 팝업 |
| [`popup/PopupStartDatePicker.java`](./PineApple/app/src/main/java/com/makeus/pineapple/popup/PopupStartDatePicker.java), `PopupEndDatePicker.java` | 기간 필터용 날짜 선택 팝업 |
| [`popup/loading/PopupLoading.java`](./PineApple/app/src/main/java/com/makeus/pineapple/popup/loading/PopupLoading.java) | 로딩 인디케이터 팝업 |

### 공통 (root)

| 파일 | 역할 |
|---|---|
| [`HomeMail.java`](./PineApple/app/src/main/java/com/makeus/pineapple/HomeMail.java) | 뉴스레터 상세 화면. WebView와 Glide로 본문/이미지 렌더링, 북마크 연동 |
| [`FirstEditLetter.java`](./PineApple/app/src/main/java/com/makeus/pineapple/FirstEditLetter.java) | 최초 구독 매체 선택 화면 (온보딩) |
| [`CalStringDate.java`](./PineApple/app/src/main/java/com/makeus/pineapple/CalStringDate.java) | 서버 응답의 ISO 날짜 문자열을 화면 표시용 포맷으로 변환하는 유틸 |

---

## 핵심 구현

### 인터페이스 기반 REST API 통신 모듈
GetRequestInterface, PostRequest, PatchRequest, DeleteRequest 4개의 HTTP 메서드별 인터페이스에 tryRequest, processResponse, makeRequestUrl 공통 규약을 정의하고, Volley의 JsonObjectRequest 생성 로직을 default 메서드로 캡슐화했습니다. GetMailboxInterface처럼 상위 인터페이스를 상속해 요청 URL 생성 로직만 오버라이드하거나 응답 처리 로직만 구현하면 되도록 설계해 중복 코드를 줄였습니다. 인증이 필요한 요청은 헤더에 x-access-token을 공통으로 부착합니다.

### 다중 뷰타입 RecyclerView 무한 스크롤
NewLetterAdapter, OldLetterAdapter, SearchedNewsResultAdapter에 VIEW_TYPE_NEW_NEWS, VIEW_TYPE_LOADING, VIEW_TYPE_MORE 등 뷰타입을 분리해 목록 끝에서 추가 로딩 상태를 표시했습니다. 홈 화면 상단 리스트에는 PagerSnapHelper를 적용해 카드가 스냅되며 넘어가는 캐러셀 형태로 구현했습니다.

### SharedPreferences 기반 자동 로그인
로그인 성공 시 토큰과 닉네임을 SharedPreferences에 저장하고, 앱 재실행 시 저장된 토큰을 확인해 자동 로그인 여부를 분기합니다. MainActivity의 정적 getToken()으로 전역에서 인증 토큰에 접근해 모든 API 요청 헤더에 일괄 적용합니다.

### 당겨서 새로고침 및 뉴스레터 본문 렌더링
Fragment1_Home, Fragment2_Search에 SwipeRefreshLayout을 적용해 당겨서 새로고침 기능을 구현했습니다. 개별 뉴스레터 상세 화면(HomeMail)은 WebView로 본문을 렌더링하고 Glide로 이미지를 비동기 로딩합니다.

---

## 폴더 구조
- `PineApple/app/src/main/java/com/makeus/pineapple/server_controllers/` : REST API 통신 인터페이스 및 요청 클래스 전체
- `PineApple/app/src/main/java/com/makeus/pineapple/sign/` : 로그인 및 회원가입 화면
- `PineApple/app/src/main/java/com/makeus/pineapple/home/` : 홈 화면 및 리스트 어댑터
- `PineApple/app/src/main/java/com/makeus/pineapple/search/` : 검색 화면 및 결과 어댑터
- `PineApple/app/src/main/java/com/makeus/pineapple/mypage_settings/` : 마이페이지 및 구독 매체 설정
- `PineApple/app/src/main/java/com/makeus/pineapple/bookmark/` : 북마크 기능
- `PineApple/app/src/main/java/com/makeus/pineapple/popup/` : 공용 팝업 컴포넌트

# 🐝 Kanbee (カンビー)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white)

> **AIベースの総合コミュニティ＆予約プラットフォーム**  
> **AI 기반의 종합 커뮤니티 및 예약 플랫폼**

*(日本語の説明の後に韓国語の説明があります / 한국어 설명은 아래에 있습니다)*

---

## 🇯🇵 日本語 (Japanese)

### 📌 プロジェクトの目的
フルスタックでの開発経験を積み、Spring Securityを活用した安全な認証、MyBatisによるデータベース制御、そして外部API（AI）との連携を実践するために開発しました。

### 🚀 技術スタック
* **Backend**: Java 17, Spring Boot 3.x, Spring Security, MyBatis
* **Frontend**: HTML5, CSS3, JavaScript, Thymeleaf
* **Database**: MySQL
* **AI Integration**: OpenAI / Google Gemini API

### 💡 主な機能
1. **🔐 ユーザー認証とセキュリティ**: Spring Securityを用いたセキュアなログイン・会員登録。管理者(Admin)と一般ユーザー(User)の権限分離。
2. **💬 コミュニティ掲示板**: 自由掲示板、Q&Aなど複数のカテゴリに対応。コメント機能とページネーションを実装。
3. **📅 予約システム**: ユーザーが希望する日時でサービスを直感的に予約可能。管理者は予約状況を一目で把握・管理。
4. **🤖 AI おすすめ機能**: Gemini / OpenAI APIと連携し、ユーザーの好みに合わせたスポット（Hot Place）をAIが自動で推薦。
5. **🌍 多言語対応 (i18n)**: 日本語、韓国語、英語のメッセージプロパティを適用し、グローバルな環境に対応。

### 🛠 工夫した点
データベースの設計において、将来的な拡張性を考慮し、正規化を徹底しました。また、AI APIの呼び出し時に発生しうる遅延を考慮し、ユーザーにストレスを与えないよう非同期処理とローディングUIを工夫しました。

### ⚙️ 実行方法
1. **DB Setup**: MySQLで `jsl26db` を作成します。
2. **Config**: `application.properties` にデータベース情報とAI APIキーを入力します。
3. **Run**: `./gradlew bootRun` コマンドで実行します。

---

## 🇰🇷 한국어 (Korean)

### 📌 프로젝트 목적
풀스택 개발 경험을 쌓고, Spring Security를 활용한 안전한 인증, MyBatis를 통한 데이터베이스 제어, 그리고 외부 API(AI) 연동을 실무적으로 적용해보기 위해 개발했습니다.

### 🚀 기술 스택
* **Backend**: Java 17, Spring Boot 3.x, Spring Security, MyBatis
* **Frontend**: HTML5, CSS3, JavaScript, Thymeleaf
* **Database**: MySQL
* **AI Integration**: OpenAI / Google Gemini API

### 💡 주요 기능
1. **🔐 사용자 인증 및 보안**: Spring Security를 활용한 안전한 로그인 및 회원가입. 관리자(Admin)와 일반 사용자(User)의 권한 분리.
2. **💬 커뮤니티 게시판**: 자유게시판, Q&A 등 다중 카테고리 지원. 댓글 기능 및 페이징 처리 구현.
3. **📅 예약 시스템**: 사용자가 원하는 날짜와 시간에 서비스를 직관적으로 예약 가능. 관리자는 예약 현황을 한눈에 파악 및 관리.
4. **🤖 AI 추천 기능**: Gemini / OpenAI API와 연동하여 사용자 취향에 맞는 핫플레이스를 AI가 자동으로 추천.
5. **🌍 다국어 지원 (i18n)**: 일본어, 한국어, 영어 메시지 프로퍼티를 적용하여 글로벌 환경 지원.

### 🛠 개발 중 고민한 점
데이터베이스 설계 시 향후 확장성을 고려하여 정규화를 철저히 진행했습니다. 또한 AI API 호출 시 발생할 수 있는 지연 시간을 고려하여 사용자에게 불편함을 주지 않도록 비동기 처리와 로딩 UI를 개선했습니다.

### ⚙️ 실행 방법
1. **DB Setup**: MySQL에서 `jsl26db`를 생성합니다.
2. **Config**: `application.properties`에 DB 정보와 AI API 키를 입력합니다.
3. **Run**: `./gradlew bootRun` 명령어로 실행합니다.

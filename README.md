<div align="center">
  <h1>🐝 Kanbee (カンビー)</h1>
  
  <p><strong>AIベースの総合コミュニティ＆予約プラットフォーム</strong><br>
  <strong>AI 기반의 종합 커뮤니티 및 예약 플랫폼</strong></p>

  <p>
    <img src="https://img.shields.io/badge/Spring%20Boot-3.4.0-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white" alt="Java">
    <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white" alt="MySQL">
    <img src="https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  </p>
</div>

<br>

Kanbeeは、ユーザー間の円滑なコミュニケーション、便利なサービス予約、そしてAIを活用したパーソナライズされたおすすめ機能を提供するWebアプリケーションです。  
Kanbee는 사용자 간의 원활한 소통, 편리한 서비스 예약, 그리고 AI를 활용한 개인화된 추천 기능을 제공하는 웹 애플리케이션입니다.

<br>

## 📖 プロジェクトの背景と名前の由来 / 프로젝트 배경 및 이름의 유래

> **Kanbee(カンビー)** は、日本語の**「完備 (かんび)」**という言葉からインスピレーションを得て名付けられました。  
> **Kanbee(칸비)**는 일본어 **「完備 (완비: 빠짐없이 모두 갖춤)」**라는 단어에서 영감을 받아 지어진 이름입니다.

* **🇯🇵 開発の背景 (Background)**:
  ユーザーが必要とするすべての機能（コミュニケーション、予約、AI推薦）を**「完備」**したプラットフォームを作りたいという思いからスタートしました。バラバラに存在していたサービスを一つの場所でシームレスに提供し、ユーザーに最高の利便性を届けることを目標としています。
  
* **🇰🇷 개발 배경 (Background)**:
  사용자가 필요로 하는 모든 기능(커뮤니티 소통, 예약, AI 추천)을 **「완비」**한 단 하나의 플랫폼을 만들고 싶다는 생각에서 출발했습니다. 파편화되어 있던 서비스들을 한 곳에서 매끄럽게 제공하여, 사용자에게 최고의 편의성을 제공하는 것을 목표로 합니다.

---

## 1. 🚀 技術スタック / 개발 환경 (Tech Stack)

| 分類 (Category) | 技術 (Technology) |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.4.0, Spring Security, MyBatis |
| **Frontend** | HTML5, CSS3, JavaScript, Thymeleaf |
| **Database** | MySQL |
| **AI Integration**| OpenAI, Google Gemini API |
| **Build & Tool** | Gradle, Git, GitHub |

<br>

## 2. 💡 主な機能 / 주요 기능 (Features)

| 区分 (Type) | 🇯🇵 機能説明 (JP) | 🇰🇷 기능 설명 (KR) |
| :---: | :--- | :--- |
| **🔐 認証**<br>(Auth) | **Spring Security**を用いた安全なログインと権限(Admin/User)管理 | **Spring Security** 기반의 안전한 로그인 및 권한(Admin/User) 분리 관리 |
| **💬 掲示板**<br>(Board) | 自由掲示板、Q&Aなど複数カテゴリ対応。コメントとページネーション | 자유게시판, Q&A 등 다중 카테고리 지원. 댓글 및 페이징 처리 |
| **📅 予約**<br>(Reserve) | 希望日時の直感的なサービス予約と、管理者のステータス管理機能 | 원하는 날짜/시간의 직관적인 서비스 예약 및 관리자 현황 파악 |
| **🤖 AI推薦**<br>(AI) | **Gemini / OpenAI API**連携による、ユーザーの好みに合わせたスポット推薦 | **Gemini / OpenAI API** 연동으로 사용자 취향에 맞는 핫플레이스 자동 추천 |
| **🌍 多言語**<br>(i18n) | 日・韓・英のメッセージプロパティ(i18n)を適用しグローバル対応 | 일본어, 한국어, 영어 메시지 프로퍼티(i18n)를 적용하여 글로벌 환경 지원 |

<br>

## 3. 📂 プロジェクト構造 / 프로젝트 구조 (Project Structure)

MVCパターンを採用し、明確な役割分担でコードの保守性を高めました。
MVC 패턴을 도입하여 명확한 역할 분담으로 코드의 유지보수성을 높였습니다.

```text
📦 src/main
┣ 📂 java/com/kanbee/project
┃ ┣ 📂 config       # 環境設定 (Security, MyBatis, WebConfig) / 환경 설정
┃ ┣ 📂 controller   # Webリクエスト処理 / 웹 요청 처리 (라우팅)
┃ ┣ 📂 mapper       # MyBatisマッパー / 데이터베이스 접근 매퍼
┃ ┣ 📂 model        # ドメインオブジェクト(DTO, VO) / 데이터 모델
┃ ┣ 📂 security     # 認証・認可ロジック / 인증 및 인가 로직
┃ ┗ 📂 service      # コアビジネスロジック / 핵심 비즈니스 로직
┃
┗ 📂 resources
  ┣ 📂 mapper       # SQLクエリXML / SQL 쿼리 XML
  ┣ 📂 messages     # 多言語ファイル / 다국어 속성 파일 (i18n)
  ┣ 📂 static       # 静的リソース / 정적 리소스 (CSS, JS, Image)
  ┗ 📂 templates    # Thymeleafビュー / Thymeleaf 뷰 템플릿
```

<br>

## 4. 🛠 採用した技術と工夫した点 / 채택한 개발 기술 및 고민한 점

### 🇯🇵 日本語 (Japanese)
- **DB設計とMyBatis**: 将来的な拡張性を考慮し、正規化を徹底してデータの整合性を保ちました。MyBatisを活用し、複雑なSQLも柔軟に管理しています。
- **非同期処理 (Ajax)**: AI APIの呼び出し時に発生しうる遅延を考慮し、ユーザーにストレスを与えないよう非同期処理(Ajax)とローディングUIを工夫しました。
- **コンポーネント化**: Thymeleafのフラグメント(`th:replace`)を活用し、ヘッダーやフッターなどの共通UIをモジュール化して保守性を大幅に高めました。

### 🇰🇷 한국어 (Korean)
- **DB 설계 및 MyBatis**: 향후 확장성을 고려하여 정규화를 철저히 진행해 데이터 무결성을 유지했습니다. MyBatis를 활용하여 복잡한 SQL도 유연하게 관리합니다.
- **비동기 처리 (Ajax)**: AI API 호출 시 발생할 수 있는 지연 시간을 고려하여, 사용자에게 불편함을 주지 않도록 비동기 처리(Ajax)와 로딩 UI를 개선했습니다.
- **컴포넌트화 (UI 모듈화)**: Thymeleaf 프래그먼트(`th:replace`)를 적극 활용하여 헤더/푸터 등 공통 UI를 모듈화하고 코드 중복을 줄였습니다.

<br>

## 5. ⚙️ 実行方法 / 실행 방법 (How to Run)

1. **DB Setup**: MySQLで `jsl26db` を作成します。(MySQL에서 `jsl26db`를 생성합니다.)
2. **Config**: `application.properties` にデータベース情報とAI APIキーを入力します。(DB 정보와 AI API 키를 입력합니다.)
3. **Run**: ターミナルで以下のコマンドを実行します。(터미널에서 아래 명령어를 실행합니다.)
   ```bash
   ./gradlew bootRun
   ```

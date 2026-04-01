-- SQL 파일 인코딩 및 문자셋 설정 (한글/일어 등 다국어 지원을 위해 utf8mb4 사용)
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 테이블이 존재할 경우 삭제 (초기화용) - 외래 키 제약 조건을 고려하여 하위 테이블부터 삭제
DROP TABLE IF EXISTS community_comments;
DROP TABLE IF EXISTS community;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS hot_places;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- [사용자 테이블] 회원 정보를 저장합니다.
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,            -- 고유 식별자 (자동 증가)
    username VARCHAR(50) NOT NULL UNIQUE,            -- 아이디 (중복 불가)
    password VARCHAR(100) NOT NULL,                  -- 암호화된 비밀번호
    nickname VARCHAR(50) NOT NULL UNIQUE,            -- 닉네임 (중복 불가)
    role VARCHAR(20) DEFAULT 'USER',                 -- 권한 (USER, ADMIN 등)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- 가입 일시
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- [초기 사용자 데이터] 관리자 및 테스트용 계정 (비밀번호는 시큐리티 암호화 적용됨)
-- 관리자 비밀번호: admin1234**
-- 일반 유저 비밀번호: 1234
INSERT IGNORE INTO users (username, password, nickname, role) VALUES 
('superadmin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', '最高管理者 Kanbee', 'ADMIN'),
('jsl26db', '$2a$10$6Hk9/x.hM9k1s/N.pLzR.O7fXG8H7p9f5S8z1nLzX9X8yYk2n1n2m', 'Master JSL', 'USER'),
('tanaka_yui', '$2a$10$6Hk9/x.hM9k1s/N.pLzR.O7fXG8H7p9f5S8z1nLzX9X8yYk2n1n2m', '田中 結衣', 'USER'),
('sato_ken', '$2a$10$6Hk9/x.hM9k1s/N.pLzR.O7fXG8H7p9f5S8z1nLzX9X8yYk2n1n2m', '佐藤 健', 'USER'),
('ito_misaki', '$2a$10$6Hk9/x.hM9k1s/N.pLzR.O7fXG8H7p9f5S8z1nLzX9X8yYk2n1n2m', '伊藤 美咲', 'USER');

-- [예약 리스트 테이블] 사용자의 대행 예약 요청 정보를 저장합니다.
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,            -- 예약 ID
    username VARCHAR(50) NOT NULL,                   -- 요청자 아이디 (users 테이블 참조)
    category VARCHAR(50) NOT NULL,                   -- 예약 카테고리 (식당, 공연 등)
    request_time VARCHAR(100) NOT NULL,              -- 희망 일시
    headcount INT NOT NULL,                          -- 인원 수
    requirements TEXT,                               -- 상세 요구사항
    ticket_link VARCHAR(500),                        -- 관련 티켓/정보 링크 (관리자 -> 사용자)
    reference_url VARCHAR(500),                      -- 사용자가 제공하는 참고 링크 (사용자 -> 관리자)
    image_url TEXT,                                  -- 참고 이미지 경로 (긴 URL 지원)
    admin_comment TEXT,                              -- 관리자 답변/메모
    status VARCHAR(20) DEFAULT 'PENDING',            -- 상태 (PENDING, PROCESSING, COMPLETED, CANCELLED)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- 요청 일시
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- [추천 장소 테이블] 메인 화면에 표시될 핫플레이스 정보를 저장합니다.
CREATE TABLE IF NOT EXISTS hot_places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,            -- 장소 ID
    name VARCHAR(200) NOT NULL,                      -- 장소 이름
    category VARCHAR(50) NOT NULL,                   -- 카테고리
    location VARCHAR(200),                           -- 위치 (지역)
    description TEXT,                                -- 상세 설명
    image_url TEXT,                                  -- 이미지 URL (긴 URL 지원)
    tag VARCHAR(100)                                 -- 해시태그 (예: #맛집)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- [추천 장소 샘플 데이터]
INSERT IGNORE INTO hot_places (id, name, category, location, description, image_url, tag) VALUES
(1, 'プルンソグム (Blue Salt)', 'RESTAURANT', '弘大 (Hongdae)', '弘大で一番人気のサムギョプサル専門店。熟成肉の旨味が絶品です。', 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80', '美味しい #熟成肉'),
(2, 'London Bagel Museum', 'CAFE', '安国 (Anguk)', 'ソウルで今最も待ち時間が長いと言われる大人気のベーグルカフェ。', 'https://images.unsplash.com/photo-1585478259715-876acc5be8eb?auto=format&fit=crop&w=800&q=80', '超人気 #ベーグル'),
(3, 'Amore Seongsu', 'BEAUTY', '聖水 (Seongsu)', 'アモーレパシフィックが手掛ける体験型ビューティーラウンジ。', 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=800&q=80', '体験型 #コスメ'),
(4, 'Dior Seongsu', 'HOT PLACE', '聖水 (Seongsu)', '期間限定から常設へ。聖水洞のランドマークとなった美しいブティック。', 'https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?auto=format&fit=crop&w=800&q=80', 'ランドマーク #映えスポット'),
(5, '明洞餃子', 'RESTAURANT', '明洞 (Myeongdong)', 'ミシュランガイドにも掲載される、ソウルを代表するカルグクスとマンドゥの名店。', 'https://images.unsplash.com/photo-1512058560366-cd2427ffaa96?auto=format&fit=crop&w=800&q=80', '伝統の味 #ミシュラン'),
(6, 'Sulwhasoo Spa', 'SPA', '江南 (Gangnam)', '高級韓国コスメ「雪花秀」の製品を贅沢に使用した究極のヒーリングスパ。', 'https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=800&q=80', '癒やし #高級スパ');

-- [커뮤니티 게시판 테이블] 사용자 간 소통을 위한 게시글을 저장합니다.
CREATE TABLE IF NOT EXISTS community (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,            -- 게시글 ID
    author VARCHAR(100) NOT NULL,                    -- 작성자 닉네임
    title VARCHAR(200) NOT NULL,                     -- 제목
    content TEXT NOT NULL,                           -- 본문 내용
    category VARCHAR(50) DEFAULT 'TALK',             -- 카테고리 (TALK, TIP, QUESTION 등)
    image_url VARCHAR(255),                          -- 첨부 이미지 경로
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- 작성 일시
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- [커뮤니티 댓글 테이블] 게시글에 달린 댓글 정보를 저장합니다.
CREATE TABLE IF NOT EXISTS community_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,            -- 댓글 ID
    post_id BIGINT NOT NULL,                         -- 게시글 ID (community 테이블 참조)
    author VARCHAR(100) NOT NULL,                    -- 작성자 닉네임
    content TEXT NOT NULL,                           -- 댓글 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- 작성 일시
    FOREIGN KEY (post_id) REFERENCES community(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- [샘플 예약 데이터]
INSERT IGNORE INTO reservations (id, username, category, request_time, headcount, requirements, status, ticket_link, reference_url, image_url, admin_comment) VALUES 
(1, 'tanaka_yui', 'RESTAURANT', '2026-02-01 19:00', 2, '弘大のサムギョプサル店「プルンソグム」の予約をお願いします。窓際の席を希望します。', 'COMPLETED', 'https://example.com/ticket/12345', 'https://www.instagram.com/p/C2_abc/', 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80', '予約完了しました！当日は予約画面をスタッフにお見せください。'),
(2, 'sato_ken', 'POPUP', '2026-02-05 11:00', 1, '聖水洞の「LINE FRIENDS」ポップアップの入場予約をお願いします。', 'PROCESSING', NULL, 'https://map.naver.com/p/entry/place/123456', 'https://images.unsplash.com/photo-1585478259715-876acc5be8eb?auto=format&fit=crop&w=800&q=80', '現在予約枠を確認中です。少々お待ちください。'),
(3, 'ito_misaki', 'HAIR', '2026-02-10 14:00', 1, '江南の「JUNO HAIR」でレイヤーカットとカラーをお願いしたいです。', 'PENDING', NULL, 'https://www.instagram.com/junohair_official/', 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=800&q=80', NULL),
(4, 'tanaka_yui', 'CAFE', '2026-02-12 15:30', 3, '安国の「London Bagel Museum」のテイクアウト予約は可能でしょうか？', 'CANCELLED', NULL, 'https://www.instagram.com/london.bagel.museum/', NULL, '申し訳ありませんが、該当店舗は現地でのウェイティングのみとなっております。'),
(5, 'sato_ken', 'RESTAURANT', '2026-02-14 18:00', 2, '梨泰院の「Maple Tree House」予約お願いします。記念日なので静かな席がいいです。', 'COMPLETED', 'https://example.com/ticket/67890', 'https://maple-tree-house.com', 'https://images.unsplash.com/photo-1559339352-11d035aa65de?auto=format&fit=crop&w=800&q=80', '予約確定しました。記念日おめでとうございます！窓側の静かな席を確保しました。'),
(6, 'ito_misaki', 'BEAUTY', '2026-02-15 10:00', 1, '明洞の「Lienjang」でシュリンク施術を受けたいです。日本語通訳希望。', 'PROCESSING', NULL, 'http://lienjang.co.kr/jp/', NULL, '通訳スタッフの空き状況を確認しています。'),
(7, 'tanaka_yui', 'DELIVERY', '2026-02-15 20:00', 4, '東大門のホテルにキョチョンチキンのハニーコンボを届けてほしいです。', 'COMPLETED', 'https://example.com/order/chicken', NULL, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80', '配達注文完了しました！到着予定時刻は20:40です。'),
(8, 'sato_ken', 'CONCERT', '2026-03-01 18:00', 1, 'KSPOドームで行われるアイドルのコンサートチケット代行をお願いします。', 'PENDING', NULL, 'http://ticket.interpark.com/', NULL, NULL),
(9, 'ito_misaki', 'ACTIVITY', '2026-02-20 14:00', 2, '北村韓屋村で韓服体験と写真撮影の予約をお願いします。', 'COMPLETED', 'https://example.com/hanbok/res', 'https://www.instagram.com/hanbok_girl/', 'https://images.unsplash.com/photo-1583209814683-c023dd293cc6?auto=format&fit=crop&w=800&q=80', '予約完了です。ヘアセットサービスも含まれています！'),
(10, 'tanaka_yui', 'OTHER', '2026-02-22 10:00', 1, '帰国時の仁川空港までのタクシー予約をお願いします。荷物が大きいです。', 'PENDING', NULL, NULL, NULL, NULL);

-- [샘플 커뮤니티 데이터]
INSERT IGNORE INTO community (id, author, title, content, category, created_at) VALUES 
(1, '田中 結衣', '明洞でおすすめのカンジャンケジャンは？', '来週、初めて明洞に行きます。安くて美味しいカンジャンケジャンの店を教えてください！', 'QUESTION', '2026-02-15 10:00:00'),
(2, '佐藤 健', '仁川空港の配送サービスが便利でした', '空港からホテルまで荷物を送れるサービスを利用しました。手ぶらで移動できて最高です！', 'TIP', '2026-02-16 11:30:00'),
(3, '伊藤 美咲', '最近の聖水洞の雰囲気', '昨日、聖水洞に行ってきましたが、おしゃれなカフェが増えていて楽しかったです。', 'TALK', '2026-02-17 14:20:00'),
(4, '鈴木 桜', 'TAMBURINSのハンドクリーム', 'お土産にTAMBURINSのハンドクリームを買いたいのですが、フラッグシップストアは混んでいますか？', 'QUESTION', '2026-02-18 09:15:00'),
(5, '高橋 勇気', 'WOWPASSは必須アイテム！', '現金を持ち歩かなくていいので本当に便利でした。アプリで残高確認できるのも良いですね。', 'TIP', '2026-02-18 13:45:00'),
(6, '渡辺 陽子', '弘大のカフェ Onion', 'パンがとても美味しかったです！でも週末はかなり並ぶので、朝早く行くのがおすすめ。', 'INFO', '2026-02-18 15:00:00'),
(7, '山本 太郎', '一人ご飯できるお店（江南エリア）', '来月一人でソウルに行きます。江南周辺で一人でも入りやすいご飯屋さんを知りたいです。', 'QUESTION', '2026-02-18 16:30:00'),
(8, '小林 愛', 'オリーブヤングのセール情報', 'ちょうどセール期間中に行けました！マスクパックが半額で大量買いしちゃいました（笑）', 'TALK', '2026-02-18 17:00:00'),
(9, '加藤 浩二', 'ロッテワールドの制服レンタル', '彼女と制服レンタルして遊びました。恥ずかしかったけど良い思い出になりました！', 'TALK', '2026-02-18 18:20:00'),
(10, '吉田 真由美', '空港でのタックスリファンドについて', 'キオスク端末で簡単に申請できました。出国前に手続きするのを忘れないように注意です！', 'TIP', '2026-02-18 19:10:00'),
(11, '山田 花子', '漢江公園でピクニック', '天気が良かったので漢江でラーメンを食べました。コンビニで作るラーメンは格別ですね。', 'TALK', '2026-02-18 20:00:00'),
(12, '佐々木 健太', '地下鉄アプリは「Subway Korea」が良い', '日本語対応していて、乗り換え案内も正確でした。旅行中ずっと使っていました。', 'TIP', '2026-02-18 21:30:00'),
(13, '山口 美穂', 'おすすめの皮膚科ありますか？', 'シミ取りレーザーを受けたいのですが、日本語通訳がいるおすすめのクリニックを教えてください。', 'QUESTION', '2026-02-18 22:15:00'),
(14, '松本 潤', '広蔵市場のユッケ', 'ミシュランにも載っているプチョンユッケに行きました。並びましたが並ぶ価値ありです！', 'INFO', '2026-02-18 23:00:00'),
(15, '井上 マリ', 'また韓国行きたい...', '帰国したばかりですが、もう韓国料理が恋しいです。次は釜山にも行ってみたいな。', 'TALK', '2026-02-19 08:00:00'),
(16, '鈴木 一郎', 'T-moneyカードの残高払い戻し', '帰国時に残高が残ってしまったのですが、払い戻しはどこでできますか？', 'QUESTION', '2026-02-19 09:30:00'),
(17, '田中 美咲', '景福宮の夜間観覧', '夜のライトアップがとても綺麗でした！チマチョゴリを着ていくと入場無料になりますよ。', 'INFO', '2026-02-19 10:15:00'),
(18, '佐藤 健太', '東大門のナイトショッピング', '夜中の2時まで買い物してしまいました。眠いけど楽しかった！', 'TALK', '2026-02-19 11:00:00'),
(19, '高橋 エリカ', 'おすすめの韓国ドラマ', '最近見た「涙の女王」が最高でした。ロケ地巡りしたい！', 'TALK', '2026-02-19 12:45:00'),
(20, '伊藤 翔太', '辛いものが苦手な人へ', '辛くない韓国料理のおすすめリストを作ってみました。参鶏湯、プルコギ、タッカンマリ...', 'TIP', '2026-02-19 13:20:00'),
(21, '渡辺 直人', '釜山旅行の計画', 'KTXの予約はいつからできますか？週末はすぐ埋まると聞いて心配です。', 'QUESTION', '2026-02-19 14:10:00'),
(22, '小林 さくら', '韓国のカフェ文化', 'どこのカフェに入ってもWi-Fiが早くてコンセントがあるのが素晴らしいですね。', 'TALK', '2026-02-19 15:30:00'),
(23, '加藤 裕子', '美容皮膚科の予約', '人気のクリニックは1ヶ月前でも予約が取れないことがあります。早めの準備を！', 'TIP', '2026-02-19 16:45:00'),
(24, '吉田 拓也', 'ソウルの地下鉄', '乗り換えが少し複雑でしたが、アプリがあればなんとかなりました。階段が多いので歩きやすい靴で！', 'INFO', '2026-02-19 17:50:00'),
(25, '山田 隆', '南大門市場のカルグクス横丁', '安くて量が多くて美味しい！サービスで冷麺もついてきました。', 'INFO', '2026-02-19 18:30:00'),
(26, '佐々木 麻衣', '韓国のコンビニスイーツ', 'CUの延世牛乳クリームパン、やっと見つけました！クリームたっぷりで幸せ。', 'TALK', '2026-02-19 19:15:00'),
(27, '山口 健二', '明洞の両替所', '大使館前の両替所レートが一番良かったです。パスポートを忘れずに。', 'TIP', '2026-02-19 20:00:00'),
(28, '松本 香織', '漢江のラーメン自販機', 'ドラマで見て憧れてたやつ！作り方が書いてあるので簡単でした。', 'TALK', '2026-02-19 21:10:00'),
(29, '井上 太郎', '仁寺洞の伝統茶屋', '五味子茶を飲みながらゆっくりしました。雰囲気が落ち着いていて良かったです。', 'TALK', '2026-02-19 22:20:00'),
(30, '木村 花', '帰国前のソウル駅チェックイン', '荷物を預けて出国審査まで済ませられるので、空港で並ばなくて済みました。おすすめ！', 'TIP', '2026-02-20 08:30:00');

-- [샘플 댓글 데이터]
INSERT IGNORE INTO community_comments (id, post_id, author, content) VALUES
(1, 1, '最高管理者 Kanbee', '明洞なら「オダリチプ」がおすすめです！日本語も通じますし、味も安定していますよ。'),
(2, 1, '佐藤 健', '私はプロカンジャンケジャンが好きです。少し高いですが味は間違いないです！'),
(3, 2, '伊藤 美咲', '私も使いました！ホテルにチェックイン前に手ぶらで観光できるのが良いですよね。'),
(4, 4, '鈴木 桜', '平日のお昼過ぎに行きましたが、そこまで並ばずに入れましたよ！'),
(5, 7, '高橋 勇気', '江南駅近くの「ミズコンテナ」は一人でも入りやすい雰囲気でした。'),
(6, 13, '最高管理者 Kanbee', '「シャインビーム」や「リエンジャン」などは日本語対応可能で人気があります。予約代行も可能ですのでご相談ください！'),
(7, 15, '渡辺 陽子', 'わかります！私も帰国してすぐに次の航空券を検索しちゃいました（笑）'),
(8, 16, '最高管理者 Kanbee', 'コンビニ（GS25, CU, Seven Eleven）で2万ウォン以下なら払い戻し可能です。それ以上は地下鉄駅のサービスセンターへ。'),
(9, 20, '田中 結衣', 'タッカンマリ大好きです！東大門のタッカンマリ横丁がおすすめですよ。'),
(10, 26, '鈴木 桜', '私も食べました！チョコ味が一番好きです。');

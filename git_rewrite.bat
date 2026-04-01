@echo off
chcp 65001 > nul
rmdir /s /q .git
git init
git config user.name "AN-ILGWON"
git config user.email "jindohound@gmail.com"
git config i18n.commitEncoding utf-8
git config i18n.logOutputEncoding utf-8
git config core.quotepath false
git remote add origin https://github.com/AN-ILGWON/Project_KANBEE.git
git branch -M main

git add build.gradle settings.gradle gradlew gradlew.bat .gitignore src/main/resources/application.properties src/main/resources/schema.sql
git commit -m "chore: プロジェクト初期設定およびDBスキーマの構成"

git add src/main/java/com/kanbee/project/model src/main/java/com/kanbee/project/mapper src/main/resources/mapper
git commit -m "feat: ドメインモデルとMyBatisマッパーの実装"

git add src/main/java/com/kanbee/project/config src/main/java/com/kanbee/project/security
git commit -m "feat: Spring Securityを用いた認証・認可の適用"

git add src/main/java/com/kanbee/project/service src/main/java/com/kanbee/project/controller src/main/java/com/kanbee/project/KanbeeApplication.java
git commit -m "feat: コアビジネスロジックとコントローラーの追加"

git add src/main/resources/templates src/main/resources/static src/main/resources/messages
git commit -m "feat: Thymeleafを活用したUI実装と多言語対応"

git add src/
git commit -m "refactor: コードの最適化およびバグ修正"

git add README.md
git commit -m "docs: プロジェクトの概要を説明する詳細なREADMEの作成"

git add .
git commit -m "chore: 最終的なコードの整理"

git push -f -u origin main

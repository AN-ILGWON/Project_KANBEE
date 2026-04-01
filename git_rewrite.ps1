$ErrorActionPreference = "Stop"

# Set encoding to UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Remove-Item -Recurse -Force .git -ErrorAction Ignore
git init
git config user.name "AN-ILGWON"
git config user.email "jindohound@gmail.com"
git config i18n.commitEncoding utf-8
git config i18n.logOutputEncoding utf-8
git config core.quotepath false
git remote add origin https://github.com/AN-ILGWON/Project_KANBEE.git
git branch -M main

$utf8NoBom = New-Object System.Text.UTF8Encoding $false

[System.IO.File]::WriteAllText("msg1.txt", "chore: プロジェクト初期設定およびDBスキーマの構成", $utf8NoBom)
git add build.gradle settings.gradle gradlew gradlew.bat .gitignore src/main/resources/application.properties src/main/resources/schema.sql
git commit -F msg1.txt

[System.IO.File]::WriteAllText("msg2.txt", "feat: ドメインモデルとMyBatisマッパーの実装", $utf8NoBom)
git add src/main/java/com/kanbee/project/model src/main/java/com/kanbee/project/mapper src/main/resources/mapper
git commit -F msg2.txt

[System.IO.File]::WriteAllText("msg3.txt", "feat: Spring Securityを用いた認証・認可の適用", $utf8NoBom)
git add src/main/java/com/kanbee/project/config src/main/java/com/kanbee/project/security
git commit -F msg3.txt

[System.IO.File]::WriteAllText("msg4.txt", "feat: コアビジネスロジックとコントローラーの追加", $utf8NoBom)
git add src/main/java/com/kanbee/project/service src/main/java/com/kanbee/project/controller src/main/java/com/kanbee/project/KanbeeApplication.java
git commit -F msg4.txt

[System.IO.File]::WriteAllText("msg5.txt", "feat: Thymeleafを活用したUI実装と多言語対応", $utf8NoBom)
git add src/main/resources/templates src/main/resources/static src/main/resources/messages
git commit -F msg5.txt

[System.IO.File]::WriteAllText("msg6.txt", "refactor: コードの最適化およびバグ修正", $utf8NoBom)
git add src/
git commit -F msg6.txt

[System.IO.File]::WriteAllText("msg7.txt", "docs: プロジェクトの概要を説明する詳細なREADMEの作成", $utf8NoBom)
git add README.md
git commit -F msg7.txt

[System.IO.File]::WriteAllText("msg8.txt", "chore: 最終的なコードの整理", $utf8NoBom)
git add .
git commit -F msg8.txt

Remove-Item msg*.txt -ErrorAction Ignore
git push -f -u origin main

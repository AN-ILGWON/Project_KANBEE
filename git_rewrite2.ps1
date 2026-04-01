$ErrorActionPreference = "Stop"
Remove-Item -Recurse -Force .git -ErrorAction Ignore
git init
git config user.name "AN-ILGWON"
git config user.email "jindohound@gmail.com"
git config i18n.commitEncoding utf-8
git config i18n.logOutputEncoding utf-8
git config core.quotepath false
git remote add origin https://github.com/AN-ILGWON/Project_KANBEE.git
git branch -M main

git add build.gradle settings.gradle gradlew gradlew.bat .gitignore src/main/resources/application.properties src/main/resources/schema.sql
git commit -F msg1.txt

git add src/main/java/com/kanbee/project/model src/main/java/com/kanbee/project/mapper src/main/resources/mapper
git commit -F msg2.txt

git add src/main/java/com/kanbee/project/config src/main/java/com/kanbee/project/security
git commit -F msg3.txt

git add src/main/java/com/kanbee/project/service src/main/java/com/kanbee/project/controller src/main/java/com/kanbee/project/KanbeeApplication.java
git commit -F msg4.txt

git add src/main/resources/templates src/main/resources/static src/main/resources/messages
git commit -F msg5.txt

git add src/
git commit -F msg6.txt

git add README.md
git commit -F msg7.txt

git add .
git commit -F msg8.txt

Remove-Item msg*.txt -ErrorAction Ignore
git push -f -u origin main

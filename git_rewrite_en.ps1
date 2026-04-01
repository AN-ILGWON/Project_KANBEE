$ErrorActionPreference = "Stop"

Remove-Item -Recurse -Force .git -ErrorAction Ignore
git init
git config user.name "AN-ILGWON"
git config user.email "jindohound@gmail.com"
git remote add origin https://github.com/AN-ILGWON/Project_KANBEE.git
git branch -M main

git add build.gradle settings.gradle gradlew gradlew.bat .gitignore src/main/resources/application.properties src/main/resources/schema.sql
git commit -m "chore: Initial project setup and DB schema configuration"

git add src/main/java/com/kanbee/project/model src/main/java/com/kanbee/project/mapper src/main/resources/mapper
git commit -m "feat: Implement domain models and MyBatis mappers"

git add src/main/java/com/kanbee/project/config src/main/java/com/kanbee/project/security
git commit -m "feat: Apply Spring Security for authentication and authorization"

git add src/main/java/com/kanbee/project/service src/main/java/com/kanbee/project/controller src/main/java/com/kanbee/project/KanbeeApplication.java
git commit -m "feat: Add core business logic and controllers"

git add src/main/resources/templates src/main/resources/static src/main/resources/messages
git commit -m "feat: Implement Thymeleaf UI and i18n"

git add src/
git commit -m "refactor: Optimize code and fix bugs"

git add README.md
git commit -m "docs: Create detailed README for project overview"

git add .
git commit -m "chore: Final code cleanup"

git push -f -u origin main

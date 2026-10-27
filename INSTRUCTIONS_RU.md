# Запуск и сдача

1. Распакуй ZIP. Открой папку maze-escape в IntelliJ с JDK 17+.
2. В Terminal выполни по очереди:

```powershell
java tools/Check.java
java -cp out Main
```

3. Открой output/maze.html. Для обычного вида отключи zones и slides.
4. Разбери код и адаптируй работу перед сдачей; сохрани раздел AI use.
5. Создай пустой публичный репозиторий maze-escape в своём GitHub.
6. В папке проекта выполни:

```powershell
git remote add origin https://github.com/AbishevAssylzhan/maze-escape.git
git push -u origin main
```

Папка .git уже содержит историю сборки с автором-помощником. Не удаляй её.
После собственных правок: git add . → git commit → git push.
Отправь ссылку на публичный репозиторий в Moodle.

После Check всегда запускай Main: тесты заменяют картинку примером.

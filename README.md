# Дипломный проект по профессии «Тестировщик ПО»

## Описание приложения
Приложение — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:

Обычная оплата по дебетовой карте.
Уникальная технология: выдача кредита по данным банковской карты.

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:

сервису платежей, далее Payment Gate;
кредитному сервису, далее Credit Gate.
Приложение в собственной СУБД должно сохранять информацию о том, успешно ли был совершён платёж и каким способом. Данные карт при этом сохранять не допускается.

## Документы
* [План автоматизации](https://github.com/GusevaAS/QA-diploma/blob/main/docs/Plan.md)
* [Отчет по итогам тестирования](https://github.com/GusevaAS/QA-diploma/blob/main/docs/Report.md)
* [Отчет по автоматизации](https://github.com/GusevaAS/QA-diploma/blob/main/docs/Summary.md)

## ПО для работы с проектом:
- Intellij IDEA;
- Docker Desktop;
- Postman for Windows 10.13.5
- Chrome Google Браузер.

## Запуск авто-тестов
### Шаги воспроизведения:
1. Клонировать репозиторий на локальный ПК:
```
     git clone https://github.com/GusevaAS/QA_diploma
```   
2. Открыть проект в Intellij IDEA.
3. Запустить в Docker контейнеры СУБД MySQl, PostgerSQL и Node.js
4. Запустить контейнеры в терминале
``` 
docker-compose up
```
5. Запустить SUT командой

для MySQL:
``` 
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" "-Dspring.datasource.username=app" "-Dspring.datasource.password=pass" -jar ./aqa-shop.jar
```
для PostgreSQL:
```
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" "-Dspring.datasource.username=app" "-Dspring.datasource.password=pass" -jar ./aqa-shop.jar
```
Приложение должно запуститься на:
```
http://localhost:8080/
```

4. Запустить авто-тесты командой

для MySQL:
```
./gradlew test "-Dselenide.headless=true" "-Ddb.url=jdbc:mysql://localhost:3306/app" "-Ddb.username=app" "-Ddb.password=pass"
```
для PostgreSQL:
```
./gradlew test "-Dselenide.headless=true" "-Ddb.url=jdbc:postgresql://localhost:5432/app" "-Ddb.username=app" "-Ddb.password=pass"
```
Команда -Dselenide.headless=true уберет визуальное отображение UI тестов. При необходимости команду можно удалить и не использовать данный функционал.

5. Сгенерировать отчеты
``` 
./gradlew allureServe
``` 
6. Для завершения работы allureServe выполнить команду:
```
Ctrl + С далее Y
```
7. Остановить и удалить все контейнеры
``` 
docker-compose down 
``` 

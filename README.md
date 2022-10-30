# Explore-With-Me – сервис для просмотра актуальных событий

```Spring Boot``` ```Spring Data Jpa``` ```QueryDSL``` ```PostgreSQL``` ```HttpClient``` ```Swagger``` ```Postman``` ```Docker```

# PR для ревьюера: https://github.com/PavlovILYA/java-explore-with-me/pull/1

Сервис – афиша, где можно предложить какое-либо событие от выставки до похода в кино и набрать компанию для участия в нём.

# Архитектура

Сервис поделен на 4 модуля:
1. __ewm-server__   – Отдельный restful-микросервис, содержит всё необходимое для работы продукта.
2. __statistics__   – Отдельный restful-микросервис, работающий со статистикой – хранит в отдельной БД информацию о количестве обращений к эндпоинтам.
3. __stats-client__ – Модуль, содержащий необходимые классы клиентской части по обработке статистики (включен в __ewm-server__).
4. __common__       – Модуль, содержащий общие dto, исключения и константы (подмодуль, включен во все остальные модули).

# Запуск
```shell
mvn clean install
docker-compose build
docker-compose up
```

# Rest API
Swagger-спецификация [основного сервера](ewm-main-service-spec.json)

Swagger-спецификация [сервера статистики](ewm-stats-service-spec.json)

# Тесты
Postman-тесты для [основного сервера](Postman/Explore-With-Me-Main.json)

Postman-тесты для [сервера статистики](Postman/Explore-With-Me-Statistics.json)

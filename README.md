# spring-framework-blog

> [!CAUTION]
> Актуальный проект — [online-store](https://github.com/rukonpin/online-store)


Веб-приложение блога на чистом Spring Framework (без Spring Boot) с поддержкой постов, комментариев, тегов и системой лайков.

## Демонстрация

[![Демо приложения](https://img.youtube.com/vi/rk3wdP56SnA/maxresdefault.jpg)](https://www.youtube.com/watch?v=rk3wdP56SnA)
*Просматривайте на `х2`*
## Технологии

- **Backend:** Spring Framework 6.2.1, Spring MVC, Spring JDBC
- **Frontend:** Thymeleaf, HTML/CSS, JavaScript
- **База данных:**
    - H2 (для разработки и тестирования)
    - PostgreSQL 15 (для production)
- **Сервер приложений:** Apache Tomcat 10.1
- **Сборка:** Maven
- **Тестирование:** JUnit 5, Mockito, Spring Test
- **Контейнеризация:** Docker, Docker Compose

## Функциональность

### Основные возможности
- ✅ Просмотр списка постов с пагинацией
- ✅ Фильтрация постов по тегам
- ✅ Просмотр детальной информации о посте
- ✅ Создание, редактирование и удаление постов
- ✅ Добавление и удаление комментариев
- ✅ Система лайков для постов
- ✅ Загрузка изображений для постов
- ✅ Теги для категоризации контента

## Установка и запуск

### Требования
- Java 21
- Maven 3.8+
- Docker и Docker Compose

### Локальный запуск (H2)

1. Клонируйте репозиторий:

```bash
git clone https://github.com/rukonpin/spring-framework-blog.git
cd spring-framework-blog
```

2. Соберите проект:

```bash
mvn clean package
```

3. Разверните WAR-файл на Tomcat или используйте встроенный сервер IDE

4. Приложение будет доступно по адресу: `http://localhost:8080/posts`

### Запуск в Docker (PostgreSQL)

1. Соберите проект:

```bash
mvn clean package
```

2. Запустите через Docker Compose:

```bash
docker-compose up --build
```

3. Приложение будет доступно по адресу: `http://localhost:8080/posts`

4. PostgreSQL будет доступен на порту `5433` (для подключения через pgAdmin/DBeaver)

## Тестирование

### Запуск всех тестов

```bash
mvn test
```

### Структура тестов

*В проекте умышленно не было реализовано 100% покрытие*

- **Integration Tests** (`PostRepositoryImplIT`, `CommentRepositoryImplIT`) — тестирование слоя репозиториев с реальной БД (H2)
- **Unit Tests** (`PostServiceImplTest`, `CommentServiceImplTest`) — тестирование бизнес-логики с моками

## Структура проекта
```
src/
├── main/
│   ├── java/ru/github/rukonpin/
│   │   ├── config/          # Конфигурация Spring
│   │   ├── controller/      # MVC контроллеры
│   │   ├── service/         # Бизнес-логика
│   │   ├── repository/      # Работа с БД
│   │   ├── model/           # Entity классы
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── mapper/          # Маперы DTO ↔ Entity
│   │   └── exception/       # Кастомные исключения
│   ├── resources/
│   │   ├── schema.sql       # DDL для создания таблиц
│   │   ├── data-*.sql       # Тестовые данные под H2, PostgreSQL
│   │   ├── application-*.properties
│   │   └── templates/       # Thymeleaf шаблоны
│   │       ├── errors/      
│   │       ├── fragments/
│   │       └── layout/
│   └── webapp/
│       ├── styles/          # CSS
│       ├── static-images/   # Images
│       ├── icons/           # Icons
│       ├── scripts/         # JavaScript
│       └── WEB-INF/
└── test/                    # Интеграционные и unit-тесты
```

## Конфигурация

### Профили Spring
- **dev** (по умолчанию) — H2 in-memory база
- **test** — H2 in-memory база
- **docker** — PostgreSQL в контейнере


## 🐳 Docker

### Dockerfile

```dockerfile
FROM tomcat:10.1-jdk21
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/spring-framework-blog.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["catalina.sh", "run"]
```

### Docker Compose

- **postgres** — PostgreSQL 15 (порт 5433)
- **app** — Spring приложение (порт 8080)

## База данных

### Схема

- **posts** — посты блога
- **tags** — теги для категоризации
- **comments** — комментарии к постам
- **post_tags** — связь постов и тегов (many-to-many)
- **post_comments** — связь постов и комментариев (many-to-many)

### Миграции

Схема и данные загружаются автоматически при старте через `schema.sql` и `data-*.sql`.

## Особенности реализации

### Без Spring Boot
Проект намеренно использует чистый Spring Framework для демонстрации понимания внутренних механизмов:

- Ручная конфигурация через `@Configuration`
- Настройка DataSource без автоконфигурации
- Явная настройка Spring MVC и Thymeleaf
- Поддержка профилей без Spring Boot

### JDBC вместо ORM
Используется Spring JDBC Template для:

- Полного контроля над SQL
- Демонстрации работы с ResultSet
- Оптимизации сложных запросов с JOIN'ами


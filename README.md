# Автоматизация тестирования веб-сервиса покупки туров

## Описание проекта
Проект представляет собой автоматизацию тестирования комплексного веб-сервиса, предлагающего покупку туров с двумя способами оплаты: по дебетовой карте и в кредит. Автоматизированные тесты проверяют функциональность приложения, его взаимодействие с банковскими сервисами и корректность сохранения данных в различных СУБД.

##  Предварительные требования

### Необходимое программное обеспечение
Перед началом работы убедитесь, что на вашем компьютере установлено:

1. **Git** - система контроля версий
2. **Docker** и **Docker Compose** - для запуска контейнеров
3. **JDK 11 или выше** - для запуска Java-приложения
4. **Браузер** (Chrome, Firefox или Edge)

### Проверка установки
Выполните в терминале следующие команды, чтобы проверить наличие необходимых компонентов:

```bash
# Проверка версий установленного ПО
git --version
docker --version
docker-compose --version
java -version
```

##  Запуск проекта

### 1. Клонирование репозитория

```bash
# Клонируйте репозиторий
git clone <URL-вашего-репозитория>
cd qa-diploma
```

### 2. Настройка окружения с помощью Docker Compose

Проект использует три контейнера:
- **MySQL** - база данных (порт 3306)
- **PostgreSQL** - база данных (порт 5432)
- **Node.js приложение** - симулятор банковских сервисов (порт 9999)

```bash
# Запуск всех сервисов
docker-compose up -d

# Проверка статуса контейнеров
docker-compose ps
```

### 3. Настройка тестируемого приложения

Приложение (`aqa-shop.jar`) должно быть запущено отдельно:

```bash
# Запуск веб-сервиса на порту 8080
java -jar aqa-shop.jar

# Альтернативный вариант с указанием файла конфигурации
java -jar aqa-shop.jar --spring.config.location=application.properties
```

### 4. Настройка файла application.properties

Перед запуском тестов убедитесь, что в файле `application.properties` указаны правильные настройки подключения к БД:

#### Для PostgreSQL (по умолчанию):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/app
spring.datasource.username=app
spring.datasource.password=pass
```

#### Для MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/app
spring.datasource.username=app
spring.datasource.password=pass
```

##  Запуск автотестов

### Вариант 1: Запуск всех тестов (с PostgreSQL по умолчанию)

```bash
# Запуск всех тестов с настройками по умолчанию
./gradlew test
```

### Вариант 2: Запуск тестов с конкретной СУБД

```bash
# Для PostgreSQL
./gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app

# Для MySQL
./gradlew test -Ddb.url=jdbc:mysql://localhost:3306/app
```

### Вариант 3: Запуск с дополнительными параметрами

```bash
# Запуск в headless-режиме (без отображения браузера)
./gradlew test -Dselenide.headless=true

# Указание пользователя и пароля БД
./gradlew test -Ddb.user=app -Ddb.password=pass

# Изменение URL тестируемого приложения
./gradlew test -Dsut.url=http://localhost:8080
```

### Вариант 4: Параметризованный запуск для обеих СУБД

```bash
# Последовательный запуск тестов для обеих баз данных
./gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app
./gradlew test -Ddb.url=jdbc:mysql://localhost:3306/app
```

##  Генерация и просмотр отчетов Allure

### 1. Генерация отчета после выполнения тестов

```bash
# 1. Запуск тестов (если еще не выполнены)
./gradlew test

# 2. Генерация отчета Allure
./gradlew allureReport

# 3. Запуск веб-сервера с отчетом
./gradlew allureServe
```

### 2. Комбинированные команды

```bash
# Одной командой: тесты + отчет
./gradlew test allureReport allureServe

# Для конкретной СУБД с отчетом
./gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app allureReport allureServe
```

### 3. Где найти отчеты
- **Исходные данные тестов**: `build/allure-results/`
- **Сгенерированный HTML отчет**: `build/reports/allure-report/`
- **При запуске `allureServe`** отчет автоматически откроется в браузере

##  Устранение неполадок

### Проблема: Контейнеры не запускаются
```bash
# Проверка логов конкретного контейнера
docker-compose logs mysqldb
docker-compose logs postgresdb
docker-compose logs node

# Перезапуск контейнеров
docker-compose down
docker-compose up -d
```

### Проблема: Приложение не запускается
```bash
# Проверка, что порт 8080 свободен
netstat -an | grep 8080

# Запуск приложения с выводом логов
java -jar aqa-shop.jar --spring.config.location=application.properties
```

### Проблема: Тесты падают с ошибками подключения к БД
1. Убедитесь, что контейнеры запущены: `docker-compose ps`
2. Проверьте настройки в `application.properties`
3. Попробуйте подключиться к БД вручную:
   ```bash
   # Для PostgreSQL
   psql -h localhost -p 5432 -U app -d app
   # Пароль: pass
   
   # Для MySQL
   mysql -h localhost -P 3306 -u app -p app
   # Пароль: pass
   ```

### Проблема: Ошибка "Task 'allureServe' not found"
```bash
# Синхронизация Gradle проекта
./gradlew --refresh-dependencies

# Проверка доступных задач
./gradlew tasks --all
```

##  Конфигурация

### Ключевые параметры в build.gradle
```groovy
systemProperty "db.url", System.getProperty("db.url", "jdbc:postgresql://localhost:5432/app")
systemProperty "db.user", System.getProperty("db.user", "app")
systemProperty "db.password", System.getProperty("db.password", "pass")
systemProperty "sut.url", System.getProperty("sut.url", "http://localhost:8080")
```

### Основные переменные окружения

| Переменная | Значение по умолчанию | Описание |
|------------|----------------------|----------|
| `db.url` | `jdbc:postgresql://localhost:5432/app` | URL базы данных |
| `db.user` | `app` | Пользователь БД |
| `db.password` | `pass` | Пароль БД |
| `sut.url` | `http://localhost:8080` | URL тестируемого приложения |
| `selenide.headless` | `false` | Режим без GUI |

##  Примеры использования

### Полный цикл тестирования с PostgreSQL
```bash
# 1. Запуск инфраструктуры
docker-compose up -d

# 2. Запуск приложения
java -jar aqa-shop.jar

# 3. Запуск тестов
./gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app

# 4. Генерация отчета
./gradlew allureReport allureServe
```

### Быстрая проверка для MySQL
```bash
docker-compose up -d
java -jar aqa-shop.jar &
./gradlew test -Ddb.url=jdbc:mysql://localhost:3306/app -Dselenide.headless=true
./gradlew allureServe
```

##  Примечания

1. **Порядок запуска**: Сначала Docker контейнеры, затем приложение, потом тесты
2. **Время запуска**: Дайте контейнерам 30-60 секунд для полной инициализации
3. **Ресурсы**: Убедитесь, что на системе достаточно памяти для одновременной работы трех контейнеров и Java-приложения
4. **Браузер**: По умолчанию тесты используют Chrome. Убедитесь, что установлена актуальная версия






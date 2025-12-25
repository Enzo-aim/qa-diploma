package data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

// Клас для подключения к базам SQL
public class DataSql {
    // URL для подключения к базе данных
    // Пример: jdbc:mysql://localhost:3306/mydatabase
    private static final String url = System.getProperty("db.url");

    // Имя пользователя БД
    private static final String user = System.getProperty("db.user");

    // Пароль пользователя БД
    private static final String password = System.getProperty("db.password");

    //Метод очищает (удаляет все данные из) указанных таблиц в базе данных.
    public static void cleanTables() {
        //SQL-запрос для удаления всех записей из order_entity
        String deleteOrderEntity = "DELETE FROM order_entity";

        // SQL-запрос для удаления всех записей из таблицы payment_entity
        String deletePaymentEntity = "DELETE FROM payment_entity";

        // SQL-запрос для удаления всех записей из таблицы credit_request_entity
        String deleteCreditRequestEntity = "DELETE FROM credit_request_entity";

        // Создание экземпляра QueryRunner для выполнения SQL-запросов
        QueryRunner runner = new QueryRunner();

        // try-with-resources автоматически закрывает соединение
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            // Выполнение SQL-запросов на удаление данных
            runner.update(connection, deleteOrderEntity);
            runner.update(connection, deletePaymentEntity);
            runner.update(connection, deleteCreditRequestEntity);
        } catch (SQLException e) {

            // Обработка ошибок БД
            e.printStackTrace();
        }
    }

    public static String findPayStatus() {
        // SQL-запрос для получения статуса из таблицы платежей
        String statusSql = "SELECT status FROM payment_entity";

        try {
            // 1. Установка подключения к БД
            Connection connection = DriverManager.getConnection(url, user, password);

            // 2. Создание Statement для выполнения SQL-запроса
            Statement statement = connection.createStatement();

            // 3. Выполнение запроса и получение ResultSet
            ResultSet resultSet = statement.executeQuery(statusSql);

            // 4. Проверка наличия данных в результате
            if (resultSet.next()) {

                // Возврат значения колонки "status" из первой строки
                return resultSet.getString("status");
            } else {
                return "статус не найден";
            }
        } catch (SQLException e) {
            // Обработка ошибок БД
            e.printStackTrace();
            return "ОШИБКА: Не удалось получить статус покупки из базы данных.";
        }
    }

    public static String findCreditStatus() {
        // SQL-запрос для получения статуса из таблицы кредитных заявок
        String statusSql = "SELECT status FROM credit_request_entity";
        try {
            // ШАГ 1: Устанавливаем соединение с базой данных
            // Используем параметры url, user, password из класса DataSql
            Connection connection = DriverManager.getConnection(url, user, password);

            // ШАГ 2: Создаем Statement объект для выполнения SQL-запроса
            Statement statement = connection.createStatement();

            // ШАГ 3: Выполняем SQL-запрос и получаем ResultSet (результаты запроса)
            ResultSet resultSet = statement.executeQuery(statusSql);

            // ШАГ 4: Проверяем есть ли данные в ResultSet
            // метод next() перемещает курсор на первую строку и возвращает true если данные есть
            if (resultSet.next()) {

                // ШАГ 5: Если данные есть - извлекаем значение колонки "status" из текущей строки
                return resultSet.getString("status");
            } else {
                // ШАГ 6: Если данных нет - возвращаем сообщение об отсутствии статуса
                return "статус не найден";
            }
        } catch (SQLException e) {
            // ШАГ 7: Обрабатываем исключения при работе с базой данных
            // Выводим stack trace для отладки
            e.printStackTrace();
            return "ОШИБКА: Не удалось получить статус покупки в кредит из базы данных.";
        }
    }

    public static long getOrderEntityCount() {
        // SQL-запрос для подсчета количества записей в таблице order_entity
        // COUNT(*) - агрегатная функция, возвращает общее количество строк в таблице
        String countSql = "SELECT COUNT(*) FROM order_entity";

        // ШАГ 1: Используем try-with-resources для автоматического закрытия соединения
        // Connection закроется автоматически при выходе из блока try
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            // ШАГ 2: Создаем объект QueryRunner из библиотеки Apache Commons DbUtils
            // QueryRunner упрощает выполнение SQL-запросов и обработку результатов
            QueryRunner runner = new QueryRunner();

            // ШАГ 3: Выполняем SQL-запрос и обрабатываем результат
            // - connection: активное соединение с БД
            // - countSql: SQL-запрос для выполнения
            // - new ScalarHandler<>(): обработчик результата, который извлекает одно значение
            //   ScalarHandler возвращает первый столбец первой строки результата
            Long count = runner.query(connection, countSql, new ScalarHandler<>());

            // ШАГ 4: Проверяем и возвращаем результат
            // Если count не равен null - возвращаем значение, иначе возвращаем 0
            return count != null ? count : 0;
        } catch (SQLException e) {
            // ШАГ 5: Обрабатываем исключения при работе с базой данных
            // Выводим stack trace для отладки (в логи или консоль)
            e.printStackTrace();
            // При возникновении ошибки возвращаем 0
            return 0;
        }
    }// ШАГ 6: Автоматическое закрытие Connection благодаря try-with-resources
    // Даже если произойдет исключение, соединение будет корректно закрыто
}





# language: ru
@00005 @db
Функция: Тестирование PostgreSQL с именованными SQL

  Предыстория:
    * сохранить SQL запрос как "create_users"
      """
      CREATE TABLE IF NOT EXISTS users (
          id SERIAL PRIMARY KEY,
          name VARCHAR(100),
          age INT
      )
      """
    * отправить во временную базу данных запрос "create_users"

  Сценарий: Создание таблицы и вставка данных

    * сохранить SQL запрос как "insert_ivan"
      """
      INSERT INTO users (name, age)
      VALUES ('Иван', 30)
      """
    * отправить во временную базу данных запрос "select_ivan" и проверить параметры
      | name | age |
      | Иван | 30  |
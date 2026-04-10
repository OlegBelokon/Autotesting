# language: ru
@00005 @tempDb
Функция: Тестирование PostgreSQL

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

  Сценарий: Создание таблицы, вставка данных и проверка

    * сохранить SQL запрос как "insert_ivan"
      """
      INSERT INTO users (name, age)
      VALUES ('Иван', 30)
      """
    * отправить во временную базу данных запрос "insert_ivan"
    * сохранить SQL запрос как "select_ivan"
      """
      SELECT name, age FROM users
      WHERE name = 'Иван'
      """
    * отправить во временную базу данных запрос "select_ivan" и проверить параметры
      | name | Иван |
      | age  | 30   |
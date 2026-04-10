# language: ru
@00006 @db
Функция: Тестирование PostgreSQL

  Сценарий: Добавление пользователя в таблицу testers и проверка

    * сохранить SQL запрос как "insert_tester"
      """
      INSERT INTO testers (name, age)
      VALUES ('Сюзанна', 23)
      """
    * отправить в базу данных запрос "insert_tester"

    * сохранить SQL запрос как "select_tester"
      """
      SELECT name, age FROM testers
      WHERE name = 'Сюзанна'
      """
    * отправить в базу данных запрос "select_tester" и проверить параметры
      | name | Сюзанна |
      | age  | 23      |
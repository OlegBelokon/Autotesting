# language: ru
@api
Функция: Тестирование REST API (jsonplaceholder)

  Сценарий: Получение списка пользователей
    Допустим я настроил базовый URL API "https://jsonplaceholder.typicode.com"
    Когда я отправляю GET запрос на "/users"
    Тогда статус ответа 200
    И массив "$" содержит 10 элементов
    И в ответе поле "[0].name" равно "Leanne Graham"

  Сценарий: Создание нового поста
    Допустим я настроил базовый URL API "https://jsonplaceholder.typicode.com"
    Когда я отправляю POST запрос на "/posts" с телом:
      """
      {
        "title": "foo",
        "body": "bar",
        "userId": 1
      }
      """
    Тогда статус ответа 201
    И в ответе поле "title" равно "foo"
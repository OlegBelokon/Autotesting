# language: ru
@rest
Функция: Тестирование REST API (jsonplaceholder)

  Сценарий: Получение списка пользователей
    * отправляю GET запрос "getUsers" и сохраняю ответ как "usersList"
    * проверяю что ответ "usersList" содержит параметры
      | [0].id | 1 |
      | [0].name | "Leanne Graham" |

  Сценарий: Создание нового поста через POST
    * отправляю POST запрос на "createPost" из файла "json/create_post.json" с параметрами
      | title | My new post |
      | body  | Content of the post |
    * проверяю что ответ "" содержит параметры
      | title | My new post |
      | id    | 101 |
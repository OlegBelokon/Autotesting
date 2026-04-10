# language: ru
@rest
Функция: Тестирование REST API с плейсхолдерами

  Сценарий: GET пользователя по ID
    * отправляю GET запрос "getUserById" с параметрами пути и сохраняю ответ как "user"
      | id | 1 |
    * проверяю что ответ "user" содержит параметры
      | id   | 1 |
      | name | "Leanne Graham" |

  Сценарий: POST – создание поста
    * сохраняю URL как "postUrl" для POST запроса "createPost" с параметрами пути
      | id | 1 |
    * отправляю POST запрос по сохранённому URL "postUrl" из файла "json/create_post.json" с параметрами JSON
      | title | My new title |
      | body  | Content here |
    * проверяю что ответ последнего запроса содержит параметры
      | title | My new title |
      | id    | 101 |

  Сценарий: PUT – обновление поста
    * сохраняю URL как "updateUrl" для PUT запроса "updatePost" с параметрами пути
      | id | 1 |
    * отправляю PUT запрос по сохранённому URL "updateUrl" из файла "json/update_post.json" с параметрами JSON
      | title | Updated! |
    * проверяю что ответ последнего запроса содержит параметры
      | title | Updated! |
      | id    | 1 |
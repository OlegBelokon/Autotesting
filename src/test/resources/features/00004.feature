# language: ru
@api @kafka
Функция: Тестирование REST API (jsonplaceholder)

  Сценарий: Получение списка пользователей
    * я отправляю в топик "test-topic" json сообщение c параметрами
      | order.id       | 100500 |
      | customer.name  | Иван   |
      | customer.age   | 30     |
      | items[0].name  | Книга  |
      | items[0].price | 499    |

    * в топике "test-topic" появляется сообщение с параметрами
      | order.id      | 100500 |
      | customer.name | Иван   |
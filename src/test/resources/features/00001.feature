# language: ru
@00001 @kafka
Функция: Тестирование Kafka с шаблонами JSON

  Сценарий: Отправка заказа из файла с параметрами
    * Я отправляю в топик "test-topic" json сообщение "json/order_template.json" c параметрами
      | order.id      | 100500         |
      | customer.name | Иван Хуепутало |

    * в топике "test-topic" появляется сообщение с параметрами
      | order.id      | 100500         |
      | customer.name | Иван Хуепутало |
package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.JsonUtil;
import utils.KafkaConsumerUtil;
import utils.KafkaProducerUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaSteps {
    private KafkaProducerUtil producer = new KafkaProducerUtil();
    private KafkaConsumerUtil consumer;

    /**
     * Отправка JSON сообщения, построенного из таблицы параметров.
     * Пример:
     *   Я отправляю в топик "orders" json сообщение c параметрами
     *     | order.id      | 12345   |
     *     | customer.name | John    |
     *     | order.total   | 299.99  |
     */
    @Given("я отправляю в топик {string} json сообщение c параметрами")
    public void sendJsonMessageWithParams(String topic, DataTable table) throws Exception {
        Map<String, String> params = toMap(table);
        // Базовый JSON — пустой объект, к которому применяются параметры
        String finalJson = JsonUtil.applyParameters("{}", params);
        producer.sendSync(topic, null, finalJson);
        producer.close();
    }

    @Then("в топике {string} появляется сообщение с параметрами")
    public void verifyMessageWithParams(String topic, DataTable table) throws Exception {
        Map<String, String> expectedParams = toMap(table);
        System.out.println("[KAFKA] Ожидаемые параметры: " + expectedParams);

        consumer = new KafkaConsumerUtil(topic);
        boolean found = false;
        long start = System.currentTimeMillis();
        long timeout = 5000;
        List<String> allMessages = new ArrayList<>();

        while (System.currentTimeMillis() - start < timeout && !found) {
            List<String> messages = consumer.pollMessages(Duration.ofSeconds(1));
            if (!messages.isEmpty()) {
                System.out.println("[KAFKA] Получено сообщений: " + messages.size());
                for (String msg : messages) {
                    allMessages.add(msg);
                    System.out.println("[KAFKA] Сообщение: " + msg);
                    if (JsonUtil.matchesParameters(msg, expectedParams)) {
                        found = true;
                        System.out.println("[KAFKA] ✅ Сообщение подходит! Текст: " + msg);
                        break;
                    } else {
                        System.out.println("[KAFKA] ❌ Сообщение не подходит: " + msg);
                    }
                }
            } else {
                System.out.println("[KAFKA] Ожидание сообщений...");
            }
        }

        consumer.close();

        if (!found) {
            System.err.println("[KAFKA] ОШИБКА: Не найдено сообщение с параметрами " + expectedParams);
            System.err.println("[KAFKA] Все полученные сообщения (" + allMessages.size() + "):");
            for (int i = 0; i < allMessages.size(); i++) {
                System.err.println("  " + (i+1) + ": " + allMessages.get(i));
            }
            assertTrue(false, "Не найдено сообщение с параметрами: " + expectedParams);
        } else {
            System.out.println("[KAFKA] Проверка пройдена успешно.");
        }
    }
    /**
     * Отправка JSON сообщения, загруженного из файла, с применением параметров из таблицы.
     * Пример:
     *   Я отправляю в топик "test-topic" json сообщение "json/order.json" c параметрами
     *     | order.id      | 200 |
     *     | customer.name | Anna |
     */
    @Given("Я отправляю в топик {string} json сообщение {string} c параметрами")
    public void sendJsonFileWithParams(String topic, String filePath, DataTable table) throws Exception {
        Map<String, String> params = toMap(table);
        String baseJson = JsonUtil.loadJsonFromFile(filePath);
        String finalJson = JsonUtil.applyParameters(baseJson, params);
        producer.sendSync(topic, null, finalJson);
        producer.close();
    }

    private Map<String, String> toMap(DataTable table) {
        Map<String, String> map = new HashMap<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> row : rows) {
            if (row.size() >= 2) {
                map.put(row.get(0), row.get(1));
            }
        }
        return map;
    }
}
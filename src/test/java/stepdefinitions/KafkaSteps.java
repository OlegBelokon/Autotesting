package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.JsonUtil;
import utils.KafkaConsumerUtil;
import utils.KafkaProducerUtil;

import java.time.Duration;
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
        consumer = new KafkaConsumerUtil(topic);
        boolean found = false;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 5000 && !found) {
            List<String> messages = consumer.pollMessages(Duration.ofSeconds(1));
            for (String msg : messages) {
                if (JsonUtil.matchesParameters(msg, expectedParams)) {
                    found = true;
                    break;
                }
            }
        }
        consumer.close();
        assertTrue(found, "Не найдено сообщение с параметрами: " + expectedParams);
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
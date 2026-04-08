package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.KafkaConsumerUtil;
import utils.KafkaProducerUtil;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaSteps {

    private KafkaProducerUtil producer = new KafkaProducerUtil();
    private KafkaConsumerUtil consumer;

    @Given("я отправляю в топик {string} сообщение {string}")
    public void sendMessage(String topic, String message) throws Exception {
        producer.sendSync(topic, null, message);
    }

    @Then("в топике {string} появляется сообщение {string}")
    public void verifyMessage(String topic, String expectedMessage) {
        consumer = new KafkaConsumerUtil(topic); // group.id берётся из properties
        consumer.pollMessages(Duration.ofSeconds(5));
        boolean found = consumer.getAllMessagesSinceSubscribe().contains(expectedMessage);
        consumer.close();
        assertTrue(found);
    }
}

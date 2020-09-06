package kafka.learning;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class KafkaConsumerDemo {
    public static void main(String[] args) {

        //CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> KafkaConsumerThread::run);

//        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<String, String>(getConsumerProperty());
//        kafkaConsumer.subscribe(Arrays.asList("first_topic"));
//        while(true) {
//            ConsumerRecords<String, String> consumerRecord =
//                    kafkaConsumer.poll(Duration.ofMillis(10000));
//            consumerRecord.forEach(x -> {
//                System.out.println("Key: "+ x.key());
//                System.out.println("Offset:"+x.offset());
//                System.out.println("Partition: "+ x.partition());
//            });
//        }
        KafkaConsumerThread thread = new KafkaConsumerThread();
        CompletableFuture completableFuture = CompletableFuture.runAsync(() -> thread.run());

        completableFuture.join();
    }


    public static Properties getConsumerProperty() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my_first_application");
        return properties;
    }

    static class KafkaConsumerThread implements Runnable {

        public void run() {
            KafkaConsumer<String, String> kafkaConsumer = null;
            try {
                kafkaConsumer = new KafkaConsumer<String, String>(getConsumerProperty());
                kafkaConsumer.subscribe(Arrays.asList("first_topic"));
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
                for(ConsumerRecord record: consumerRecords){
                    System.out.println("Message read is: " + record.value() + " with key: " + record.key() +
                            " from partition: " + record.partition());
                }
            } catch (WakeupException e) {
                System.out.println("wakeup exception occurred");
            } finally {
                kafkaConsumer.close();
            }
        }
    }
}

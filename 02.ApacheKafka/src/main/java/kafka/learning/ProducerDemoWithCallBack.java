package kafka.learning;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithCallBack {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class.getName());


        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        for(int i=0;i<10;i++) {
            String key = "id_" + String.valueOf(i);
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>("first_topic", key, ("hello word "+ String.valueOf(i)));
            System.out.println("key: "+ key);
            kafkaProducer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
//                        logger.info("Produced message in topic:{} ", recordMetadata.topic());
//                        logger.info("Produced message in parition:{} ", recordMetadata.partition());
//                        logger.info("Produced message at offser:{} ", recordMetadata.offset());
                        //logger.info("Produced message at time:{} ", recordMetadata.timestamp());
                        System.out.println("Produced message in topic:{} "+ recordMetadata.topic());
                        System.out.println("partition: "+recordMetadata.partition());
                    } else {
                        logger.error("Error occurred: " + e);
                    }
                }
            }).get(); // message send is asynchronous, to wait for message to publish we may use flush method
            // get() is a blocking send, we wont do it in production. Just for testing
        }
        kafkaProducer.flush();
        kafkaProducer.close(); // close the producer
    }
}

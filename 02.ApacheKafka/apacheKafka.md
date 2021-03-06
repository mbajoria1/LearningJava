# Apache Kafka is: 
1. Distributed
2. Resilient
3. Fault tolerant
4. Have really low latency close to 10 ms at times
5. Horizontal scalability, can scale by adding servers

**Use Cases:**
1. Messaging System
2. Activity tracking
3. App logs gatering
4. Stream processing with Kafka Stream APIs
5. Integration with different big data technologies like
    Spark, Storm, Hadoop etc.
    
### Topics , Partitions & offsets:
 
  ##### Topics:
 * A stream of data
 * Similar to tables in database.
 * We can have as many topics as possible
 * A topic is identified by its name
 
 ##### Partitions:
 
 * Topics are splitted into multiple **partitions**.
 * While creating a topic we have to mention number 
    of partitions which is modifiable later.
 * You can have as many partitions as possible and index starts at 0.
 * Each partition is ordered.
 
 ##### Offset:
 * Each message in a partition gets incremental id called offset.
 * Offset is true for specific partition, offset 2 for partition 0 and 1 can have totally different messages.
 * Once an offset is written that cant be reverted or rewritten, you can write to the next offset only.
 * Kafka has messages for a specific period after that messages are deleted. Default is 1 week. 
    But offsets always keep on increasing , it never resets to 0.
    
 ![Img1](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/Kafka%20Topics%20Partitions%20Offsets.PNG)
 
 ### Brokers & Topics:
 
 * Kafka has clusters containing multiple brokers (servers)
 
 * Each broker is identified by an integer number (Always a number) For Example: Broker 101, Broker 102 etc.
 
 * Each broker will have certain partitions (from same or different topics)
 
 * Once you are connected to one broker(bootstrap broker) , you are connected to entire cluster
 
 * A good number to start with is 3 brokers but some big cluster can have upto 100 brokers.
 
 ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/Brokers.PNG)
 
 ### Replication Factor & Leader: 
 
 * Each Topics should have replication factor > 1. Usually 2 or 3.
 * Replication helps in better availability so if one broker goes down , others can still
   serve the data.
   
 * Below is an example of replication factor of 2 for a topic with 2 partitions:
   
  ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/ReplicationFactor.PNG)
 
 * For a partition in a topic only one broker can be leader at a given time and other brokers are in-synch replicas(ISRs).
 * Only the leader can receive and serve data for that partition.
  
  ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/LeadersInReplication.PNG)
  
  ### Producers:
  
  * Producers can write data to topics (containing multiple partitions).
  * Producers automatically know that it will write to which partition & which broker.
  * In case of broker failures producer will automatically recover.
  * Producers can choose to get an acknowledgement for writes. There are 3 types of configurations.
     1. ack = 0, producers don't wait for ack. (Risk of data loss)
     2. ack = 1, producers will wait for leader broker to respond back with ack.(Limited data loss) - this is default option.
     3. ack = all, Leader+ replicas ack (No data loss)  
     
  #### Message keys in producers:
   
   * A key can be string or number and producers can choose to send a key with a message.    
   * Message key by default will be null and in this case, messages published by prodcuers will be 
     load balanced in round robin fashion.
   * Unlike round-robin, messages with same non null key will always go to same partition. This key could be used if we want to order 
     messages in a particular partition. 
        
   Default load balancing between multiple brokers from one producer:
        
   ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/Producers.PNG)
   
 #### Kafka consumers
 
 * Kafka consumer reads messages from topic.
 * Consumer will automatically know from which partition to read from
 * Similar to producer if any broker fails, consumer knows how to recover
 * Consumer work in consumer groups, each group will be reading from specific set of partitions
 * If we have more consumers than partitions then some of them will be inactive
 
 ##### Consumer offset
 
 * Kafka stores offsets from which a consumer has been reading
 * Offsets are commited live in a topic names __consumer_offsets.
 * When a consumer has processed the data from a partition it should be committing data
   to the topic
 * If a consumer dies, it will be able to read from the point where it left off. Because of consumer offset
 
 ##### Delivery semantics for Kafka consumer
 
 * Consumers can choose when to commit the offset.
 
 **at-most-once**
    offsets are commited as soon message is received. Chance of data loss.
    as if the processing goes wrong, the data is lost.
    <br/>
 **at-least-once**
    offsets are commited once the received message has been processed.
    If processing goes wrong, you can read the data again.
    As there are chances of duplicate processing, idempotency sollution must be in place
    to not impact your application.
    <br/>
 **exactly-once**
     This is kafka => kafka processing using Kafka stream APIS.
     In case of kafka => external system, idempotent consumer needs to be used.
     This ensures that message is delivered exactly once without duplication.
     
 <br/>
 
 #### Kafka broker discovery
 
 * Each kafka broker is a bootstrap server. 
 * What that means is you need to connect
   to only one broker and you are connected to entire cluster.
 * Each broker knows about all other brokers, partitions, topics(metadata)
 
 Any kafka client(producer/consumer) initially sends a connection metadata request to a broker.
 Broker responds back with the metadata information which has all broker details.
 Now client can connect to any of the broker as needed.
 
 #### Zookeeper
 
 * Manages all brokers
 * Have leader & follower zookeeprs. Leader managers kafka writes, where as follower manages reads
 * It sends a notification to kafka when a topic is created, deleted, borker goes down or broker is up again.
 * It manages selecting partition leader
 * Kafka cannot work with zookeeper
 * Zookeeper doesn's store consumer offset anymore starting version > v0.10
 * Zookeeper by design operate on odd number of servers
 
 #### Kafka guarantees
 
 ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/Kafka-Guarantees.PNG)
 
 #### Theory in one glance
 
 ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/Kafka%20theory.PNG)
 
 
 ## Kafka topic create through CLI
 
 To run Kafka CLI we need perform follow below steps sequentially. 
 
 * First we need to install Apache Kafka in your system from site: https://kafka.apache.org/downloads
 * After extracting , place the extracted kafka folder under c:/ driver for Windows system.
 * Inside kafka folder, we need to create a data folder which will have another two folders (kafka & zookeper). 
 * Under Kafka extracted folder, there will ne a config folder.We need to update server.properties & zookeeper.properties 
    file to point to correct kafka & zookeeper folder path just created.
 
 * Once above is done, we can start first the zookeeper server & then kafka (broker server). 
 <br/>
 ` zookeeper-server-start.bat config/zookeeper.properties `   - to start zookeeper
 <br/>
 ` kafka-server-start.bat config/server.properties `   - to start kafka broker
 
 <br/>
 
 ##### Topic Create command
  
 ` kafka-topic --zookeeper localhost:2181 --topic first_topic --create --partitions 3 --replication-factor 1`  
 <br/>
 > topic named first-topic will be created   
 > Remember: you can have replication factor as many as number of brokers, not more than that.
 > Once topic is created, we can produce and consume through CLI using below command. 
 
 ` kafka-console-producer --broker-list localhost:9092 --topic first_topic ` 
 
 > Here, broker-list is list of kafka servers running.

` kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic `
<br/>
## Kafka Producer / Consumer through Java program:
<br/>
For maven project below dependency needs to be added.

```
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>2.2.0</version>
    </dependency>

```
### Producers:
<br/>
* For Producer, we need to create a set of properties to define Kafka bootstrap-server, key & value serializer
  as Kafka stores messages in bytes , and from java class, we need to define serialize class (string, integer or custom class objects).
  
* Once KafkaProducer is created, ProducerRecord can be created which has overloaded constructor but one of them
  takes a topic name & a message.

* Now we can call `send()` method to start sending messages to kafka topic.
* We can define callback function as well, where KafkaProducer returns a metadata which contains many details like
  message key, value, partition & offset at which message was produced.

* ` kafkaProducer.flush() ` can be used to produce in blocking manner in which it will wait until all messages has been delivered.

* Its important to close the producer after messages are published using ` kafkaProducer.close()  `
<br/>

### Consumer:
<br/>

* For KafkaConsumer also it takes a list of properties but unlike producer , it takes deserializer as java needs to convert bytes into Java objects.

* Next consumer needs to subscribe to a topic.

* Then consumer will poll for any new messages. Below method returns a ConsumerRecords objects which we can
   be iterated for reading consumed message's details. 

`   kafkaConsumer.poll(Duration.ofMillis(10000)) ` 
<br/>

Below contains source code for producer & consumer demo code.

![](https://github.com/mbajoria1/LearningJava/tree/master/02.ApacheKafka/src/main/java/kafka/learning )
<br/>

       
       
   
       
    
      
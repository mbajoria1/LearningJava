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
 
  #####Topics:
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
  
  ![](https://github.com/mbajoria1/LearningJava/blob/master/02.ApacheKafka/LeadersInReplication.PNG)   
   
       
    
      
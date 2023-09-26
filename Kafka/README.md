# Start Zookeeper:
zookeeper-server-start.sh Kafka/zookeeper.properties 
KafkaUserPePePe
# Start Kafka Server:
kafka-server-start.sh Kafka/server.properties 

# (Optional): Initialize topic:
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test_topic 

# (Optional): To consume via console:
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic text_topic --from-beginning

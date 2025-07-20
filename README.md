This is a simple Spring Boot app to demonstrate sending and receiving of messages in Kafka using spring-kafka




docker run -p 9092:9092 -d bashj79/kafka-kraft
docker exec -it trusting_williams /bin/bash

cd /opt/kafka/bin

# create topic 'my-first-topic'
sh kafka-topics.sh --bootstrap-server localhost:9092 --create --topic my-first-topic --partitions 1 --replication-factor 1

# list topics
sh kafka-topics.sh --bootstrap-server localhost:9092 --list



sh kafka-topics.sh --bootstrap-server localhost:9092 --create  --replication-factor 1 --partitions 1 --topic mytopic
sh kafka-topics.sh --bootstrap-server localhost:9092 --create  --replication-factor 1 --partitions 1 --topic codingkiddo
sh kafka-topics.sh --bootstrap-server localhost:9092 --create  --replication-factor 1 --partitions 5 --topic partitioned
sh kafka-topics.sh --bootstrap-server localhost:9092 --create  --replication-factor 1 --partitions 1 --topic filtered
sh kafka-topics.sh --bootstrap-server localhost:9092 --create  --replication-factor 1 --partitions 1 --topic greeting

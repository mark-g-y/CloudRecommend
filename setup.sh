sudo apt-get update
sudo apt-get install default-jre
sudo apt-get install scala

sudo mkdir ~/programs

wget https://mirror.csclub.uwaterloo.ca/apache/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz
tar -xvf zookeeper-3.4.6.tar.gz
sudo mkdir ~/programs/zookeeper
sudo mv zookeeper-3.4.6 ~/programs/zookeeper

wget https://mirror.csclub.uwaterloo.ca/apache/kafka/0.9.0.1/kafka_2.10-0.9.0.1.tgz
tar -xvzf kafka_2.10-0.9.0.1.tgz
sudo mkdir ~/programs/kafka
sudo mv kafka_2.10-0.9.0.1 ~/programs/kafka
echo "delete.topic.enable=true" >> ~/programs/kafka/kafka_2.10-0.9.0.1/config/server.properties

wget https://mirror.csclub.uwaterloo.ca/apache/pig/pig-0.15.0/pig-0.15.0.tar.gz
tar -xvzf pig-0.15.0.tar.gz
sudo mkdir ~/programs/pig
sudo mv pig-0.15.0 ~/programs/pig

ZOOKEEPER="~/programs/zookeeper/zookeeper-3.4.6/bin"
KAFKA="~/programs/kafka/kafka_2.10-0.9.0.1/bin"
PIG="~/programs/pig/pig-0.15.0/bin"
JAVA_HOME="/usr/lib/jvm/java-7-openjdk-i386"
FULLPATH=PATH=$PATH:$ZOOKEEPER:$KAFKA:$PIG:$JAVA_HOME
echo $FULLPATH | sudo tee /etc/environment
source /etc/environment

SET_JAVA_HOME="export JAVA_HOME=$JAVA_HOME"
echo $SET_JAVA_HOME | sudo tee -a ~/.bashrc
source ~/.bashrc

sudo chmod -R 777 ~/programs

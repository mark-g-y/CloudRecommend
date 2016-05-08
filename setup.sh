sudo apt-get update
sudo apt-get install default-jre
sudo apt-get install openjdk-7-jdk
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

wget https://mirror.csclub.uwaterloo.ca/apache/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz
tar -xvzf hadoop-2.7.2.tar.gz
sudo mkdir ~/programs/hadoop
sudo mv hadoop-2.7.2 ~/programs/hadoop

ZOOKEEPER="~/programs/zookeeper/zookeeper-3.4.6/bin"
KAFKA="~/programs/kafka/kafka_2.10-0.9.0.1/bin"
PIG="~/programs/pig/pig-0.15.0/bin"
HADOOP="~/programs/hadoop/hadoop-2.7.2/bin"
HADOOPSH="~/programs/hadoop/hadoop-2.7.2/sbin"
JAVA_HOME="/usr/lib/jvm/java-7-openjdk-i386"
FULLPATH=PATH='$PATH':$ZOOKEEPER:$KAFKA:$PIG:$HADOOP:$HADOOPSH:$JAVA_HOME
echo export $FULLPATH | sudo tee -a ~/.bashrc

SET_JAVA_HOME="export JAVA_HOME=$JAVA_HOME"
echo $SET_JAVA_HOME | sudo tee -a ~/.bashrc
source ~/.bashrc

HADOOP_HOME="$HOME/programs/hadoop/hadoop-2.7.2"

echo export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native | sudo tee -a /$HADOOP_HOME/etc/hadoop/hadoop-env.sh
echo export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib" | sudo tee -a $HADOOP_HOME/etc/hadoop/hadoop-env.sh
echo export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-i386 | sudo tee -a $HADOOP_HOME/etc/hadoop/hadoop-env.sh

sudo chmod -R 777 ~/programs

sudo apt-get install python-pip
sudo pip install Jinja2

sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle

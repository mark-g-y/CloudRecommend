# CloudRecommend

CloudRecommend is a distributed, scalable, and fault tolerant recommendation engine intended to be deployed as a cloud-based service. Web developers can use an API to add recommendation features to their website, whether it be for ecommerce or social media.

### Required Software
- Apache Zookeeper
- Apache Kafka
- Apache Hadoop
- Apache HBase
- MongoDB

You will also need the following Python packages (can be installed using pip):
- Jinja2
- happybase
- Flask
- mongoengine
- pymongo

### Architecture
![alt tag](https://raw.githubusercontent.com/mark-g-y/CloudRecommend/master/documentation/Architecture.png)

### Setting Up
Start all the external required software (Zookeeper, Kafka, Hadoop, HBase, and MongoDB)

Start each EventReceiver by navigating into "event_receiver" and doing 
```sh
java -jar EventReceiver.jar <kafka_zookeeper_host:port> <hdfs_uri:port> <optional numProcesses>
```

Start the RecommendationScheduler by navigating into "recommendation_scheduler" and doing 
```sh
java -jar RecommendationScheduler.jar <myPort> <mongoDB_host> <mongoDB_port> 
```

Start each worker by navigating into "worker" and doing 
```
python worker.py <scheduler_host> <scheduler_port> <hdfs_host> <hdfs_port> <hbase_host> <hbase_port> <mongodb_host> <mongodb_port>
```

Start each Client API by navigating into "api" doing 
```
java -jar Api.jar <myPort> <hbase_zookeeper_uri> <hbase_zookeeper_port>
```

Start the Client Webpage Admin by navingating into "admin_page" and doing 
```
python main.py<scheduler_host> <scheduler_port> <mongodb_host (optional)> <mongodb_port (optional)>
```
Note that the default MongoDB host and port will be used if not specified

### How to Use
Although the architecture diagram appears confusing, as a user of this service you don't need to worry about most of it. After you've set everything up, the APIs are actually very easy to use.

First, go to the client admin webpage - this is localhost:5000 unless you configured otherwise. There, you can create a site, events for that site, the affinity score for an event, and how often the recommendations should be run. For example, an ecommerce site might have "view", "like", "purchase" events, scored 1, 3, and 10 respectively. Notice that each event is an action a user can perform on an item, and the more affinity for that item, the higher the score.

Next, whenever a user performs one of the aforementioned events, send a message to Apache Kafka's 'events' channel in the following format:
```
{
    "site_id" : site_id,
    "user" : user,
    "item" : item,
    "event" : event
}
```
Site ID is the ID of the site, as determined from the admin web console. User is the unique ID of the user performing the event. Item is the item ID. Event is the event name, as specified in the web console (from our example, this would be either 'like', 'view', or 'purchase').

When you want to query the recommendations, simply make an HTTP request to the client API. 

If you would like recommendations for a user, make a request to /recommendations/user with an "user" and a "site" param (user ID and site ID respectively).

If you would like recommendations for an item, make a request to /recommendations/item with an "item" and a "site" param (item ID and site ID respectively).

And that's it! As a user, you don't need to worry about the underlying works, it's all abstracted away for you.

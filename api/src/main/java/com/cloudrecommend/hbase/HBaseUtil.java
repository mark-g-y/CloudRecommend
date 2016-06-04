package com.cloudrecommend.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;

public class HBaseUtil {

    public static Result get(String tableName, String key) {

        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", ZookeeperConfig.getInstance().host());
        config.set("hbase.zookeeper.property.clientPort", Integer.toString(ZookeeperConfig.getInstance().port()));

        Result result = null;
        try {
            HTable table = new HTable(config, tableName);
            result = table.get(new Get(key.getBytes()));
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

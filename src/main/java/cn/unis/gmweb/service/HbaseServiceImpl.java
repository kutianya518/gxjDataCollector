package cn.unis.gmweb.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.utils.ConfigTable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Service;

import cn.unis.gmweb.utils.DateUtil;


@SuppressWarnings("deprecation")
@Service
public class HbaseServiceImpl implements HbaseService {

    @Resource
    private HbaseTemplate hbaseTemplate;

    @Override
    public void insertIntoHbase(String hbaseTableName, ConcurrentHashMap<String, List<String>> tmpData) {
        hbaseTemplate.execute(hbaseTableName, new TableCallback<String>() {
            @Override
            public String doInTable(HTableInterface table) throws Throwable {
                String family = familyMatch(hbaseTableName);
                ArrayList<Put> puts = new ArrayList<>();
                for (Map.Entry<String, List<String>> entry : tmpData.entrySet()) {
                    String[] tmpkey = entry.getKey().split(",");
                    String rowKey = "";
                    if (ConfigTable.alarmTable.toString().equals(hbaseTableName)) {
                        rowKey = tmpkey[1] + tmpkey[0];
                    } else {
                        rowKey = tmpkey[0] + tmpkey[1];
                    }
                    Put put = new Put(Bytes.toBytes(rowKey));
                    put.addColumn(Bytes.toBytes(family), Bytes.toBytes("Id"), Bytes.toBytes(tmpkey[0]));
                    put.addColumn(Bytes.toBytes(family), Bytes.toBytes("SaveTime"), Bytes.toBytes(DateUtil.dateTimeTodateString(new Date(Long.valueOf(tmpkey[1])), DateUtil.DATE_TIME_PATTERN)));
                    for (String value : entry.getValue()) {
                        String[] tmp = value.split(",");
                        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(tmp[1]), Bytes.toBytes(tmp[0]));
                    }
                    puts.add(put);
                }
                table.put(puts);
                return "ok";
            }
        });
    }

    @Override
    public void insertIntoBkcHbase(String hbaseTableName, List<String> pumpData) {

        hbaseTemplate.execute(hbaseTableName, new TableCallback<String>() {
            @Override
            public String doInTable(HTableInterface table) throws Throwable {
                String family = familyMatch(hbaseTableName);
                ArrayList<Put> puts = new ArrayList<>();
                for (String pump : pumpData) {
                    String[] pumpKeyValue = pump.split(",");
                    String rowKey = "";
                    if (ConfigTable.diagnosisTable.toString().equals(hbaseTableName)) {
                        rowKey = DateUtil.dateStringTodateTime(pumpKeyValue[1], DateUtil.DATE_TIME_PATTERN).getTime() + pumpKeyValue[0];
                    } else {
                        rowKey = pumpKeyValue[0] + DateUtil.dateStringTodateTime(pumpKeyValue[1], DateUtil.DATE_TIME_PATTERN).getTime();
                    }
                    Put put = new Put(Bytes.toBytes(rowKey));
                    for (String pumpKV : pumpKeyValue) {
                        String[] keyValue = pumpKV.split("=");
                        if ("null".equals(keyValue[1])) {
                            //......
                        } else {
                            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(keyValue[0]), Bytes.toBytes(keyValue[1]));
                        }
                    }
                    puts.add(put);
                }
                table.put(puts);
                return "ok";
            }
        });




    }

    public String familyMatch(String hbaseTableName) {
        String family = "";
        switch (hbaseTableName) {
            case "xj_ht_data":
                family = "Ht";
                break;
            case "xj_qdl_ycdata":
                family = "QdlYc";
                break;
            case "xj_qdl_ymdata":
                family = "QdlYm";
                break;
            case "xj_alarm_data":
                family = "Alarm";
                break;
            case "bkc_data_rt":
                family = "unis_cf";
                break;
            case "bkc_diagnosis_data":
                family = "Diagnosis";
                break;
            default:
                break;
        }
        return family;
    }

}

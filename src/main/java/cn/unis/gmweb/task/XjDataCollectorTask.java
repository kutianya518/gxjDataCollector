package cn.unis.gmweb.task;

import cn.unis.gmweb.dynamic.CustomerContextHolder;
import cn.unis.gmweb.pojo.HtWarn;
import cn.unis.gmweb.pojo.PumpWarn;
import cn.unis.gmweb.service.HbaseService;
import cn.unis.gmweb.service.TreeService;
import cn.unis.gmweb.thresholdmodel.ModelWarning;
import cn.unis.gmweb.utils.ConfigTable;
import cn.unis.gmweb.utils.DateUtil;
import cn.unis.gmweb.utils.ProperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用spring调度，定时写在配置文件中
 * 许继数据采集
 *
 * @author lgf
 */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("xj")
public class XjDataCollectorTask {
    private Logger logger = LoggerFactory.getLogger(XjDataCollectorTask.class);
    @Autowired
    private TreeService treeService;
    @Autowired
    private HbaseService hbaseService;

    @Async
    //@RequestMapping("/qdlyc")
    public void qdlYcCollector() {
        String st=ProperUtil.getPro("st");
        String et=ProperUtil.getPro("et");
        dataCollector(st,et,ConfigTable.qdl_sb_type.toString(), ConfigTable.qdl_ycTable.toString(), "遥测");
        logger.error("全电量-----遥测采集完毕");
    }

    @Async
    //@RequestMapping("/qdlym")
    public void qdlYmCollector() {
        String st=ProperUtil.getPro("st");
        String et=ProperUtil.getPro("et");
        dataCollector(st,et,ConfigTable.qdl_sb_type.toString(), ConfigTable.qdl_ymTable.toString(), "遥脉");
        logger.error("全电量-----遥脉采集完毕");


    }
    @Async
    //@RequestMapping("/htyc")
    public void htYcCollector() {
        String st=ProperUtil.getPro("st");
        String et=ProperUtil.getPro("et");
        dataCollector(st,et,ConfigTable.ht_sb_type.toString(), ConfigTable.ht_ycTable.toString(), "遥测");
        logger.error("火探-----遥测采集完毕");
    }
    @Async
    public void alarmCollector() {
        String st=ProperUtil.getPro("st");
        String et=ProperUtil.getPro("et");
        List<String> alarmList = treeService.queryAlarmData(st,et);
        //alarmList.add("1000241,2018-12-04 17:00:05,温度告警,东B1配电室");
        ConcurrentHashMap<String, List<String>> tmpData = new ConcurrentHashMap<>();
        for (String alarm : alarmList) {
            String[] tmpAlarm = alarm.split(",");
            String key = tmpAlarm[0] + "," + DateUtil.dateStringTodateTime(tmpAlarm[1], DateUtil.DATE_TIME_PATTERN).getTime();
            String desc = tmpAlarm[2] + "," + "Descriptions";
            String Positions = tmpAlarm[3] + "," + "Positions";
            List<String> dataList = new ArrayList<>();
            dataList.add(desc);
            dataList.add(Positions);
            tmpData.put(key, dataList);
        }
        if (tmpData.size() != 0) {
            hbaseService.insertIntoHbase(ConfigTable.alarmTable.toString(), tmpData);
        }
        logger.error("火探-----告警采集完毕");
    }

    /**
     * 数据采集入库
     * @param sb_type 设备类型（"全/火"）
     * @param hbaseTableName 表名
     * @param c_type 遥测/遥脉
     */

    public void dataCollector(String st,String et,String sb_type, String hbaseTableName, String c_type) {
        List<String> qyidList = getAllId(sb_type);
        LinkedHashMap<String, List<String>> ycMap = getAllCdMap(c_type, qyidList);
        for (Map.Entry<String, List<String>> entry : ycMap.entrySet()) {
            String qyid = entry.getKey();
            List<String> yclist = entry.getValue();
            ConcurrentHashMap<String, List<String>> tmpData = new ConcurrentHashMap<>();
            for (String ycid : yclist) {
                //一个区域一分钟的数据
                getOneDayQyData(tmpData, c_type, qyid, ycid, st, et);
            }
            if (tmpData.size() == 0) {
                logger.error(String.format("表名为:%s,区域id为:%s,没有查到数据，入库数据量为：%s"
                        , hbaseTableName, qyid, tmpData.size()));
                continue;
            } else {
                for (Map.Entry<String, List<String>> entry2 : tmpData.entrySet()) {
                    if (entry2.getValue().size() == 1) tmpData.remove(entry2.getKey());
                }
            }
            if (tmpData.size() != 0) {
                insertIntoDB(hbaseTableName,tmpData);
            }
            switch (hbaseTableName) {
                case "xj_qdl_ymdata":
                    if (tmpData.size() != 1) {
                        logger.error(String.format("表名为:%s,区域id为:%s,整点遥脉入库数据量为：%s"
                                , hbaseTableName, qyid, tmpData.size()));
                    }
                    break;
                default:
                    if (tmpData.size() != 12) {
                        logger.error(String.format("表名为:%s,区域id为:%s,整点遥测入库数据量为：%s", hbaseTableName, qyid, tmpData.size()));
                    }
                    break;
            }
        }
    }

    /**
     * 获取所有id
     *
     * @param sb_type 全/火
     * @return id集合
     */
    public List<String> getAllId(String sb_type) {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.bigdata);
        List<String> idlist = treeService.getAllQyId(sb_type);
        return idlist;
    }

    /**
     * 获取所有全电量设备（区域）的遥测id,key实际为许继设备id，值为yc集合
     * 获取所有全电量设备（区域）的遥信id,key实际为许继设备id，值为ym集合
     * 获取所有火探的遥测id,key实际为许继漏电流测点id，值为yc（漏电流id与温度id）集合
     *
     * @param c_type 测点类型
     * @param qylist 区域id集合
     * @return
     */
    public LinkedHashMap<String, List<String>> getAllCdMap(String c_type, List<String> qylist) {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.bigdata);
        LinkedHashMap<String, List<String>> ycmap = new LinkedHashMap<>();
        for (String qyid : qylist) {
            List<String> ycId = treeService.getAllYcId(qyid, c_type);
            ycmap.put(qyid, ycId);
        }
        return ycmap;
    }

    /**
     * 获取一个区域一天的数据
     *
     * @param tmpData 临时数据集聚map
     * @param c_type  测点类型
     * @param qyid    区域id
     * @param ycid    测点id
     * @param st      开始时间
     * @param et      结束时间
     * @return
     */
    public  void getOneDayQyData(ConcurrentHashMap<String, List<String>> tmpData, String c_type, String qyid, String ycid, String st, String et) {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.sems8000);
        String year = DateUtil.dateTimeTodateString(null, DateUtil.YEAR_PATTERN);
        String id = ycid.split(",")[0];
        String c_code = ycid.split(",")[1];
        int mode = Integer.valueOf(id) % 10;
        String tableName = "";
        if ("遥测".equals(c_type)) {
            tableName = String.format("hisanalog_%s_0%s", year, mode);
            //一分钟抽取一次
            List<String> yc_data_list = treeService.queryYcData(tableName, id, st, et);
            updateTmpData(tmpData, yc_data_list, qyid, c_code);
        } else {
            tableName = String.format("hisaccumulator_%s_0%s", year, mode);
            List<String> ym_data_list = treeService.queryYmData(tableName, id, st, et);
            updateTmpData(tmpData, ym_data_list, qyid, c_code);
        }
    }

    /**
     * 更新数据集map
     * @param tmpData 临时数据集
     * @param data_list  单个测点一分钟的数据集合
     * @param qyid  区域id
     * @param c_code 测点编码
     */
    public void updateTmpData(ConcurrentHashMap<String, List<String>> tmpData, List<String> data_list, String qyid, String c_code) {
        for (String data : data_list) {
            String[] tmpArr = data.split(",");
            String key = qyid + "," + DateUtil.dateStringTodateTime(tmpArr[0], DateUtil.DATE_TIME_PATTERN).getTime();//改为时间戳
            String value = tmpArr[2] + "," + c_code;
            if (tmpData.containsKey(key)) {
                if (Double.valueOf(tmpArr[2]) != 0) {
                    List<String> dataList = tmpData.get(key);
                    dataList.add(value);
                    tmpData.put(key, dataList);
                }
            } else {
                List<String> dataList = new ArrayList<>();
                String valueT = tmpArr[1] + "," + "DateTime";
                dataList.add(valueT);
                if (Double.valueOf(tmpArr[2]) != 0) {
                    dataList.add(value);
                }
                tmpData.put(key, dataList);
            }
        }
    }

    /**
     * 入库
     * @param hbaseTableName hbase表名
     * @param tmpData 原始数据
     */
     void insertIntoDB(String hbaseTableName,ConcurrentHashMap<String, List<String>> tmpData){
        if (ConfigTable.ht_ycTable.toString().equals(hbaseTableName)){
            //火探数据
            //1、查询模型，tmpData为同一区域时间段的数据
            String qyid=tmpData.keys().nextElement().split(",")[0];
            LinkedHashMap<String, String> lineHtModel= treeService.findLineHtThresholdModelMap(qyid);
            //2、模型计算
            if (lineHtModel!=null){
                ModelWarning.htThresholdCalculation(tmpData,lineHtModel);
                //3、入库mysql
                List<HtWarn> htWarnList= getHtWarn(tmpData);
                if (htWarnList.size()!=0){
                   // System.err.println(tmpData);
                    treeService.insertHtWarnMysql(htWarnList);
                }
            }

        }
         hbaseService.insertIntoHbase(hbaseTableName,tmpData);

    }
    public List<HtWarn> getHtWarn(ConcurrentHashMap<String, List<String>> tmpData){
         List<HtWarn> htWarnList = new ArrayList<>();
         for (Map.Entry<String,List<String>> ht:tmpData.entrySet()){
             //qyid,timestamp
             String[] ht_key = ht.getKey().split(",");
             //ivalue,I   tvalue,T timevaluestr,DateTime   red,warnLevel I,T,warnArguments threshold
             List<String> ht_value = ht.getValue();
             //创建htwarn
             HtWarn htWarn= new HtWarn();
             htWarn.setQyid(ht_key[0]);
             htWarn.setSaveTime(new Date(Long.valueOf(ht_key[1])));
             Class htWarnClass=htWarn.getClass();
             for (String str:ht_value){
                 String [] kv=str.split("=");
                 //set htwarn
                 try {
                     Field htWarnField=htWarnClass.getDeclaredField(kv[1]);
                     if (htWarnField!=null){
                         htWarnField.setAccessible(true);
                         htWarnField.set(htWarn,kv[0]);
                     }

                 } catch (NoSuchFieldException e) {
                     //e.printStackTrace();如果warnLevel为normal，则没有此属性
                 } catch (IllegalAccessException e) {
                     e.printStackTrace();
                 }
             }
             //判断warnLevel不为normal 则htwarn.add
             if (!"normal".equals(htWarn.getWarnLevel())){
                 htWarnList.add(htWarn);
             }
         }
         return htWarnList;
    }

}

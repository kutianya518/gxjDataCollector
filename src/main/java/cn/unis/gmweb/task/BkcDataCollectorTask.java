package cn.unis.gmweb.task;

import cn.unis.gmweb.dynamic.CustomerContextHolder;
import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.pojo.PumpDiagnosis;
import cn.unis.gmweb.pojo.PumpWarn;
import cn.unis.gmweb.service.HbaseService;
import cn.unis.gmweb.service.MongoService;
import cn.unis.gmweb.service.TreeService;
import cn.unis.gmweb.thresholdmodel.Model;
import cn.unis.gmweb.utils.ConfigTable;
import cn.unis.gmweb.utils.DateUtil;
import cn.unis.gmweb.utils.ProperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用spring调度，定时写在配置文件中
 * 必可测数据采集
 *
 * @author lgf
 */
@Service
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("bkc")
public class BkcDataCollectorTask {
    private Logger logger = LoggerFactory.getLogger(BkcDataCollectorTask.class);
    @Autowired
    private TreeService treeService;
    @Autowired
    private MongoService mongoService;

    @Autowired
    private HbaseService hbaseService;

    /**
     * 水泵详情数据采集入口
     */
    @Async
    public void bkcDetailsCollector() {
        try {
            //1、查询水泵全电量qyid Db_MapId,MachineID,sid
            List<String> pumpIaList = treeService.findAllPumpQyid();
            String st = ProperUtil.getPro("st");
            String et = ProperUtil.getPro("et");
            //2、查询属性映射集Db_MapId,C_code
            HashMap<String, String> id_code = treeService.queryMapIdAndCode();
            //验证一下state,code
            HashMap<String, String> state_code = treeService.queryStateAndCode();
            for (String pump : pumpIaList) {
                //Pump=Db_MapIaId,MachineID,sid
                String[] pumpArray = pump.split(",");
                //根据Ia判断水泵是否运行
                ConcurrentHashMap<Long, List<String>> tmpData = getQdlYcData(pumpArray[0], st, et);
                Iterator<List<String>> iteratorQdlData = tmpData.values().iterator();
                Double Ia = iteratorQdlData.hasNext() ? Double.valueOf(iteratorQdlData.next().get(4).split(",")[0]) : 0;
                //3、封装PumpDetails
                LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap = new LinkedHashMap<>();
                if (Ia > 1) {
                    //采集GSSB1   振动
                    mongoService.setVibrationData(pumpArray, st, et, id_code, pumpDetailsLinkedHashMap);
                    if (pumpDetailsLinkedHashMap.size() != 0) {
                        //采集GSSB1 工艺
                        mongoService.setProcessData(pumpArray, st, et, id_code, pumpDetailsLinkedHashMap);
                        //采集健康状态值
                        mongoService.setEquipmentHealth(pumpArray, pumpDetailsLinkedHashMap, state_code);
                        //采集流量值
                        treeService.setBkc_flow_big(st, et, pumpDetailsLinkedHashMap);
                        //采集强电数据
                        setQdlYcData(pumpDetailsLinkedHashMap, tmpData);
                        //阈值模型计算
                        LinkedHashMap<String, String> pumpModelMap = treeService.findThresholdModelMap(pumpArray[2]);
                        Model.ThresholdCalculation(pumpDetailsLinkedHashMap, pumpModelMap);
                    }
                }
                //入库
                for (PumpDetails pumpDetails : pumpDetailsLinkedHashMap.values()) {
                    System.err.println(pumpDetails.toString1());
                }
                //insertIntoDB(ConfigTable.bkcTable.toString(), pumpDetailsLinkedHashMap);
            }
            logger.error("水泵详情-----采集完毕");
        } catch (Exception e) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            e.printStackTrace(pout);
            String ret = new String(out.toByteArray());
            logger.error("水泵详情采集出错:" + ret);
            e.printStackTrace();
        }
    }

    /**
     * 诊断详情数据采集入口
     */
    @Async
    public void bkcDiagnosisCollector() {
        try {
            String st = ProperUtil.getPro("st");
            String et = ProperUtil.getPro("et");
            //1、查询水泵轴承 (MachineID,c_id,c_code)
            HashMap<String, HashMap<String, String>> pumpCidMap = treeService.findAllPumpCid();
            //2、查询状态集
            HashMap<String, String> state_code = treeService.queryStateAndCode();
            //3、查询诊断数据
            for (Map.Entry<String, HashMap<String, String>> pump : pumpCidMap.entrySet()) {
                List<String> pumpDiagnosisList = getExpertHistory(st, et, pump, state_code);
                //4、入库
//                for (String string : pumpDiagnosisList) {
//                    System.err.println(string);
//                }
                hbaseService.insertIntoBkcHbase(ConfigTable.diagnosisTable.toString(), pumpDiagnosisList);
            }
            logger.error("水泵诊断-----采集完毕");
        } catch (Exception e) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            e.printStackTrace(pout);
            String ret = new String(out.toByteArray());
            logger.error("水泵诊断采集出错:" + ret);
        }

    }

    /**
     * 入库hbase and mysql
     * @param hbaseTableName hbase表名
     * @param pumpDetailsLinkedHashMap 水泵详情Map
     */
    public void insertIntoDB(String hbaseTableName, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap) {
        List<String> pumpDetailsToHbaseList = new ArrayList<>();
        List<PumpWarn> pumpWarnList = new ArrayList<>();
        for (PumpDetails pumpDetails : pumpDetailsLinkedHashMap.values()) {
            pumpDetailsToHbaseList.add(pumpDetails.toString());
            if (!"normal".equals(pumpDetails.getWarnLevel())) {
                //封装pumpWarn
                PumpWarn pumpWarn = new PumpWarn();
                pumpWarn.setMachineId(pumpDetails.getMachineID());
                pumpWarn.setDataTime(DateUtil.dateStringTodateTime(pumpDetails.getDataTime(),DateUtil.DATE_TIME_PATTERN));
                pumpWarn.setWarnLevel(pumpDetails.getWarnLevel());
                pumpWarn.setWarnArguments(pumpDetails.getWarnArguments());
                pumpWarn.setThreshold(pumpDetails.getThreshold());
                pumpWarnList.add(pumpWarn);
            }
        }
        if (pumpDetailsToHbaseList.size() != 0) {
            hbaseService.insertIntoBkcHbase(hbaseTableName, pumpDetailsToHbaseList);
        }
        if (pumpWarnList.size() != 0) {
            //入库mysql
            treeService.insertMysql(pumpWarnList);
        }
    }

    /**
     * set 历史诊断
     * @param st startTime
     * @param et endTime
     * @param pump 水泵数据集
     * @param state_code 状态编码
     * @return 水泵诊断详情
     */
    public List<String> getExpertHistory(String st, String et, Map.Entry<String, HashMap<String, String>> pump, HashMap<String, String> state_code) {
        LinkedHashMap<String, PumpDiagnosis> pumpDiagnosisLinkedHashMap = mongoService.getExpertHistory(st, et, pump, state_code);
        List<String> pumpDiagnosisList = new ArrayList<>();
        for (PumpDiagnosis pumpDiagnosis : pumpDiagnosisLinkedHashMap.values()) {
            pumpDiagnosisList.add(pumpDiagnosis.toString());
        }
        return pumpDiagnosisList;
    }

    /**
     * 获取一分钟内全电量数据
     *
     * @param qyid 水泵全电量区域id
     * @param st   开始时间
     * @param et   结束时间
     * @return map数据集
     */
    public ConcurrentHashMap<Long, List<String>> getQdlYcData(String qyid, String st, String et) {
        //通过qyid查找测点id
        CustomerContextHolder.setCustomerType(CustomerContextHolder.bigdata);
        //ycid:c_id,c_code
        List<String> ycList = treeService.getAllPumpQdlYcId(qyid, "遥测");
        ConcurrentHashMap<Long, List<String>> tmpData = new ConcurrentHashMap<>();
        //一个区域一分钟的数据
        for (String ycid : ycList) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.sems8000);
            String year = DateUtil.dateTimeTodateString(null, DateUtil.YEAR_PATTERN);
            String id = ycid.split(",")[0];
            String c_code = ycid.split(",")[1];
            int mode = Integer.valueOf(id) % 10;
            String tableName = String.format("hisanalog_%s_0%s", year, mode);
            //一分钟抽取一次savetime,curdatatime,calvalue
            List<String> yc_data_list = treeService.queryYcData(tableName, id, st, et);
            for (String data : yc_data_list) {
                String[] tmpArr = data.split(",");
                long key = DateUtil.dateStringTodateTime(tmpArr[0], DateUtil.DATE_TIME_PATTERN).getTime();//改为时间戳
                String value = tmpArr[2] + "," + c_code;
                if (tmpData.containsKey(key)) {
                    List<String> dataList = tmpData.get(key);
                    dataList.add(value);
                    tmpData.put(key, dataList);
                } else {
                    List<String> dataList = new ArrayList<>();
                    dataList.add(value);
                    tmpData.put(key, dataList);
                }
            }
        }
        return tmpData;
    }

    /**
     * set 水泵全电量数据
     * @param pumpDetailsLinkedHashMap 水泵数据集
     * @param tmpData set后的数据集
     */
    private void setQdlYcData(LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap, ConcurrentHashMap<Long, List<String>> tmpData) {
        for (PumpDetails pump : pumpDetailsLinkedHashMap.values()) {
            long pumpTimeStamp = DateUtil.dateStringTodateTime(pump.getDataTime(), DateUtil.DATE_TIME_PATTERN).getTime();
            long pumpDateTimeKey = pumpTimeStamp;
            Enumeration<Long> enumeration = tmpData.keys();
            //查找最接近全电量数据
            long min = Long.MAX_VALUE;
            while (enumeration.hasMoreElements()) {
                long next = enumeration.nextElement();
                long tmp = Math.abs(next - pumpTimeStamp);
                if (tmp == 0) {
                    pumpDateTimeKey = next;
                    break;
                } else if (tmp < min) {
                    min = tmp;
                    pumpDateTimeKey = next;
                }
            }
            List<String> qdlData = tmpData.get(pumpDateTimeKey);
            //set全电量数据
            for (String qdl : qdlData) {
                String qdlValue = qdl.split(",")[0];
                String qdlField = qdl.split(",")[1].toLowerCase();
                try {
                    Field field = pump.getClass().getDeclaredField(qdlField);
                    field.setAccessible(true);
                    field.set(pump, qdlValue);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}

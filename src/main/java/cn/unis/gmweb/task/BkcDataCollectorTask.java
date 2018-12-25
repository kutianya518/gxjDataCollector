package cn.unis.gmweb.task;

import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.pojo.PumpDiagnosis;
import cn.unis.gmweb.pojo.Tree;
import cn.unis.gmweb.service.HbaseService;
import cn.unis.gmweb.service.MongoService;
import cn.unis.gmweb.service.TreeService;
import cn.unis.gmweb.utils.ConfigTable;
import cn.unis.gmweb.utils.ProperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 利用spring调度，定时写在配置文件中
 * 必可测数据采集
 *
 * @author lgf
 */
@Service
@CrossOrigin(origins = "*", maxAge = 3600)
public class BkcDataCollectorTask {
    private Logger logger = LoggerFactory.getLogger(BkcDataCollectorTask.class);
    @Autowired
    private TreeService treeService;
    @Autowired
    private MongoService mongoService;

    @Autowired
    private HbaseService hbaseService;

    @Async
    public void bkcDetailsCollector() {
        try {
            //1、查询水泵Ia Db_MapId,MachineID,sid
            List<String> pumpIaList = treeService.findAllPumpIa();
            String st = ProperUtil.getPro("st");
            String et = ProperUtil.getPro("et");
            //2、查询属性映射集Db_MapId,C_code
            HashMap<String, String> id_code = treeService.queryMapIdAndCode();
            //验证一下state,code
            HashMap<String, String> state_code = treeService.queryStateAndCode();
            for (String pump : pumpIaList) {
                //Pump=Db_MapIaId,MachineID,sid
                String[] pumpArray = pump.split(",");
                Boolean Iboolean = treeService.queryIa(pumpArray[0]);
                //3、封装PumpDetails
                LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap = new LinkedHashMap<>();
                if (!Iboolean) {
                    //采集GSSB1 振动
                    mongoService.setVibrationData(pumpArray, st, et, id_code, pumpDetailsLinkedHashMap);
                    //采集GSSB1 工艺
                    mongoService.setProcessData(pumpArray, st, et, id_code, pumpDetailsLinkedHashMap);
                    //采集健康状态值
                    mongoService.setEquipmentHealth(pumpArray, pumpDetailsLinkedHashMap, state_code);
                    //采集流量值
                    treeService.setBkc_flow_big(st, et, pumpDetailsLinkedHashMap);
                } else {
                    //.....
                }
                //入库
//                for (PumpDetails pumpDetails : pumpDetailsLinkedHashMap.values()) {
//                    System.err.println(pumpDetails.toString());
//                }
                insertIntoBkcHbase(ConfigTable.bkcTable.toString(), pumpDetailsLinkedHashMap);
            }
            logger.error("水泵详情-----采集完毕");
        } catch (Exception e) {
            logger.error("水泵详情采集出错:" + e.getMessage());
        }
    }

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
            logger.error("水泵诊断采集出错:" + e.getMessage());
        }

    }

    public void insertIntoBkcHbase(String hbaseTableName, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap) {
        List<String> pumpDetailsList = new ArrayList<>();
        for (PumpDetails pumpDetails : pumpDetailsLinkedHashMap.values()) {
            pumpDetailsList.add(pumpDetails.toString());
        }
        hbaseService.insertIntoBkcHbase(hbaseTableName, pumpDetailsList);
    }

    public List<String> getExpertHistory(String st, String et, Map.Entry<String, HashMap<String, String>> pump, HashMap<String, String> state_code) {
        LinkedHashMap<String, PumpDiagnosis> pumpDiagnosisLinkedHashMap = mongoService.getExpertHistory(st, et, pump, state_code);
        List<String> pumpDiagnosisList = new ArrayList<>();
        for (PumpDiagnosis pumpDiagnosis : pumpDiagnosisLinkedHashMap.values()) {
            pumpDiagnosisList.add(pumpDiagnosis.toString());
        }
        return pumpDiagnosisList;
    }
}

package cn.unis.gmweb.service;

import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.pojo.PumpDiagnosis;
import cn.unis.gmweb.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MongoServiceImpl implements MongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void setVibrationData(String[] pumpArray, String st, String et, HashMap<String, String> id_code, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.and("InsertDate").greaterThanEquals(DateUtil.dateStringTodateTime(st,DateUtil.MINUTE_PATTERN)).
                lessThan(DateUtil.dateStringTodateTime(et,DateUtil.MINUTE_PATTERN)).and("MachineID").is(pumpArray[1]);
        BasicDBObject fieldDBObject = new BasicDBObject();
        Query query = new BasicQuery(queryBuilder.get(), fieldDBObject);
        List<DBObject> vibrationDataList = mongoTemplate.find(query, DBObject.class, "VibrationData");
        //System.err.println(vibrationDataList.toString());
        JSONArray jsonArray = JSONArray.parseArray(vibrationDataList.toString());
        for (Object object : jsonArray) {
            PumpDetails pumpDetails = new PumpDetails();
            pumpDetails.setMachineID(pumpArray[2]);
            JSONObject jsonObject = JSON.parseObject(object.toString());
            String tm = jsonObject.getJSONObject("InsertDate").getString("$date");
            pumpDetails.setDataTime(DateUtil.utcStringToLocalString(tm, DateUtil.DATE_TIME_PATTERN));//转换为分级别
            String MachineTestId = jsonObject.getString("MachineTestId");
            JSONArray VibrationTestDataList = jsonObject.getJSONArray("VibrationTestDataList");
            for (Object v_object : VibrationTestDataList) {
                JSONObject v_jsonObject = JSON.parseObject(v_object.toString());
                String id = v_jsonObject.getString("LocationID");
                String fypp = v_jsonObject.getString("fypp");
                if ("DJ_shock".equals(id_code.get(id))) {
                    pumpDetails.setDJ_shock(fypp);
                } else if ("SB_shock".equals(id_code.get(id))) {
                    pumpDetails.setSB_shock(fypp);
                }
            }
            pumpDetailsLinkedHashMap.put(MachineTestId, pumpDetails);
        }
    }

    @Override
    public void setProcessData(String[] pumpArray, String st, String et, HashMap<String, String> id_code, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.and("InsertDate").greaterThanEquals(DateUtil.dateStringTodateTime(st,DateUtil.MINUTE_PATTERN)).
                lessThan(DateUtil.dateStringTodateTime(et,DateUtil.MINUTE_PATTERN)).and("MachineID").is(pumpArray[1]);
        BasicDBObject fieldDBObject = new BasicDBObject();
        Query query = new BasicQuery(queryBuilder.get(), fieldDBObject);
        List<DBObject> processDataList = mongoTemplate.find(query, DBObject.class, "ProcessData");
        //System.err.println(processDataList.toString());
        JSONArray jsonArray = JSONArray.parseArray(processDataList.toString());
        for (Object object : jsonArray) {
            JSONObject jsonObject = JSON.parseObject(object.toString());
            String MachineTestId = jsonObject.getString("MachineTestId");
            PumpDetails pumpDetails = pumpDetailsLinkedHashMap.get(MachineTestId);
            JSONArray TestProcessDataList = jsonObject.getJSONArray("TestProcessDataList");
            for (Object processDate : TestProcessDataList) {
                JSONObject processDateJsonObject = JSONObject.parseObject(processDate.toString());
                String LocationID = processDateJsonObject.getString("LocationID");
                String amplitude = processDateJsonObject.getString("amplitude");
                if ("speed".equals(id_code.get(LocationID))) {
                    pumpDetails.setSpeed(amplitude);
                } else if ("temperature".equals(id_code.get(LocationID))) {
                    pumpDetails.setTemperature(amplitude);
                }
            }
        }
    }

    @Override
    public void setEquipmentHealth(String[] pumpArray, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap, HashMap<String, String> state_code) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.and("FacilityId").is(pumpArray[1]);
        Query query = new BasicQuery(queryBuilder.get());
        DBObject equipmentHealthDBObject = mongoTemplate.findOne(query, DBObject.class, "EquipmentHealth");
        //System.err.println(equipmentHealthDBObject.toString());
        JSONObject equipmentHealthJsonObject = JSON.parseObject(equipmentHealthDBObject.toString());
        for (PumpDetails pumpDetails : pumpDetailsLinkedHashMap.values()) {
            String comState = equipmentHealthJsonObject.getString("ComState");
            String runState = equipmentHealthJsonObject.getString("RunSate");
            String healthState = equipmentHealthJsonObject.getString("HealthState");
            pumpDetails.setComState(state_code.get(comState));
            pumpDetails.setRunSate(state_code.get(runState));
            pumpDetails.setHealthState(state_code.get(healthState));
        }
    }

    @Override
    public LinkedHashMap<String, PumpDiagnosis> getExpertHistory(String st, String et, Map.Entry<String, HashMap<String, String>> pump, HashMap<String, String> state_code) {
        LinkedHashMap<String, PumpDiagnosis> pumpDiagnosisLinkedHashMap = new LinkedHashMap<>();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.and("ROWSTAMP").greaterThanEquals(DateUtil.dateStringTodateTime(st,DateUtil.MINUTE_PATTERN)).
                lessThan(DateUtil.dateStringTodateTime(et,DateUtil.MINUTE_PATTERN)).and("MachineID").is(pump.getKey());
        BasicDBObject fieldDBObject = new BasicDBObject();
        Query query = new BasicQuery(queryBuilder.get(), fieldDBObject);
        List<DBObject> expertHistoryList = mongoTemplate.find(query, DBObject.class, "ExpertHistory");
        //System.err.println(expertHistoryList.toString());
        JSONArray jsonArray = JSONArray.parseArray(expertHistoryList.toString());
        for (Object object : jsonArray) {
            JSONObject expertHistoryObject = JSON.parseObject(object.toString());
            String ROWSTAMP =expertHistoryObject.getJSONObject("ROWSTAMP").getString("$date");
            String saveTime = DateUtil.utcStringToLocalString(ROWSTAMP, DateUtil.DATE_TIME_PATTERN);
            JSONArray ExpertDiagnosesJsonArray = expertHistoryObject.getJSONArray("ExpertDiagnoses");
            for (Object expertDiagnoses : ExpertDiagnosesJsonArray) {
                JSONObject expertDiagnosesJsonObject = JSON.parseObject(expertDiagnoses.toString());
                PumpDiagnosis pumpDiagnosis = new PumpDiagnosis();
                String Diagnosis = expertDiagnosesJsonObject.getString("Diagnosis");
                String Peaks = expertDiagnosesJsonObject.getString("Peaks").replace("=","等于").replace("\r\n","");
                String Severity = expertDiagnosesJsonObject.getString("Severity");
                pumpDiagnosis.setSaveTime(saveTime);
                pumpDiagnosis.setDiagnosis(Diagnosis);
                pumpDiagnosis.setPeaks(Peaks);
                pumpDiagnosis.setSeverity(Severity);
                String c_id ="";
                if (Diagnosis.contains("泵")) {
                     c_id = pump.getValue().get("SB_shock");
                } else if (Diagnosis.contains("电机")) {
                    c_id = pump.getValue().get("DJ_shock");
                }
                pumpDiagnosis.setC_id(c_id);
                pumpDiagnosisLinkedHashMap.put(c_id + ROWSTAMP, pumpDiagnosis);
            }
            JSONArray ExpertRecommendationsJsonArray = expertHistoryObject.getJSONArray("ExpertRecommendations");
            for (Object recommend:ExpertRecommendationsJsonArray){
                JSONObject recommendJsonObject = JSONObject.parseObject(recommend.toString());
                String Recommendation = recommendJsonObject.getString("Recommendation");
                String Description = state_code.get(recommendJsonObject.getString("Description"));
                String c_id = "";
                if (Recommendation.contains("电机")){
                     c_id=pump.getValue().get("DJ_shock");
                }else if (Recommendation.contains("泵")){
                    c_id = pump.getValue().get("SB_shock");
                }
                pumpDiagnosisLinkedHashMap.get(c_id+ROWSTAMP).setRecommend(Recommendation);
                pumpDiagnosisLinkedHashMap.get(c_id+ROWSTAMP).setDesc(Description);
            }
        }
        return pumpDiagnosisLinkedHashMap;
    }
}

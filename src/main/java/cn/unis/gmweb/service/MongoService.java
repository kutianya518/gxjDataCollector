package cn.unis.gmweb.service;

import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.pojo.PumpDiagnosis;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface MongoService {

    void setVibrationData(String[] pumpArray, String st, String et, HashMap<String, String> id_code, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap);

    void setProcessData(String[] pumpArray, String st, String et, HashMap<String, String> id_code, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap);

    void setEquipmentHealth(String[] pumpArray, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap,HashMap<String,String> state_code);

    LinkedHashMap<String, PumpDiagnosis> getExpertHistory(String st, String et, Map.Entry<String, HashMap<String, String>> pump, HashMap<String, String> state_code);
}

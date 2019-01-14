package cn.unis.gmweb.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.pojo.PumpWarn;
import cn.unis.gmweb.pojo.Tree;

public interface TreeService {
    List<Tree> findHtTree(String lineName);

    List<Tree> findqdlTree(String lineName);

    List<String> getAllQyId(String sb_type);

    List<String> getAllYcId(String qyid, String c_type);

    List<String> queryYcData(String tableName, String id, String st, String et);

    List<String> queryYmData(String tableName, String id, String st, String et);

    List<String> queryAlarmData(String st, String et);

    Boolean queryIa(String ia_id,String st);

    List<String> findAllPumpIa();

    HashMap<String, String> queryMapIdAndCode();

    HashMap<String, String> queryStateAndCode();

    void setBkc_flow_big(String st, String et, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap);

    HashMap<String, HashMap<String, String>> findAllPumpCid();

    List<String> findAllPumpQyid();

    List<String> getAllPumpQdlYcId(String qyid, String c_type);

    String findThresholdModel(String sbid);

    LinkedHashMap<String, String> findThresholdModelMap(String sbid);

    void insertMysql(List<PumpWarn> pumpWarnList);
}

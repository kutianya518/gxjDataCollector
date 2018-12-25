package cn.unis.gmweb.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import cn.unis.gmweb.dynamic.CustomerContextHolder;
import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.utils.DateUtil;
import cn.unis.gmweb.utils.ProperUtil;
import org.springframework.stereotype.Service;

import cn.unis.gmweb.mapper.TreeMapper;
import cn.unis.gmweb.pojo.Tree;

@Service
public class TreeServiceImpl implements TreeService {

    @Resource
    private TreeMapper treeMapper;

    @Override
    public List<Tree> findHtTree(String lineName) {
        return treeMapper.findHtTree(lineName);
    }

    @Override
    public List<Tree> findqdlTree(String lineName) {
        return treeMapper.findqdlTree(lineName);
    }

    @Override
    public List<String> getAllQyId(String sb_type) {
        return treeMapper.getAllQyId(sb_type);
    }

    @Override
    public List<String> getAllYcId(String qyid, String c_type) {
        return treeMapper.getAllYcId(qyid, c_type);
    }

    @Override
    public List<String> queryYcData(String tableName, String id, String st, String et) {
        return treeMapper.queryYcData(tableName, id, st, et);
    }

    @Override
    public List<String> queryYmData(String tableName, String id, String st, String et) {
        return treeMapper.queryYmData(tableName, id, st, et);
    }

    @Override
    public List<String> queryAlarmData(String st, String et) {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.sems8000);
        return treeMapper.queryAlarmData(st, et);
    }

    @Override
    public Boolean queryIa(String ia_id) {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.sems8000);
        String year = DateUtil.dateTimeTodateString(null, DateUtil.YEAR_PATTERN);
        String tableName = String.format("hisanalog_%s_0%s", year, Integer.valueOf(ia_id) % 10);
        Double iaValue = treeMapper.queryIaValue(ia_id, tableName, ProperUtil.getPro("st"));
        return iaValue <= 1 ? true : false;
    }

    @Override
    public List<String> findAllPumpIa() {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.bigdata);
        return treeMapper.findAllPumpIa();
    }

    @Override
    public HashMap<String, String> queryMapIdAndCode() {
        List<String> id_code = treeMapper.queryMapIdAndCode();
        HashMap<String, String> map = new HashMap<>();
        for (String str : id_code) {
            String[] array = str.split(",");
            map.put(array[0], array[1]);
        }
        return map;
    }

    @Override
    public HashMap<String, String> queryStateAndCode() {
        List<String> state_codeList = treeMapper.queryStateAndCode();
        HashMap stateAndCodeMap = new HashMap<>();
        for (String str : state_codeList) {
            String[] array = str.split(",");
            stateAndCodeMap.put(array[0], array[1]);
        }
        return stateAndCodeMap;
    }

    @Override
    public void setBkc_flow_big(String st, String et, LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap) {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.bigdata);
        List<String> flowDataList = treeMapper.findFlowData(st, et);
       if (flowDataList.size()==0) return;
        int index = 0;
        for (PumpDetails pumpDetails : pumpDetailsLinkedHashMap.values()) {
            String[] flowData = flowDataList.get(index).split(",");
            pumpDetails.setIn_flow(flowData[0]);
            pumpDetails.setIn_speed(flowData[1]);
            pumpDetails.setIn_sum_flow(flowData[2]);
            pumpDetails.setLevel(flowData[3]);
            pumpDetails.setOut_pressure(flowData[4]);
            pumpDetails.setOut_flow(flowData[5]);
            pumpDetails.setOut_speed(flowData[6]);
            pumpDetails.setOut_sum_flow(flowData[7]);
            if (index < flowDataList.size() - 1) {
                //如果水泵一分钟内多于flowDataList.size()信息，则水泵流量数据赋值为一分钟内最后一条
                index++;
            } else {
                //.....
            }
        }


    }

    @Override
    public HashMap<String, HashMap<String, String>> findAllPumpCid() {
        CustomerContextHolder.setCustomerType(CustomerContextHolder.bigdata);
        List<String> pumpCidList = treeMapper.findAllPumpCid();
        HashMap<String, HashMap<String, String>> pumpCidMap = new HashMap<>();
        for (String pumpCid : pumpCidList) {
            String[] pump = pumpCid.split(",");
            String machineId = pump[0];
            String c_id = pump[1];
            String c_code = pump[2];
            if (pumpCidMap.containsKey(machineId)) {
                pumpCidMap.get(machineId).put(c_code, c_id);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put(c_code, c_id);
                pumpCidMap.put(machineId, map);
            }
        }
        return pumpCidMap;
    }


}

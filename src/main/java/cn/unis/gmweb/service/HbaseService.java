package cn.unis.gmweb.service;

import cn.unis.gmweb.pojo.PumpDetails;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;



public interface HbaseService {
    void insertIntoHbase(String hbaseTableName, ConcurrentHashMap<String, List<String>> tmpData);

    void insertIntoBkcHbase(String hbaseTableName, List<String> pumpData);
}

package cn.unis.gmweb.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import cn.unis.gmweb.pojo.Tree;

public interface TreeMapper {

    List<Tree> findHtTree(@Param("lineName") String lineName);

    @Select("SELECT q_id ,q_name FROM xj_qy_big WHERE z_id=(SELECT z_id FROM xj_zl_big WHERE z_name=#{lineName}) AND q_name LIKE '%全'")
    List<Tree> findqdlTree(String lineName);

    @Select("select q_id from xj_qy_big where q_name like '%${sb_type}'")
    List<String> getAllQyId(@Param("sb_type") String sb_type);

    @Select("select CONCAT_WS(',',c_id,c_code) from xj_cd_big where q_id= #{qyid} and c_type=#{c_type} order by c_id")
    List<String> getAllYcId(@Param("qyid") String qyid, @Param("c_type") String c_type);

    @Select("SELECT CONCAT_WS(',',savetime,curdatatime,calvalue) FROM ${tableName} WHERE analogid=#{id} and savetime>=#{st} and savetime<#{et}")
    List<String> queryYcData(@Param("tableName") String tableName, @Param("id") String id, @Param("st") String st, @Param("et") String et);

    @Select("SELECT CONCAT_WS(',',savetime,datetime,curvalue) FROM ${tableName} WHERE accumulatorid=#{id} and savetime>=#{st} and savetime<#{et}")
    List<String> queryYmData(@Param("tableName") String tableName, @Param("id") String id, @Param("st") String st, @Param("et") String et);

    @Select("SELECT CONCAT_WS(',',sid ,savetime ,Descriptions ,Positions)  FROM (SELECT objid,objtype,thetime AS SaveTime ,description AS Descriptions FROM hisalarm WHERE thetime>#{st} AND thetime<=#{et} AND objtype IN('soe','analog')) his INNER JOIN (SELECT s_id AS Sid,S_position AS Positions FROM xj_sb_big) sb ON his.objid=sb.Sid")
    List<String> queryAlarmData(@Param("st") String st, @Param("et") String et);

    @Select("SELECT * FROM xj_cd_big WHERE q_id=#{qid} AND c_code=#{Ia}")
    String queryIa(@Param("qid") String qid, @Param("Ia") String Ia);

    @Select("SELECT calvalue FROM ${tableName} WHERE ANALOGID=#{ia_cdId} and SaveTime > #{st} limit 1")
    Double queryIaValue(@Param("ia_cdId") String ia_cdId, @Param("tableName") String tableName, @Param("st") String st);

    @Select("SELECT CONCAT_WS(',',Db_MapId,MachineID,sid) FROM (SELECT s_id AS sid,MachineID FROM bkc_sb_big) sb INNER JOIN (SELECT Db_MapId ,C_code,s_id FROM bkc_cd_big) cd ON sb.sid=cd.s_id AND c_code ='Ia'")
    List<String> findAllPumpIa();
    @Select("SELECT CONCAT_WS(',',db_mapid ,C_code) FROM bkc_cd_big")
    List<String> queryMapIdAndCode();
    @Select("SELECT CONCAT_WS(',',State ,CODE) FROM bkc_state_big")
    List<String> queryStateAndCode();
    @Select("SELECT CONCAT_WS(',',In_flow,in_speed,in_sum_flow,LEVEL,out_pressure,out_flow,out_speed,out_sum_flow) FROM bkc_flow_big where SaveTime >= #{st} and SaveTime < #{et}")
    List<String> findFlowData(@Param("st") String st,@Param("et") String et);
    @Select("SELECT CONCAT_WS(',',MachineID,c_id,c_code) FROM (SELECT s_id AS sid,MachineID FROM bkc_sb_big) sb INNER JOIN (SELECT c_id,C_code,s_id FROM bkc_cd_big) cd ON sb.sid=cd.s_id ")
    List<String> findAllPumpCid();
}

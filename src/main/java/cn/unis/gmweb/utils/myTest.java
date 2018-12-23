package cn.unis.gmweb.utils;

import cn.unis.gmweb.pojo.PumpDetails;
import cn.unis.gmweb.service.HttpClientService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class myTest {
    @Test
    public void testPumpJSON(){
        PumpDetails pumpDetails = new PumpDetails();
        pumpDetails.setDateTime("2018-11-21");
        pumpDetails.setTemperature("1000");
        pumpDetails.setOut_sum_flow("333");
        pumpDetails.setIn_speed("299");
        System.err.println(pumpDetails.toString());
        String[] pump = pumpDetails.toString().split(",");
        for (String str:pump){
            String[] tmp = str.split("=");
            System.err.println(tmp[0]);
            System.err.println(tmp[1]);
        }
    }
@Test
public void testJQR() throws Exception {
    String dboList ="[{ \"_id\" : { \"$oid\" : \"5c122d28c4f473094408d404\"} , \"MachineTestId\" : \"4c3811c85a9d4e899ca62dff7da6578b\" , \"MachineID\" : \"2ea7b8b6487f4c7ea39a1e9a3d4e8cf8\" , \"StartDatetime\" : { \"$date\" : \"2018-12-13T09:57:58.000Z\"} , \"StopDatetime\" : { \"$date\" : \"2018-12-13T09:57:58.000Z\"} , \"InsertDate\" : { \"$date\" : \"2018-12-13T09:58:00.024Z\"} , \"siteId\" : \"c0dffe6a462544fa802c3ef5a275a24b\" , \"TestProcessDataList\" : [ { \"LocationID\" : \"5afe1101cffc49d7871e693127bbe579\" , \"amplitude\" : 1350.0 , \"unit\" : \"rpm\" , \"warning\" : 0} , { \"LocationID\" : \"09b4f30f3a274fff8fa48b93d3be1e1e\" , \"amplitude\" : 25.14152717590332 , \"unit\" : \"℃\" , \"warning\" : 0}]}, { \"_id\" : { \"$oid\" : \"5c122d55c4f473094408d40a\"} , \"MachineTestId\" : \"d137ff9b1ea04fb4adf198b681c685d1\" , \"MachineID\" : \"2ea7b8b6487f4c7ea39a1e9a3d4e8cf8\" , \"StartDatetime\" : { \"$date\" : \"2018-12-13T09:58:43.000Z\"} , \"StopDatetime\" : { \"$date\" : \"2018-12-13T09:58:43.000Z\"} , \"InsertDate\" : { \"$date\" : \"2018-12-13T09:58:45.007Z\"} , \"siteId\" : \"c0dffe6a462544fa802c3ef5a275a24b\" , \"TestProcessDataList\" : [ { \"LocationID\" : \"5afe1101cffc49d7871e693127bbe579\" , \"amplitude\" : 1265.625 , \"unit\" : \"rpm\" , \"warning\" : 0} , { \"LocationID\" : \"09b4f30f3a274fff8fa48b93d3be1e1e\" , \"amplitude\" : 25.17204475402832 , \"unit\" : \"℃\" , \"warning\" : 0}]}]";
    JSONArray jsonArray=JSONArray.parseArray(dboList.toString());
    for (Object object:jsonArray){
        JSONObject jsonObject = JSON.parseObject(object.toString());
        String tm =JSON.parseObject(jsonObject.get("InsertDate").toString()).get("$date").toString();
        System.err.println(tm);
        System.err.println(DateUtil.utcStringToLocalString(tm,DateUtil.DATE_TIME_PATTERN));
        String TestProcessDataList =jsonObject.get("TestProcessDataList").toString();
        JSONArray result=JSONArray.parseArray(TestProcessDataList);
        String amp=  JSONObject.parseObject(result.get(0).toString()).get("amplitude").toString();
        System.err.println(amp);
        String amp2=  JSONObject.parseObject(result.get(1).toString()).get("amplitude").toString();
        System.err.println(amp2);
    }
}
    @Test
    public void testSwitch2() {

        String aa = "xj_ht_data";
        String family = "";

        System.err.println(family);
    }

    @Test
    public void testDate() {
        String time = "1544282970419";
        

        Date dt = new Date(Long.valueOf(time));
        System.err.println(DateUtil.dateTimeTodateString(dt, DateUtil.DATE_TIME_PATTERN));
    }

    @Test
    public void testifelse() {
        int a = 1;
        if (a == 1) {
            System.out.println(a);
        } else if (a != 1) {
            System.out.println(a + "1");
        }

    }

    @Test
    public void testSwitch() {
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 1:
                    System.out.println(1);
                    break;
                default:
                    System.out.println(i + "##");
                    break;

            }
        }
    }

    @Test
    public void testContinue() {
        for (int i = 0; i < 5; i++) {
            if (i == 1 || i == 3) continue;
            System.out.println(i);
        }

    }


    public static void main(String[] args) throws ParseException {
        for (ConfigTable ta : ConfigTable.values()) {
            System.out.println(ta.name());
            System.out.println(ta.ordinal());
            System.out.println(ta.valueOf(ta.name()));
            System.out.println(ta.toString());
        }
        ConfigTable.valueOf("jxinterfacedata");

    }
}

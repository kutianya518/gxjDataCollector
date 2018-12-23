package cn.unis.gmweb.task;

import cn.unis.gmweb.service.HttpClientService;
import cn.unis.gmweb.utils.XMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * 利用spring调度，定时写在配置文件中
 *机器人数据采集
 * @author lgf
 */
@Component
@RequestMapping("/jqr")
@CrossOrigin(origins = "*", maxAge = 3600)
public class JqrDataCollectorTask {

    @Autowired
    private HttpClientService httpClientService;
    @RequestMapping("/jqr")
    public void JqrCollector() {
        String url ="http://211.155.165.1:12357/dalirobot/QueryInspection";
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><MESSAGE Verison=\"1.0\"><HEADER MsgType=\"QueryInspectionResultReq\" MsgSeq=\"\" /><ParamList><InspectionLogid>1598</InspectionLogid><DevStr>14,15,16,17,18,19,20</DevStr><BeginTime>2018-12-12 08:00:00</BeginTime><EndTime>2018-12-13 10:59:00</EndTime><StartNum>0</StartNum><EndNum>100</EndNum></ParamList></MESSAGE>";
        String result = httpClientService.doPost(url,xml);
        System.err.println(result);

        String xml2 ="<?xml version=\"1.0\" encoding=\"UTF-8\" ?><MESSAGE Verison=\"1.0\"><HEADER MsgType=\"QueryDiffResultReq\" MsgSeq=\"\" /><ParamList><BeginTime>2018-12-12 08:00:00</BeginTime><EndTime>2018-12-13 10:59:00</EndTime><DevStr>1,2,3,4</DevStr></ParamList></MESSAGE>";
        String result2 = httpClientService.doPost(url,xml2);
        System.err.println(result2);
        XMLUtils.getResultMap(result);
        XMLUtils.getResultMap(result2);
        httpClientService.download("","");
    }
}

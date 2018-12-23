package cn.unis.gmweb.task;

import cn.unis.gmweb.utils.DateUtil;
import cn.unis.gmweb.utils.ProperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 利用spring调度，定时写在配置文件中
 *更新采集时间
 * @author lgf
 */
@Component
public class UpdateCollectorTimeTask {
    private Logger logger = LoggerFactory.getLogger(UpdateCollectorTimeTask.class);
    @Async
    public void updateCollectorTime() {
        String startTime = ProperUtil.getPro("et");
        long nextMinute = System.currentTimeMillis()+60000;
        Date date = new Date(nextMinute);
        String stopTime = DateUtil.dateTimeTodateString(date, DateUtil.MINUTE_PATTERN);
	    ProperUtil.updatePro("st",startTime);
	    ProperUtil.updatePro("et",stopTime);
        logger.error(ProperUtil.getPro("st"));
        logger.error(ProperUtil.getPro("et"));
    }
}

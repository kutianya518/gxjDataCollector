package cn.unis.gmweb.pojo;

import java.util.Date;

public class HtWarn {
    private String qyid;//区域id
    private Date saveTime;//采集时间
    private String warnLevel;//预警级别
    private String warnArguments;//预警参数
    private String threshold;//阈值条件

    public String getQyid() {
        return qyid;
    }

    public void setQyid(String qyid) {
        this.qyid = qyid;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public String getWarnArguments() {
        return warnArguments;
    }

    public void setWarnArguments(String warnArguments) {
        this.warnArguments = warnArguments;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "HtWarn{" +
                "qyid='" + qyid + '\'' +
                ", saveTime=" + saveTime +
                ", warnLevel='" + warnLevel + '\'' +
                ", warnArguments='" + warnArguments + '\'' +
                ", threshold='" + threshold + '\'' +
                '}';
    }
}

package cn.unis.gmweb.pojo;

import java.util.Date;

public class PumpWarn {
    private String machineId;//设备id
    private Date dataTime;//数据时间
    private String warnLevel;//预警级别
    private String warnArguments;//预警参数
    private String threshold;//阈值条件
    private String data;//数据

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

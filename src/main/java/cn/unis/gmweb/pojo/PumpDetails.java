package cn.unis.gmweb.pojo;

public class PumpDetails {
    private String MachineID;
    private String DataTime;
    private String DJ_shock;
    private String SB_shock;
    private String speed;
    private String temperature;
    private String ComState;
    private String HealthState;
    private String RunSate;
    private String In_flow;
    private String In_speed;
    private String In_sum_flow;
    private String level;
    private String out_pressure;
    private String out_flow;
    private String out_speed;
    private String out_sum_flow;
    private String ua;
    private String ub;
    private String uc;
    private String ia;
    private String ib;
    private String ic;
    private String pa;
    private String pb;
    private String pc;
    private String p;
    private String qa;
    private String qb;
    private String qc;
    private String q;
    private String acos;
    private String bcos;
    private String ccos;
    private String cos;
    private String f;
    private String warnLevel="normal";
    private String warnArguments;
    private String threshold;

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
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

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getUb() {
        return ub;
    }

    public void setUb(String ub) {
        this.ub = ub;
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public String getIa() {
        return ia;
    }

    public void setIa(String ia) {
        this.ia = ia;
    }

    public String getIb() {
        return ib;
    }

    public void setIb(String ib) {
        this.ib = ib;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getQa() {
        return qa;
    }

    public void setQa(String qa) {
        this.qa = qa;
    }

    public String getQb() {
        return qb;
    }

    public void setQb(String qb) {
        this.qb = qb;
    }

    public String getQc() {
        return qc;
    }

    public void setQc(String qc) {
        this.qc = qc;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getAcos() {
        return acos;
    }

    public void setAcos(String acos) {
        this.acos = acos;
    }

    public String getBcos() {
        return bcos;
    }

    public void setBcos(String bcos) {
        this.bcos = bcos;
    }

    public String getCcos() {
        return ccos;
    }

    public void setCcos(String ccos) {
        this.ccos = ccos;
    }

    public String getCos() {
        return cos;
    }

    public void setCos(String cos) {
        this.cos = cos;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public PumpDetails() {
    }

    public String getMachineID() {
        return MachineID;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public String getDataTime() {
        return DataTime;
    }

    public void setDataTime(String dateTime) {
        DataTime = dateTime;
    }

    public String getDJ_shock() {
        return DJ_shock;
    }

    public void setDJ_shock(String DJ_shock) {
        this.DJ_shock = DJ_shock;
    }

    public String getSB_shock() {
        return SB_shock;
    }

    public void setSB_shock(String SB_shock) {
        this.SB_shock = SB_shock;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getComState() {
        return ComState;
    }

    public void setComState(String comState) {
        ComState = comState;
    }

    public String getHealthState() {
        return HealthState;
    }

    public void setHealthState(String healthState) {
        HealthState = healthState;
    }

    public String getRunSate() {
        return RunSate;
    }

    public void setRunSate(String runSate) {
        RunSate = runSate;
    }


    public String getIn_flow() {
        return In_flow;
    }

    public void setIn_flow(String in_flow) {
        In_flow = in_flow;
    }

    public String getIn_speed() {
        return In_speed;
    }

    public void setIn_speed(String in_speed) {
        In_speed = in_speed;
    }

    public String getIn_sum_flow() {
        return In_sum_flow;
    }

    public void setIn_sum_flow(String in_sum_flow) {
        In_sum_flow = in_sum_flow;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOut_pressure() {
        return out_pressure;
    }

    public void setOut_pressure(String out_pressure) {
        this.out_pressure = out_pressure;
    }

    public String getOut_flow() {
        return out_flow;
    }

    public void setOut_flow(String out_flow) {
        this.out_flow = out_flow;
    }

    public String getOut_speed() {
        return out_speed;
    }

    public void setOut_speed(String out_speed) {
        this.out_speed = out_speed;
    }

    public String getOut_sum_flow() {
        return out_sum_flow;
    }

    public void setOut_sum_flow(String out_sum_flow) {
        this.out_sum_flow = out_sum_flow;
    }

    public String toString1() {
        return "PumpDetails{" +
                "MachineID='" + MachineID + '\'' +
                ", DataTime='" + DataTime + '\'' +
                ", DJ_shock='" + DJ_shock + '\'' +
                ", SB_shock='" + SB_shock + '\'' +
                ", speed='" + speed + '\'' +
                ", temperature='" + temperature + '\'' +
                ", ComState='" + ComState + '\'' +
                ", HealthState='" + HealthState + '\'' +
                ", RunSate='" + RunSate + '\'' +
                ", In_flow='" + In_flow + '\'' +
                ", In_speed='" + In_speed + '\'' +
                ", In_sum_flow='" + In_sum_flow + '\'' +
                ", level='" + level + '\'' +
                ", out_pressure='" + out_pressure + '\'' +
                ", out_flow='" + out_flow + '\'' +
                ", out_speed='" + out_speed + '\'' +
                ", out_sum_flow='" + out_sum_flow + '\'' +
                ", ua='" + ua + '\'' +
                ", ub='" + ub + '\'' +
                ", uc='" + uc + '\'' +
                ", ia='" + ia + '\'' +
                ", ib='" + ib + '\'' +
                ", ic='" + ic + '\'' +
                ", pa='" + pa + '\'' +
                ", pb='" + pb + '\'' +
                ", pc='" + pc + '\'' +
                ", p='" + p + '\'' +
                ", qa='" + qa + '\'' +
                ", qb='" + qb + '\'' +
                ", qc='" + qc + '\'' +
                ", q='" + q + '\'' +
                ", acos='" + acos + '\'' +
                ", bcos='" + bcos + '\'' +
                ", ccos='" + ccos + '\'' +
                ", cos='" + cos + '\'' +
                ", f='" + f + '\'' +
                ", warnLevel='" + warnLevel + '\'' +
                ", warnArguments='" + warnArguments + '\'' +
                ", threshold='" + threshold + '\'' +
                '}';
    }

    /**
     * 入库数据
     * @return
     */
    @Override
    public String toString() {
        return "MachineID=" + MachineID +
                ",DataTime=" + DataTime +
                ",DJ_shock=" + DJ_shock +
                ",SB_shock=" + SB_shock +
                ",speed=" + speed +
                ",temperature=" + temperature +
                ",ComState=" + ComState +
                ",HealthState=" + HealthState +
                ",RunSate=" + RunSate +
                ",In_flow=" + In_flow +
                ",In_speed=" + In_speed +
                ",In_sum_flow=" + In_sum_flow +
                ",level=" + level +
                ",out_pressure=" + out_pressure +
                ",out_flow=" + out_flow +
                ",out_speed=" + out_speed +
                ",out_sum_flow=" + out_sum_flow +
                ",ua=" + ua +
                ",ub=" + ub +
                ",uc=" + uc +
                ",ia=" + ia +
                ",ib=" + ib +
                ",ic=" + ic +
                ",pa=" + pa +
                ",pb=" + pb +
                ",pc=" + pc +
                ",p=" + p +
                ",qa=" + qa +
                ",qb=" + qb +
                ",qc=" + qc +
                ",q=" + q +
                ",acos=" + acos +
                ",bcos=" + bcos +
                ",ccos=" + ccos +
                ",cos=" + cos +
                ",f=" + f +
                ",warnLevel=" + warnLevel +
                ",warnArguments=" + warnArguments;
    }
}

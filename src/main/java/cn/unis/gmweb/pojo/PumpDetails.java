package cn.unis.gmweb.pojo;

public class PumpDetails {
    private String MachineID="111";
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
                ",out_sum_flow=" + out_sum_flow;
    }
}

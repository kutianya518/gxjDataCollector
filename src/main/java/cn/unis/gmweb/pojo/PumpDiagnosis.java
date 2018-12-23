package cn.unis.gmweb.pojo;

public class PumpDiagnosis {
    private String C_id;
    private String SaveTime;
    private String Severity;
    private String Diagnosis;
    private String Peaks;
    private String Desc;
    private String Recommend;

    public PumpDiagnosis() {
    }

    public String getC_id() {
        return C_id;
    }

    public void setC_id(String c_id) {
        C_id = c_id;
    }

    public String getSaveTime() {
        return SaveTime;
    }

    public void setSaveTime(String saveTime) {
        SaveTime = saveTime;
    }

    public String getSeverity() {
        return Severity;
    }

    public void setSeverity(String severity) {
        Severity = severity;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getPeaks() {
        return Peaks;
    }

    public void setPeaks(String peaks) {
        Peaks = peaks;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getRecommend() {
        return Recommend;
    }

    public void setRecommend(String recommend) {
        Recommend = recommend;
    }

    @Override
    public String toString() {
        return "C_id=" + C_id +
                ",SaveTime=" + SaveTime +
                ",Severity=" + Severity +
                ",Diagnosis=" + Diagnosis +
                ",Peaks=" + Peaks +
                ",Desc=" + Desc +
                ",Recommend=" + Recommend ;
    }
}

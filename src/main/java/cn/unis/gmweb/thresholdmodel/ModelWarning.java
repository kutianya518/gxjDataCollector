package cn.unis.gmweb.thresholdmodel;

import cn.unis.gmweb.pojo.PumpDetails;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ModelWarning {

    public static void htThresholdCalculation(ConcurrentHashMap<String, List<String>> tmpData, LinkedHashMap<String, String> lineHtModel) {

        for (Map.Entry<String, List<String>> ht : tmpData.entrySet()) {
            //qyid,timestamp
            String ht_key = ht.getKey();
            //ivalue,I   tvalue,T timevaluestr,DateTime   red,warnLevel I,T,warnArguments threshold
            List<String> ht_value = ht.getValue();
            Set<String> newHt_value=new HashSet<>();
            //预警条件阈值red:orange:yellow:blue
            String warnLevel = "normal";//初始化为normal
            String warnArguments = null;
            String threshold = JSON.toJSON(lineHtModel).toString();
            for (Map.Entry<String, String> warnCondition : lineHtModel.entrySet()) {
                if (!warnLevel.equals("normal")) break;
                String warnColour = warnCondition.getKey();//预警等级
                String warnThreshold = warnCondition.getValue();//预警条件
                String i_threshold = warnThreshold.split("[|]")[0];//电流条件
                String t_threshold = warnThreshold.split("[|]")[1];//温度条件
                for (String ele : ht_value) {
                    String value = ele.split(",")[0];
                    String value_code = ele.split(",")[1];
                    if ("I".equals(value_code) && isInMinAndMax(value, i_threshold)) {
                        warnLevel = warnColour;
                        warnArguments = warnArguments == null ? "I" : warnArguments + "," + "I";
                    } else if ("T".equals(value_code) && isInMinAndMax(value, t_threshold)) {
                        warnLevel = warnColour;
                        warnArguments = warnArguments == null ? "T" : warnArguments + "," + "T";
                    }
                    newHt_value.add(value+"="+value_code);
                }
            }
            if (!"normal".equals(warnLevel)){
                newHt_value.add(warnArguments + "=" + "warnArguments");
                newHt_value.add(threshold + "=" + "threshold");
            }
            newHt_value.add(warnLevel + "=" + "warnLevel");
            //计算后的数据重新放回tmpData中
            tmpData.put(ht_key, new ArrayList<>(newHt_value));

        }
    }




    public static void pumpThresholdCalculation(LinkedHashMap<String, PumpDetails> pumpDetailsLinkedHashMap, LinkedHashMap<String, String> pumpModelMap) {
        for (PumpDetails pump : pumpDetailsLinkedHashMap.values()) {
            //预警条件阈值red:orange:yellow:blue
            LinkedHashMap<String, LinkedHashMap<String, String>> pumpWarnLevel = switchMap(pumpModelMap);
            for (Map.Entry<String, LinkedHashMap<String, String>> entry : pumpWarnLevel.entrySet()) {
                if (!"normal".equals(pump.getWarnLevel())) break;
                //预警级别
                String warnLevel = entry.getKey();
                //阈值判定
                for (Map.Entry<String, String> warnArg : entry.getValue().entrySet()) {
                    //属性
                    String field = warnArg.getKey();
                    //阈值区间
                    String threshold = warnArg.getValue();
                    try {
                        Field pumpField = pump.getClass().getDeclaredField(field);
                        pumpField.setAccessible(true);
                        //属性值
                       Object fieldObject= pumpField.get(pump);
                       if (fieldObject!=null){//防止没有采集到数据比如压力
                           String fieldArg = fieldObject.toString();
                           if (isInMinAndMax(fieldArg, threshold)) {
                               pump.setWarnLevel(warnLevel);
                               pump.setWarnArguments(pump.getWarnArguments() == null ? field : pump.getWarnArguments() + "," + field);
                               pump.setThreshold(JSON.toJSON(pumpModelMap).toString());
                           }
                       }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static Boolean isInMinAndMax2(String argument, String threshold) {
        String Min = threshold.split(",")[0];
        String Max = threshold.split(",")[1];
        Double newMin = "-1".equals(Min) ? Double.NEGATIVE_INFINITY : Double.valueOf(Min);
        Double newMax = "-1".equals(Max) ? Double.POSITIVE_INFINITY : Double.valueOf(Max);
        Double newArg = Double.valueOf(argument);
        if (newArg > newMin && newArg <= newMax) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 多阈值之间是或的关系
     * @param argument 值
     * @param threshold 阈值区间
     * @return true or false
     */
    public static Boolean isInMinAndMax(String argument, String threshold) {
        Double newArg = Double.valueOf(argument);
        String[] thresholdArray = threshold.split(",");
        Boolean isin = false;
        for (int i = 0; i < thresholdArray.length ; i=i+2) {
            Double newMin = "-1".equals(thresholdArray[i]) ? Double.NEGATIVE_INFINITY : Double.valueOf(thresholdArray[i]);
            Double newMax = "-1".equals(thresholdArray[i + 1]) ? Double.POSITIVE_INFINITY : Double.valueOf(thresholdArray[i + 1]);
            if (newArg > newMin && newArg <= newMax) {
                isin=isin||true;
            } else {
                isin=isin||false;
            }
        }
        return isin;

    }

    public static LinkedHashMap<String, LinkedHashMap<String, String>> switchMap(LinkedHashMap<String, String> pumpModelMap) {
        //预警条件阈值red:orange:yellow:blue
        LinkedHashMap<String, LinkedHashMap<String, String>> pumpMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> pumpModel : pumpModelMap.entrySet()) {

            LinkedHashMap<String, String> thresholdMap = new LinkedHashMap<>();
            /*
            参数定义顺序为：振动|温度|电压|电流|压力
            参数格式为：
            振动min,振动max|温度min,温度max|电压min,电压max|电流min,电流max|压力min,压力max
            参数值含义：-1出现min位置上表示负无穷大，-1出现max位置上表示正无穷大
             */

            String[] threshold = pumpModel.getValue().split("[|]");
            thresholdMap.put("SB_shock", threshold[0]);
            thresholdMap.put("DJ_shock", threshold[0]);
            thresholdMap.put("temperature", threshold[1]);
            thresholdMap.put("ua", threshold[2]);
            thresholdMap.put("ub", threshold[2]);
            thresholdMap.put("uc", threshold[2]);
            thresholdMap.put("ia", threshold[3]);
            thresholdMap.put("ib", threshold[3]);
            thresholdMap.put("ic", threshold[3]);
            thresholdMap.put("out_pressure", threshold[4]);
            pumpMap.put(pumpModel.getKey(), thresholdMap);
        }
        return pumpMap;
    }


}

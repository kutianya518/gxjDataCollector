package cn.unis.gmweb.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 读取修改配置文件KEY VALUE
 */

public class ProperUtil {
    private static Properties prop;
    private static String path = ProperUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private static final String fileName = "collectorTime.properties";

    /**
     * 加载配置文件
     */
    public static void load() {
        String filePath = path + fileName;
        prop = new Properties();// 属性集合对象
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            prop.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新配置文件key Value
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public static Boolean updatePro(String key, String value) {
        if (prop == null) {
            load();
        }
        prop.setProperty(key, value);
        // 文件输出流
        try {
            String filePath = path + fileName;
            FileOutputStream fos = new FileOutputStream(filePath);
            // 将Properties集合保存到流中
            prop.store(fos, "更新");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取配置文件数据
     *
     * @param key 键
     * @return 值
     */
    public static String getPro(String key) {
        if (prop == null) {
            load();
        }
        FileInputStream fis;
        try {
            String filePath = path + fileName;
            fis = new FileInputStream(filePath);
            prop.load(fis);// 将属性文件流装载到Properties对象中
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop.getProperty(key).trim();
    }

}



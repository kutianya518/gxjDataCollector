package cn.unis.gmweb.dynamic;

public class CustomerContextHolder {
    public static final String sems8000="dataSource_sems8000";
    public static final String bigdata="dataSource_bigdata";
    public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCustomerType(String customerType) {
        contextHolder.set(customerType);
    }

    public static String getCustomerType() {
        return contextHolder.get();
    }

    public static void clearCustomerType() {
        contextHolder.remove();
    }


}

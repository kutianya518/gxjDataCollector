package cn.unis.gmweb.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
       // System.err.println(CustomerContextHolder.getCustomerType());
        return CustomerContextHolder.getCustomerType();
    }
}

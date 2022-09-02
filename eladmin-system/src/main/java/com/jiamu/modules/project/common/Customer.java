package com.jiamu.modules.project.common;

import java.text.DecimalFormat;

/**
 * @ProjectName: yshopmall_hd
 * @Package: com.jiamu.common
 * @ClassName: Customer
 * @Author: fengwen
 * @Description: 自动生成客户号自增序列
 * @Date: 2022/7/25 15:38
 * @Version: 1.0.0
 */
public class Customer {

    private static int totalCount = 0;
    private final int customerID;
    public Customer(){
        ++totalCount;
        customerID = totalCount;
        System.out.println("增加一个");
    }
    public String getCustomerID() {
        DecimalFormat decimalFormat = new DecimalFormat("000");
        return decimalFormat.format(customerID);
    }

    public static void main(String[] args){
        Customer c1 = new Customer();
        System.out.println(c1.getCustomerID());
        Customer c2 = new Customer();
        System.out.println(c2.getCustomerID());
    }
}

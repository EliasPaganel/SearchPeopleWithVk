package com.company.repository;

import com.company.Pojos.UserInfo;
import SearchPeopleThroughVk.Tables;
import SearchPeopleThroughVk.tables.daos.CustomerDao;
import SearchPeopleThroughVk.tables.pojos.Customer;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthRepository {

//    @Autowired
//    CustomerDao customerDao;

//    @Autowired
//    DSLContext dslContext;

//    public void insertCustomerInfo(UserInfo userInfo){
//        Customer customer = new Customer();
//        customer.setUserId(Long.valueOf(userInfo.getUserId()));
//        customer.setAccessKey(userInfo.getToken());
//        customerDao.insert(customer);
//    }
//
//    public void fillAccessToken(UserInfo userInfo){
//        List<Customer> customers = customerDao.fetchByUserId(Long.valueOf(userInfo.getUserId()));
//        if (customers == null || customers.isEmpty()) {
//            insertCustomerInfo(userInfo);
//        } else {
//            if(customers.size() == 1){
//                Customer customer = customers.get(0);
//                customer.setAccessKey(userInfo.getToken());
//                customerDao.update(customer);
//            } else {
//                customerDao.delete(customers);
//            }
//        }
//    }
//
//    public Customer fetchByCustomerByUserId(Integer userId){
//        return dslContext.selectFrom(Tables.CUSTOMER)
//                .where(Tables.CUSTOMER.USER_ID.eq(Long.valueOf(userId)))
//                .fetchOneInto(Customer.class);
//    }


}

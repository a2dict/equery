package com.a2dict.equery;

import com.a2dict.equery.domain.Customer;
import io.ebean.DB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by a2dict on 2019/11/9
 */
class EQueryTest {
    @BeforeAll
    static void setup() {
        Date now = new Date();
        long ts = System.currentTimeMillis();
        DB.save(new Customer().setName("Carl").setCity("shenzhen").setPhone("010").setCreatedAt(new Date((long) (Math.random() * ts))));
        DB.save(new Customer().setName("Gary").setCity("ShenZhen").setPhone("011").setCreatedAt(new Date((long) (Math.random() * ts))));
        DB.save(new Customer().setName("Bob").setCity("guangzhou").setPhone("012").setCreatedAt(new Date((long) (Math.random() * ts))));
        DB.save(new Customer().setName("Job").setCity("guangzhou").setPhone("013").setCreatedAt(new Date((long) (Math.random() * ts))));
        DB.save(new Customer().setName("Gate").setCity("guangzhou").setPhone("014").setCreatedAt(new Date((long) (Math.random() * ts))));
    }

    @Test
    void testEqQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("phone", "012");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 1);
    }

    @Test
    void testInQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("name::in", "[\"Bob\",\"Job\"]");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 2);
    }

    @Test
    void testNotInQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("name::not_in", "[\"Bob\",\"Job\"]");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 3);
    }

    @Test
    void testLikeQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("city::like", "shen%");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 1);
    }

    @Test
    void testILikeQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("city::ilike", "shen%");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 2);
    }

    @Test
    void testGtQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("phone::gt", "012");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 2);
    }

    @Test
    void testGeQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("phone::ge", "012");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 3);
    }

    @Test
    void testLtQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("phone::lt", "012");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 2);
    }

    @Test
    void testLeQuery() {
        HashMap<String, String> qMap = new HashMap<>();
        qMap.put("phone::le", "012");
        QReq qReq = new QReq()
                .setQ(qMap);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);
        assertEquals(pw.getData().size(), 3);
    }

    @Test
    void testSort() {
        List<String> sort = Arrays.asList("-createdAt", "city");
        QReq qReq = new QReq()
                .setSort(sort);

        IPagedQuery<Customer> queryFunc = EQuery.buildPagedQuery(Customer.class);
        PageWrap<Customer> pw = queryFunc.query(qReq);

        List<Customer> customs = pw.getData().stream().sorted(comparing(Customer::getCreatedAt).reversed()
                .thenComparing(Customer::getCity)).collect(toList());
        assertEquals(pw.getData(), customs);
    }
}

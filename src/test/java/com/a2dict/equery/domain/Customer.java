package com.a2dict.equery.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by a2dict on 2019/11/9
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String city;
    private String phone;
    private Date createdAt;
}

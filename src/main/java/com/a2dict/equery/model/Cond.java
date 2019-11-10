package com.a2dict.equery.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Condition
 * Created by a2dict on 2019/10/18
 */
@Data
@Accessors(chain = true)
public class Cond {
    private String cond;
    private List<Object> vals;
}

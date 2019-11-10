package com.a2dict.equery.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by a2dict on 2019/10/18
 */
@Data
@Accessors(chain = true)
public class Od {
    private String col;
    private String od;
}

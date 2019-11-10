package com.a2dict.equery;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a2dict on 2019/11/9
 */
@Data
@Accessors(chain = true)
public class QReq {
    /**
     * 1-based
     */
    private int page = 1;
    private int size = 20;
    private List<String> select = new ArrayList<>();
    private Map<String, String> q = new HashMap<>();
    private List<String> sort = new ArrayList<>();
}

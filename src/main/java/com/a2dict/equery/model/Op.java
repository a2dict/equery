package com.a2dict.equery.model;

import com.a2dict.equery.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Operator
 * Created by zhangmd on 2019/10/18
 */
@Data
@Accessors(chain = true)
public class Op {
    /**
     * Allowed Ops
     */
    // TODO: add in not_in
    public static final Set<String> opSet = new HashSet<>(Arrays.asList("eq", "gt", "ge", "lt", "le", "like", "ilike", "in", "not_in"));

    private String op;
    private String col;
    private String val;

    public Cond toCond() {
        switch (op) {
            case "gt":
                return new Cond().setCond(String.format("`%s` > ?", col)).setVals(Collections.singletonList(val));
            case "ge":
                return new Cond().setCond(String.format("`%s` >= ?", col)).setVals(Collections.singletonList(val));
            case "lt":
                return new Cond().setCond(String.format("`%s` < ?", col)).setVals(Collections.singletonList(val));
            case "le":
                return new Cond().setCond(String.format("`%s` <= ?", col)).setVals(Collections.singletonList(val));
            case "like":
                return new Cond().setCond(String.format("`%s` like ?", col)).setVals(Collections.singletonList(val));
            case "ilike":
                return new Cond().setCond(String.format("lower(`%s`) like lower(?)", col)).setVals(Collections.singletonList(val));
            case "in": {
                List<Object> vals = JsonUtil.toJavaList(val);
                if (vals != null && !vals.isEmpty()) {
                    String inClause = vals.stream().map(it -> "?").collect(Collectors.joining(","));
                    return new Cond().setCond(String.format("`%s` in (%s)", col, inClause)).setVals(vals);
                }
                return null;
            }

            case "not_in": {
                List<Object> vals = JsonUtil.toJavaList(val);
                if (vals != null && !vals.isEmpty()) {
                    String inClause = vals.stream().map(it -> "?").collect(Collectors.joining(","));
                    return new Cond().setCond(String.format("`%s` not in (%s)", col, inClause)).setVals(vals);
                }
                return null;
            }
            default:
                return new Cond().setCond(String.format("`%s` = ?", col)).setVals(Collections.singletonList(val));
        }
    }
}

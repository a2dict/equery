package com.a2dict.equery;

import com.a2dict.equery.model.Cond;
import com.a2dict.equery.model.Od;
import com.a2dict.equery.model.Op;
import io.ebean.DB;
import io.ebean.Database;
import io.ebean.PagedList;
import io.ebean.Query;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.a2dict.equery.StringUtil.camelcase2underscore;
import static java.util.stream.Collectors.*;

/**
 * Created by a2dict on 2019/11/9
 */
public class EQuery {

    public static <T> IPagedQuery<T> buildPagedQuery(Class<T> clazz) {
        return buildPagedQuery(DB.getDefault(), clazz);
    }

    /**
     * 构建动态查询
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> IPagedQuery<T> buildPagedQuery(Database db, Class<T> clazz) {
        String idCol = getIdCol(clazz);
        String tableName = getTableName(clazz);
        Set<String> colSet = Stream.of(clazz.getDeclaredFields())
                .filter(it -> !it.isSynthetic())
                .map(EQuery::getColName)
                .collect(toSet());


        return qreq -> {
            String selectCause = "*";

            List<String> selects = qreq.getSelect().stream()
                    .map(StringUtil::camelcase2underscore)
                    .filter(colSet::contains)
                    .collect(toList());

            if (!selects.isEmpty()) {
                selectCause = selects.stream()
                        .map(it -> String.format("`%s`", it))
                        .collect(Collectors.joining(","));
            }


            String sql = String.format("select %s from `%s` ", selectCause, tableName);
            List<Object> params = new ArrayList<>();

            // parse condition
            List<Cond> conds = qreq.getQ().entrySet().stream()
                    .map(EQuery::parseOp)
                    .peek(it -> it.setCol(camelcase2underscore(it.getCol())))
                    .filter(it -> colSet.contains(it.getCol()))
                    .filter(it -> Op.opSet.contains(it.getOp()))
                    .map(Op::toCond)
                    .filter(Objects::nonNull)
                    .collect(toList());

            if (!conds.isEmpty()) {
                String whereCause = conds.stream().map(Cond::getCond)
                        .collect(joining(" and "));
                sql = sql + " where " + whereCause;
                params = conds.stream()
                        .map(Cond::getVals)
                        .flatMap(List::stream)
                        .collect(toList());
            }

            // parse order
            List<String> sorts = qreq.getSort() != null ? qreq.getSort() : new ArrayList<>();
            if (!sorts.isEmpty()) {
                String odCause = sorts.stream().map(it -> {
                    String od = it.startsWith("-") ? "desc" : "asc";
                    String col = it.replaceAll("^[+-]*", "");
                    return new Od().setCol(col)
                            .setOd(od);
                }).filter(it -> colSet.contains(it.getCol()))
                        .map(it -> String.format("`%s` %s", it.getCol(), it.getOd()))
                        .collect(joining(", "));
                sql = sql + " order by " + odCause;
            }

            // page
            int pageSize = qreq.getSize();
            if (pageSize > 200) {
                pageSize = 200;
            }

            int offset = (qreq.getPage() - 1) * pageSize;

            Query<T> query = db.findNative(clazz, sql);
            for (int i = 0; i < params.size(); i++) {
                query = query.setParameter(i + 1, params.get(i));
            }

            PagedList<T> pagedList = query.setFirstRow(offset)
                    .setMaxRows(pageSize)
                    .findPagedList();
            return new PageWrap<T>()
                    .setPage(qreq.getPage())
                    .setTotal(pagedList.getTotalCount())
                    .setPages(pagedList.getTotalPageCount())
                    .setSize(pagedList.getPageSize())
                    .setData(pagedList.getList());
        };
    }


    /**
     * 解析操作
     *
     * @param opEntry
     * @return
     */
    private static Op parseOp(Map.Entry<String, String> opEntry) {
        String k = opEntry.getKey();
        String v = opEntry.getValue();
        String[] sk = k.split("::");
        if (sk.length >= 2) {
            return new Op().setCol(sk[0])
                    .setOp(sk[1])
                    .setVal(v);
        }
        return new Op().setCol(k)
                .setOp("eq")
                .setVal(v);
    }

    private static <T> String getTableName(Class<T> clazz) {
        Table t = clazz.getDeclaredAnnotation(Table.class);
        if (t != null && !t.name().isEmpty()) {
            return t.name();
        }
        return camelcase2underscore(clazz.getSimpleName());
    }

    private static <T> String getColName(Field f) {
        Column c = f.getDeclaredAnnotation(Column.class);
        if (c != null && !c.name().isEmpty()) {
            return c.name();
        }

        return camelcase2underscore(f.getName());
    }

    private static <T> String getIdCol(Class<T> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(it -> it.getDeclaredAnnotation(Id.class) != null)
                .findFirst()
                .map(EQuery::getColName)
                .orElse("id");
    }


}

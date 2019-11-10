package com.a2dict.equery;

/**
 * Created by a2dict on 2019/11/9
 */
@FunctionalInterface
public interface IPagedQuery<T> {
    PageWrap<T> query(QReq qreq);
}

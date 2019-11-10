package com.a2dict.equery;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by a2dict on 2019/11/9
 */
@Data
@Accessors(chain = true)
public class PageWrap<T> {
    private int page;
    private int size;
    private long total;
    private int pages;
    private List<T> data;
}

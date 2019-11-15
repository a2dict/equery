package com.a2dict.equery;

import org.junit.jupiter.api.Test;

import static com.a2dict.equery.StringUtil.camelcase2underscore;
import static com.a2dict.equery.StringUtil.underscore2camelcase;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by a2dict on 2019/11/9
 */
class StringUtilTest {

    @Test
    void testCamelcase2underscore() {
        assertEquals(camelcase2underscore("abcDefGh"),"abc_def_gh");
        assertEquals(camelcase2underscore("AbcDefGh"),"_abc_def_gh");
        assertEquals(camelcase2underscore("abcDEfGh"),"abc_d_ef_gh");
        assertEquals(camelcase2underscore("ABc"),"_a_bc");
    }

    @Test
    void testUnderscore2camelcase() {
        assertEquals(underscore2camelcase("a_b_c"), "aBC");
        assertEquals(underscore2camelcase("a_bc"), "aBc");
        assertEquals(underscore2camelcase("_a_b_c"), "ABC");
        assertEquals(underscore2camelcase("a_b_c_"), "aBC_");
    }
}
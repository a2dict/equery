package com.a2dict.equery;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a2dict on 2019/11/9
 */
public class StringUtil {

    public static String camelcase2underscore(String s) {
        if (isEmpty(s)) {
            return s;
        }
        String res = replaceBy(s, "[A-Z]",
                m -> "_" + m.group().toLowerCase());
        return res;
    }

    public static String underscore2camelcase(String s) {
        if (isEmpty(s)) {
            return s;
        }
        String res = replaceBy(s, "_+(\\w)", m -> m.group(1).toUpperCase());
        return res;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String replaceBy(String templateText, String regex,
                                   Function<Matcher, String> replacer) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(templateText);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacer.apply(matcher));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

package com.a2dict.equery;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by a2dict on 2019/11/9
 */
public class StringUtil {

    public static String camelcase2underscore(String s) {
        if (isEmpty(s)) {
            return s;
        }
        String res = Stream.of(s.split("(?=[A-Z])"))
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"));
        return res;
    }

    public static String underscore2camelcase(String s) {
        if (isEmpty(s)) {
            return s;
        }
        String res = replaceAll(s, "_+(\\w)", m -> m.group(1).toUpperCase());
        return res;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String replaceAll(String templateText, String regex,
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

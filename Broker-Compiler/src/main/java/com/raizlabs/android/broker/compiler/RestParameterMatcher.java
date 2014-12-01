package com.raizlabs.android.broker.compiler;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RestParameterMatcher {

    private static Pattern pattern = Pattern.compile("\\{(.*?)\\}");

    public static List<String> getMatches(String url) {
        Matcher matcher = pattern.matcher(url);
        List<String> matches = Lists.newArrayList();
        while(matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches;
    }
}

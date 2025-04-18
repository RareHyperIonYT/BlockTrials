package me.RareHyperIon.BlockTrials.utility;

import java.util.ArrayList;
import java.util.List;

public final class StringUtility {

    public static String applyColor(final String message) {
        return message.replace('&', '§');
    }

    public static List<String> getPartialMatches(final String str, final Iterable<String> options) {
        final List<String> matches = new ArrayList<>();

        for (String string : options) {
            if (startsWithIgnoreCase(string, str)) {
                matches.add(string);
            }
        }

        return matches;
    }

    public static boolean startsWithIgnoreCase(final String string, final String prefix) {
        return string.length() >= prefix.length() &&
                string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

}

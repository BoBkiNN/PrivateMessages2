package pl.mirotcz.privatemessages.spigot.utils;

import java.util.*;

public class StringUtils {
    public static String getStringFromList(List list) {
        StringBuilder sb = new StringBuilder();
        if (list == null) {
            return sb.append("").toString();
        } else if (list.isEmpty()) {
            return sb.append("").toString();
        } else {
            Iterator it = list.iterator();

            while (it.hasNext()) {
                sb.append((String) it.next());
                if (it.hasNext()) {
                    sb.append(";");
                }
            }

            return sb.toString();
        }
    }

    public static List getListFromString(String string) {
        List list = new ArrayList();
        if (string == null) {
            return list;
        } else if (string.isEmpty()) {
            return list;
        } else {
            String[] elements = string.split(";");
            String[] var3 = elements;
            int var4 = elements.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String element = var3[var5];
                list.add(element);
            }

            return list;
        }
    }

    public static String getStringFromSet(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        if (set == null) {
            return sb.toString();
        } else if (set.isEmpty()) {
            return sb.toString();
        } else {
            Iterator<String> it = set.iterator();

            while (it.hasNext()) {
                sb.append((String) it.next());
                if (it.hasNext()) {
                    sb.append(";");
                }
            }

            return sb.toString();
        }
    }

    public static Set<String> getSetFromString(String string) {
        Set<String> set = new HashSet<>();
        if (string != null && !string.isEmpty()) {
            String[] elements = string.split(";");
            set.addAll(Arrays.asList(elements));
        }
        return set;
    }

    public static boolean isSafeString(String string) {
        if (string.contains("%")) {
            return false;
        } else if (string.contains("\\")) {
            return false;
        } else {
            return !string.contains("$");
        }
    }

    public static String getSafeString(String string) {
        return string.replace("%", "%%").replace("%%", "%").replace("\\", "\\\\").replace("$", "\\$");
    }

    public static List<String> getWordsFromMessage(String message) {
        List<String> words = new ArrayList<>();
        StringBuilder wordContent = new StringBuilder();

        for (int i = 1; i <= message.length(); ++i) {
            if (!Character.isWhitespace(message.charAt(i - 1))) {
                wordContent.append(message.charAt(i - 1));
                if (i + 1 > message.length()) {
                    words.add(wordContent.toString());
                    wordContent = new StringBuilder();
                }
            } else {
                wordContent.append(" ");
                words.add(wordContent.toString());
                wordContent = new StringBuilder();
            }
        }

        return words;
    }
}

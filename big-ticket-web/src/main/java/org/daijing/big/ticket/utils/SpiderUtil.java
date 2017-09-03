package org.daijing.big.ticket.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daijing03 on 17/6/28.
 */
public class SpiderUtil {
    private static final Pattern HHmmPattern = Pattern.compile("\\d+:\\d+");
    private static final Pattern yyyyMMddPattern = Pattern.compile("\\d+-\\d+-\\d+");
    private static final Pattern MMddPattern = Pattern.compile("\\d+-\\d+");

    private SpiderUtil() {}

    public static void handleReplyTimeFormat(List<String> lastReplyTimeList) throws ParseException {
        DateFormat yyyyDf = new SimpleDateFormat("yyyy");
        DateFormat hhMmDf = new SimpleDateFormat("HH:mm");
        DateFormat yyyyMMddDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat mmDdDf = new SimpleDateFormat("MM-dd");
        DateFormat standardDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb;
        Matcher matcher;
        Date now = new Date();
        int index = 0;
        for (String replyTime : lastReplyTimeList) {
            matcher = HHmmPattern.matcher(replyTime);
            if (matcher.matches()) {
                sb = new StringBuilder("");
                replyTime = sb.append(yyyyMMddDf.format(now)).append(" ").append(replyTime).append(":00").toString();
            }
            matcher = yyyyMMddPattern.matcher(replyTime);
            if (matcher.matches()) {
                replyTime = standardDf.format(yyyyMMddDf.parse(replyTime));
            }
            matcher = MMddPattern.matcher(replyTime);
            if (matcher.matches()) {
                replyTime = standardDf.format(yyyyMMddDf.parse(yyyyDf.format(now) + "-" + replyTime));
            }
            lastReplyTimeList.set(index, replyTime);
            index ++;
        }
    }
}

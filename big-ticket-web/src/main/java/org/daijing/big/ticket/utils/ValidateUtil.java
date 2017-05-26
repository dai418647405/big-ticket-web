package org.daijing.big.ticket.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**验证工具
 * Created by daijing03 on 17/5/25.
 */
public class ValidateUtil {

    public static boolean validatePass(String token) {
        if (token == null || token.trim().equals("")) {
            return false;
        }
        Date now = new Date();
        DateFormat dfYyyy = new SimpleDateFormat("yyyy");
        DateFormat dfMMdd = new SimpleDateFormat("MMdd");
        DateFormat dfHHmm = new SimpleDateFormat("HHmm");
        int yyyy = Integer.valueOf(dfYyyy.format(now));
        int mMdd = Integer.valueOf(dfMMdd.format(now));
        int hHmm = Integer.valueOf(dfHHmm.format(now));

        StringBuilder sb = new StringBuilder("");
        String passValue = sb.append(yyyy + 1994)
                             .append(mMdd + 217)
                             .append(hHmm + 413)
                             .toString();
        return token.equals(passValue);
    }
}

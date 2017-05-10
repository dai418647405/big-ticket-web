package org.daijing.big.ticket.crawler.hupu;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Store results in files.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
@ThreadSafe
public class HupuListPagePipeLine extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * create a HupuListPagePipeLine with default path"/data/webmagic/"
     */
    public HupuListPagePipeLine() {
        setPath("/data/webmagic/");
    }

    public HupuListPagePipeLine(String path) {
        setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".csv")),"UTF-8"));
            List<String> idList = resultItems.get("idList");
            List<String> titleList = resultItems.get("titleList");
            List<String> hrefList = resultItems.get("hrefList");
            List<String> replyCountList = resultItems.get("replyCountList");
            List<String> pageViewCountList = resultItems.get("pageViewCountList");
            List<String> lastReplyTimeList = resultItems.get("lastReplyTimeList");
            handleReplyTimeFormat(lastReplyTimeList);
            for (int index = 0; index <= titleList.size() - 1; index ++) {
                printWriter.println(idList.get(index) + "\t" + titleList.get(index) + "\t" + hrefList.get(index) + "\t" + replyCountList.get(index) + "\t" + pageViewCountList.get(index) + "\t" + lastReplyTimeList.get(index));
            }
            printWriter.close();
        } catch (IOException e) {
            logger.error("write file error", e);
        } catch (ParseException pe) {
            logger.error("parse reply date error", pe);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public static void handleReplyTimeFormat(List<String> lastReplyTimeList) throws ParseException {
        Pattern HHmmPattern = Pattern.compile("\\d+:\\d+");
        Pattern yyyyMMddPattern = Pattern.compile("\\d+-\\d+-\\d+");
        Pattern MMddPattern = Pattern.compile("\\d+-\\d+");

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

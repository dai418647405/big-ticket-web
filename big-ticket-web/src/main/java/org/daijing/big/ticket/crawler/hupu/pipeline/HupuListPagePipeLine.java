package org.daijing.big.ticket.crawler.hupu.pipeline;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.ThreadSafe;
import org.daijing.big.ticket.utils.SpiderUtil;
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
import java.text.ParseException;
import java.util.List;

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
            SpiderUtil.handleReplyTimeFormat(lastReplyTimeList);
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


}

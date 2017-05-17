package org.daijing.big.ticket.web;

import org.daijing.big.ticket.service.HupuListPageService;
import org.daijing.big.ticket.vo.PaginationVO;
import org.daijing.big.ticket.vo.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by daijing03 on 17/5/16.
 */
@RequestMapping("/api/hupu")
@Controller
public class HupuListPageController {

    private static final Logger logger = LoggerFactory.getLogger(AvatarDataApiController.class);

    @Qualifier("hupuListPageServiceImpl")
    @Autowired
    private HupuListPageService hupuListPageService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseData getListPage(Integer topicId, Integer sortType, PaginationVO pager) {
        try {
            return ResponseData.getSuccessResponse(hupuListPageService.getListPage(topicId, sortType, pager));
        } catch (IllegalArgumentException iae) {
            logger.error(String.format("getListPage req error! topicId=%d, sortType=%d, pager=%s"
                    , topicId, sortType, (pager != null ? pager.toString() : "null")), iae);
            return ResponseData.getSuccessResponse(null, iae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("getListPage error! params: topicId=%d, sortType=%d, pagination=%s"
                    , topicId, sortType, (pager != null ? pager.toString() : "null")), e);
            return ResponseData.getFailedResponse(null);
        }
    }
}

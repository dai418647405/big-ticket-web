package org.daijing.big.ticket.web;

import org.daijing.big.ticket.service.AvatarDataApiService;
import org.daijing.big.ticket.vo.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by daijing03 on 17/5/2.
 */
@RequestMapping("/api")
@Controller
public class AvatarDataApiController {

    private static final Logger logger = LoggerFactory.getLogger(AvatarDataApiController.class);

    @Qualifier("avatarDataApiServiceImpl")
    @Autowired
    private AvatarDataApiService avatarDataApiService;

    @RequestMapping("/latest/joke/img")
    @ResponseBody
    public ResponseData getLatestJokeImg(Integer page, Integer rows) {
        try {
            return ResponseData.getSuccessResponse(avatarDataApiService.getLatestJokeImg(page, rows));
        } catch (Exception e) {
            logger.error(String.format("getLatestJokeImg error! params: page=%d, rows=%d", page, rows));
            return ResponseData.getFailedResponse(null);
        }
    }
}

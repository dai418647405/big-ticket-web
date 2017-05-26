package org.daijing.big.ticket.web;

import org.daijing.big.ticket.service.BlogService;
import org.daijing.big.ticket.vo.BlogArticleVO;
import org.daijing.big.ticket.vo.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by daijing03 on 17/5/22.
 */
@RequestMapping("/api/blog")
@Controller
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Qualifier("blogServiceImpl")
    @Autowired
    private BlogService blogService;

    @RequestMapping("/title/list")
    @ResponseBody
    public ResponseData getBlogTitleList() {
        try {
            return ResponseData.getSuccessResponse(blogService.getBlogTitleList());
        } catch (Exception e) {
            logger.error("getBlogTitleList error!", e);
            return ResponseData.getFailedResponse(e.getMessage());
        }
    }

    @RequestMapping("/type/list")
    @ResponseBody
    public ResponseData getBlogTypeList() {
        try {
            return ResponseData.getSuccessResponse(blogService.getBlogTypeList());
        } catch (Exception e) {
            logger.error("getBlogTypeList error!", e);
            return ResponseData.getFailedResponse(e.getMessage());
        }
    }

    @RequestMapping("/get/{blogId}")
    @ResponseBody
    public ResponseData getBlogById(@PathVariable Integer blogId) {
        try {
            return ResponseData.getSuccessResponse(blogService.getBlogById(blogId));
        } catch (Exception e) {
            logger.error("getBlogById error, blogId=" + blogId, e);
            return ResponseData.getFailedResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/add/{token}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseData addBlog(@RequestBody BlogArticleVO blog, @PathVariable("token") String token) {
        try {
            return ResponseData.getSuccessResponse(blogService.addBlog(blog, token));
        } catch (Exception e) {
            logger.error("addBlog error, blog=" + (blog != null ? blog.toString() : "null"), e);
            return ResponseData.getFailedResponse(e.getMessage());
        }
    }
}

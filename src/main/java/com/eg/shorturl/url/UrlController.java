package com.eg.shorturl.url;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author makewheels
 * @Time 2021.01.10 22:26:42
 */
@Controller
public class UrlController {
    @Resource
    private UrlService urlService;

    /**
     * 添加新的url
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public String add(@RequestBody JSONObject request) {
        return urlService.add(request);
    }

    @GetMapping("/{shortId}")
    public String visit(HttpServletResponse response, @PathVariable("shortId") String shortId) {
        String url = urlService.getUrlFromRedis(shortId);
        if (url == null) {
            response.setStatus(404);
            return shortId + " not exist";
        }
        return "redirect:" + url;
    }
}

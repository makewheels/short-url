package com.eg.shorturl.controller;

import com.eg.shorturl.bean.Url;
import com.eg.shorturl.service.UrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
     * @param fullUrl
     * @param timestamp
     * @param sign
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public String add(@RequestParam String fullUrl, @RequestParam String timestamp,
                      @RequestParam String sign) {
        return urlService.add(fullUrl, timestamp, sign);
    }

    @RequestMapping("/{shortId}")
    public String visit(@PathVariable("shortId") String shortId) {
        //查询shortUrl
        Url urlByShortUrl = urlService.getUrlByShortId(shortId);
        if (urlByShortUrl == null) {
            return null;
        }
        return "redirect:" + urlByShortUrl.getFullUrl();
    }
}

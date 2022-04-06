package com.eg.shorturl.url;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.eg.shorturl.bean.Url;
import com.eg.shorturl.bean.UrlExample;
import com.eg.shorturl.bean.mapper.UrlMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author makewheels
 * @Time 2021.01.10 22:35:33
 */
@Service
public class UrlService {
    @Resource
    private UrlMapper urlMapper;
    @Resource
    private UrlRedisService urlRedisService;

    @Value("${baseUrl}")
    private String baseUrl;

    /**
     * 根据fullUrl查询
     *
     * @param fullUrl
     * @return
     */
    public Url getUrlByFullUrl(String fullUrl) {
        UrlExample urlExample = new UrlExample();
        urlExample.createCriteria().andFullUrlEqualTo(fullUrl);
        List<Url> urlList = urlMapper.selectByExample(urlExample);
        if (CollectionUtils.isEmpty(urlList)) {
            return null;
        }
        return urlList.get(0);
    }

    /**
     * 从缓存拿url
     *
     * @param shortId
     * @return
     */
    public String getUrlFromRedis(String shortId) {
        Url url = urlRedisService.getUrl(shortId);
        if (url == null) {
            url = getUrlByShortId(shortId);
            //如果数据库也不存在，那就不对了，直接返回
            if (url == null) return null;
            url.setVisitCount(null);
            urlRedisService.setUrl(shortId, url);
        }
        Long urlId = url.getId();

        //增加访问次数
        Integer oldVisitCount = urlMapper.selectByPrimaryKey(url.getId()).getVisitCount();
        new Thread(() -> {
            Url updateUrl = new Url();
            updateUrl.setId(urlId);
            updateUrl.setVisitCount(oldVisitCount + 1);
            urlMapper.updateByPrimaryKeySelective(updateUrl);
        }).start();

        return url.getFullUrl();
    }

    /**
     * 根据shortId查询
     *
     * @param shortId
     * @return
     */
    public Url getUrlByShortId(String shortId) {
        UrlExample urlExample = new UrlExample();
        urlExample.createCriteria().andShortIdEqualTo(shortId);
        List<Url> urlList = urlMapper.selectByExample(urlExample);
        if (CollectionUtils.isEmpty(urlList)) {
            return null;
        }
        return urlList.get(0);
    }

    /**
     * 新增url
     *
     * @param request
     * @return
     */
    public String add(JSONObject request) {
        String fullUrl = request.getString("fullUrl");

        //签名校验
//        String sign = request.getString("sign");
//        String key = System.getenv("short_url_add_sign_key");
//        if (!SecureUtil.md5(fullUrl + key).equals(sign)) {
//            return "签名校验错误";
//        }

        //根据fullUrl查询url
        Url urlByFullUrl = getUrlByFullUrl(fullUrl);
        //如果已经有了，则返回shortUrl
        if (urlByFullUrl != null) return baseUrl + urlByFullUrl.getShortId();

        //生成随机id
        String shortId = IdUtil.nanoId();

        //到这里说明找到了没有的新id，保存到数据库
        Url url = new Url();
        url.setShortId(shortId);
        url.setShortUrl(baseUrl + shortId);
        url.setFullUrl(fullUrl);
        url.setCreateTime(new Date());
        url.setVisitCount(0);
        urlMapper.insert(url);

        //返回shortUrl
        return url.getShortUrl();
    }
}

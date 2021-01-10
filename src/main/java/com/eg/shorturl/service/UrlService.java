package com.eg.shorturl.service;

import com.eg.shorturl.bean.Url;
import com.eg.shorturl.bean.UrlExample;
import com.eg.shorturl.bean.mapper.UrlMapper;
import com.eg.shorturl.util.Constants;
import com.eg.shorturl.util.RandomUtil;
import org.apache.commons.collections4.CollectionUtils;
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
     * @param fullUrl
     * @param timestamp
     * @param sign
     * @return
     */
    public String add(String fullUrl, String timestamp, String sign) {
        //签名校验

        //根据fullUrl查询url
        Url urlByfullUrl = getUrlByFullUrl(fullUrl);
        //如果已经有了，则返回shortUrl
        if (urlByfullUrl != null) {
            return urlByfullUrl.getShortUrl();
        }

        //生成随机id
        String shortId;
        //查询数据库，看这个id是否已经存在了，如果已经存在，重新生成
        do {
            shortId = RandomUtil.getRandomString(Constants.SHORT_ID_LENGTH);
            System.out.println(shortId);
        } while (getUrlByShortId(shortId) != null);

        //到这里说明找到了没有的新id，保存到数据库
        Url url = new Url();
        url.setShortId(shortId);
        url.setShortUrl(Constants.BASE_URL + shortId);
        url.setFullUrl(fullUrl);
        url.setCreateTime(new Date());
        url.setVisitCount(0);
        urlMapper.insert(url);

        //返回shortUrl
        return url.getShortUrl();
    }
}

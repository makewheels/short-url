package com.eg.shorturl.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Url {
    private Long id;

    private String shortId;

    private String shortUrl;

    private String fullUrl;

    private Date createTime;

    private Integer visitCount;

}
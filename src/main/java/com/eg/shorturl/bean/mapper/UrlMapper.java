package com.eg.shorturl.bean.mapper;

import com.eg.shorturl.bean.Url;
import com.eg.shorturl.bean.UrlExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UrlMapper {
    long countByExample(UrlExample example);

    int deleteByExample(UrlExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Url record);

    int insertSelective(Url record);

    List<Url> selectByExample(UrlExample example);

    Url selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Url record, @Param("example") UrlExample example);

    int updateByExample(@Param("record") Url record, @Param("example") UrlExample example);

    int updateByPrimaryKeySelective(Url record);

    int updateByPrimaryKey(Url record);
}
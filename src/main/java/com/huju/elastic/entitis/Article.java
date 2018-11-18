package com.huju.elastic.entitis;

import io.searchbox.annotations.JestId;
import lombok.Data;

/**
 * Created by huju on 2018/11/18.
 */
@Data
public class Article {

    // 用@JestId来标识这是一个主键
    @JestId
    private Integer id;
    private String author;
    private String title;
    private String content;


}

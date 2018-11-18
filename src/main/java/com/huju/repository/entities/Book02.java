package com.huju.repository.entities;

import lombok.Data;

/**
 * Created by huju on 2018/11/17.
 */

@Data
// @Document(indexName = "test002",type = "book")
public class Book02 {

    private Integer id;
    private String bookName;
    private String author;

}

package com.huju.rabbitmq.entities;

import lombok.Data;

/**
 * Created by huju on 2018/11/17.
 */

@Data
public class Book {

    private String bookName;
    private String author;

    public Book() {
    }

    public Book(String bookName, String author) {
        this.bookName = bookName;
        this.author = author;
    }

}

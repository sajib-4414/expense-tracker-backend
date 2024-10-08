package com.sajib_4414.expense.tracker.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class PagedResponse <T>{
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;


    public PagedResponse(
             List<T> content,
           int page,
            int size,
             long totalElements,
            int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}

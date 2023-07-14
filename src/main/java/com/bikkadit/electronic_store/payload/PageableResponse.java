package com.bikkadit.electronic_store.payload;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponse <T>{

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalpages;
    private boolean lastpage;

}

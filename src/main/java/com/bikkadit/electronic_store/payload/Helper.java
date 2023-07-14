package com.bikkadit.electronic_store.payload;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U,V> PageableResponse<V> getpageableResponse(Page<U> page, Class<V> type){
        List<U> entity = page.getContent();
        List<V> userDtos = entity.stream()
                .map((object) -> new ModelMapper().map(object, type))
                .collect(Collectors.toList());

        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(userDtos);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());

        response.setTotalElements(page.getTotalElements());
        response.setTotalpages(page.getTotalPages());
        response.setLastpage(page.isLast());

        return response;
    }
}

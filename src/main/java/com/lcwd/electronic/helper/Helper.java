package com.lcwd.electronic.helper;

import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.dtos.UserDto;
import com.lcwd.electronic.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    //U-Entity
    //V-Dto
    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map((Object) -> new ModelMapper().map(Object,type)).collect(Collectors.toList());
        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPage(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }
}

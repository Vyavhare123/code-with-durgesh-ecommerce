package com.lcwd.electronics.store.helper;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import com.lcwd.electronics.store.dtos.PageableResponse;
public class Helper {
	
	//U- USER
	//V- USERDTO 
	
	public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class <V> type){
		List<U> entity = page.getContent();
		List<V> userDtoList = entity.stream().map(object -> new ModelMapper().map(object,type))
				.collect(Collectors.toList());

		PageableResponse<V> pageableResponse = new PageableResponse<>();
		pageableResponse.setContent(userDtoList);
		pageableResponse.setPageNumber(page.getNumber());
		pageableResponse.setPageSize(page.getSize());
		pageableResponse.setTotalElement(page.getTotalElements());
		pageableResponse.setLastpage(page.isLast());
		pageableResponse.setTotalPage(page.getTotalPages());
		return pageableResponse; 
		
	}

}

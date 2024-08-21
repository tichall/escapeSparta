package com.sparta.global.util;

import com.sparta.global.exception.customException.PageException;
import com.sparta.global.exception.errorCode.PageErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {

//    public static final int PAGE_SIZE_LIMIT = 5;
//    public static final int SEARCH_SIZE_LIMIT = 50;

  private PageUtil() {
  }

  public static Pageable createPageable(int pageNum, int pageSize, boolean isDesc, String sortBy) {
    if (pageNum < 1) {
      throw new PageException(PageErrorCode.PAGE_NOT_FOUND);
    }

    Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, sortBy);

    return PageRequest.of(pageNum - 1, pageSize, sort);
  }

//    public static long getOffset(int pageNumber) {
//        if (pageNumber < PageUtil.PAGE_SIZE_LIMIT) {
//            return 0;
//        }
//        return (pageNumber / PageUtil.PAGE_SIZE_LIMIT) * PageUtil.SEARCH_SIZE_LIMIT;
//    }
}
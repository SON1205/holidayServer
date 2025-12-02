package com.planitsquare.holidayserver.common.dto;

import org.springframework.data.domain.Page;

public record PageDto(
        long totalCount,
        int totalPages,
        int currentPage,
        boolean hasNext,
        boolean hasPrevious
) {
    public static PageDto from(Page<?> page) {
        return new PageDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.hasNext(),
                page.hasPrevious()
        );
    }
}

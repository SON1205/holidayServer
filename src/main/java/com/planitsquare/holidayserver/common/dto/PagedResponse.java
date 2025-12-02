package com.planitsquare.holidayserver.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResponse<T>(
        List<T> items,
        PageDto page
) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(List.copyOf(page.getContent()), PageDto.from(page));
    }
}

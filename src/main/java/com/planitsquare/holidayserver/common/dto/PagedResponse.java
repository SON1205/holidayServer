package com.planitsquare.holidayserver.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "offset 기반 페이징 응답 DTO")
public record PagedResponse<T>(
        @Schema(description = "데이터")
        List<T> items,

        @Schema(description = "페이지 정보")
        PageDto page
) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(List.copyOf(page.getContent()), PageDto.from(page));
    }
}

package com.planitsquare.holidayserver.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "페이지 정보 응답 DTO")
public record PageDto(
        @Schema(description = "데이터 전체 개수", example = "2025")
        long totalCount,

        @Schema(description = "전체 페이지 개수", example = "30")
        int totalPages,

        @Schema(description = "현재 페이지 번호", example = "3")
        int currentPage,

        @Schema(description = "다음 페이지로 갈 수 있는지에 대한 여부", example = "true")
        boolean hasNext,

        @Schema(description = "이전 페이지로 갈 수 있는지에 대한 여부", example = "true")
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

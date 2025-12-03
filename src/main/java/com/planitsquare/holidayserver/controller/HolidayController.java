package com.planitsquare.holidayserver.controller;

import com.planitsquare.holidayserver.common.dto.PagedResponse;
import com.planitsquare.holidayserver.common.exception.ExceptionResponse;
import com.planitsquare.holidayserver.dto.HolidayRefreshReq;
import com.planitsquare.holidayserver.dto.HolidayRefreshRes;
import com.planitsquare.holidayserver.dto.HolidayRemoveReq;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.planitsquare.holidayserver.dto.RefreshResult;
import com.planitsquare.holidayserver.service.HolidayService;
import com.planitsquare.holidayserver.service.HolidaySyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Holiday", description = "공휴일 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/holidays")
public class HolidayController {
    private final HolidayService holidayService;
    private final HolidaySyncService holidaySyncService;

    @Operation(
            summary = "공휴일 검색 필터",
            description = "연도, 국가 등의 필터 기반 공휴일 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (파라미터 값 오류 등)",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
    })
    @GetMapping
    public ResponseEntity<PagedResponse> searchHolidays(
            @Valid @ModelAttribute HolidaySearchCondition condition,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        Page<HolidayResponse> res = holidayService.searchHolidays(condition, pageable);
        return ResponseEntity.ok(PagedResponse.from(res));
    }

    @Operation(
            summary = "공휴일 재동기화",
            description = "특정 연도 및 국가 데이터 재호출하여 덮어쓰기"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "덮어쓰기 성공",
                    content = @Content(schema = @Schema(implementation = HolidayRefreshRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (파라미터 값 오류 등)",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
    })
    @PostMapping("/refresh")
    public ResponseEntity<HolidayRefreshRes> refreshHolidays(@Valid @RequestBody HolidayRefreshReq request) {
        RefreshResult refreshed = holidaySyncService.refreshHoliday(request);
        return ResponseEntity.ok(HolidayRefreshRes.from(refreshed));
    }

    @Operation(
            summary = "공휴일 삭제",
            description = "특정 연도 및 국가의 공휴일 데이터 전체 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (파라미터 값 오류 등)",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
    })
    @DeleteMapping
    public ResponseEntity<Void> removeHolidays(@Valid @ModelAttribute HolidayRemoveReq request) {
        holidayService.removeHoliday(request);
        return ResponseEntity.noContent().build();
    }
}


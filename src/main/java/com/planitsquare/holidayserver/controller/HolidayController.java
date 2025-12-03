package com.planitsquare.holidayserver.controller;

import com.planitsquare.holidayserver.common.dto.PagedResponse;
import com.planitsquare.holidayserver.dto.HolidayRefreshReq;
import com.planitsquare.holidayserver.dto.HolidayRefreshRes;
import com.planitsquare.holidayserver.dto.HolidayRemoveReq;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.planitsquare.holidayserver.dto.RefreshResult;
import com.planitsquare.holidayserver.service.HolidayService;
import com.planitsquare.holidayserver.service.HolidaySyncService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/holidays")
public class HolidayController {
    private final HolidayService holidayService;
    private final HolidaySyncService holidaySyncService;

    @GetMapping
    public ResponseEntity<PagedResponse> searchHolidays(
            @Valid @ModelAttribute HolidaySearchCondition condition,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        Page<HolidayResponse> res = holidayService.searchHolidays(condition, pageable);
        return ResponseEntity.ok(PagedResponse.from(res));
    }

    @PostMapping("/refresh")
    public ResponseEntity<HolidayRefreshRes> refreshHolidays(@RequestBody HolidayRefreshReq request) {
        RefreshResult refreshed = holidaySyncService.refreshHoliday(request);
        return ResponseEntity.ok(HolidayRefreshRes.from(refreshed));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeHolidays(@Valid @ModelAttribute HolidayRemoveReq request) {
        holidayService.removeHoliday(request);
        return ResponseEntity.noContent().build();
    }
}


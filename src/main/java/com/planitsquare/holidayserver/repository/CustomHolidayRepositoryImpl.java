package com.planitsquare.holidayserver.repository;

import static com.planitsquare.holidayserver.domain.QCountry.country;
import static com.planitsquare.holidayserver.domain.QHoliday.holiday;
import static org.springframework.util.StringUtils.hasLength;

import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomHolidayRepositoryImpl implements CustomHolidayRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Holiday> search(HolidaySearchCondition condition, Pageable pageable) {
        List<Holiday> content = getHolidayJPAQuery(condition)
                .orderBy(holiday.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(holiday.count())
                .from(holiday)
                .where(
                        countryCodeEq(condition.countryCode()),
                        yearEq(condition.year()),
                        startDateGoe(condition.startDate()),
                        endDateLoe(condition.endDate()),
                        typeEq(condition.type())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count != null ? count : 0);
    }

    private JPAQuery<Holiday> getHolidayJPAQuery(HolidaySearchCondition condition) {
        return queryFactory
                .selectFrom(holiday)
                .join(holiday.country, country).fetchJoin()
                .where(
                        countryCodeEq(condition.countryCode()),
                        yearEq(condition.year()),
                        startDateGoe(condition.startDate()),
                        endDateLoe(condition.endDate()),
                        typeEq(condition.type())
                );
    }

    private BooleanExpression countryCodeEq(@Size(min = 2, max = 3) String countryCode) {
        return hasLength(countryCode) ? holiday.country.countryCode.eq(countryCode) : null;
    }

    private BooleanExpression yearEq(@Min(value = 1900) @Max(value = 2100) Integer year) {
        return year == null ? null : holiday.date.year().eq(year);
    }

    private BooleanExpression startDateGoe(LocalDate startDate) {
        return startDate == null ? null : holiday.date.goe(startDate);
    }

    private BooleanExpression endDateLoe(LocalDate endDate) {
        return endDate == null ? null : holiday.date.loe(endDate);
    }

    private BooleanExpression typeEq(String type) {
        return hasLength(type) ? holiday.types.contains(type) : null;
    }
}

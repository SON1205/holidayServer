package com.planitsquare.holidayserver.repository;

import static com.planitsquare.holidayserver.domain.QCountry.country;
import static com.planitsquare.holidayserver.domain.QHoliday.holiday;
import static org.springframework.util.StringUtils.hasLength;

import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomHolidayRepositoryImpl implements CustomHolidayRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Holiday> search(HolidaySearchCondition condition, Pageable pageable) {
        List<Holiday> content = queryFactory
                .selectFrom(holiday)
                .join(holiday.country, country).fetchJoin()
                .where(
                        countryCodeEq(condition.countryCode()),
                        dateOnCondition(condition),
                        typeEq(condition.type())
                )
                .orderBy(
                        holiday.date.desc(),
                        holiday.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(holiday.count())
                .from(holiday)
                .where(
                        countryCodeEq(condition.countryCode()),
                        dateOnCondition(condition),
                        typeEq(condition.type())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression countryCodeEq(String countryCode) {
        return hasLength(countryCode) ? holiday.country.countryCode.eq(countryCode) : null;
    }

    private BooleanExpression dateOnCondition(HolidaySearchCondition condition) {
        if (condition.startDate() != null || condition.endDate() != null) {
            return dateBetween(condition.startDate(), condition.endDate());
        }
        return yearEq(condition.year());
    }

    private BooleanExpression yearEq(Integer year) {
        return year == null ? null : holiday.date.year().eq(year);
    }

    private BooleanExpression dateBetween(LocalDate start, LocalDate end) {
        if (start == null && end == null) {
            return null;
        }

        if (end == null) {
            return holiday.date.goe(start);
        }

        if (start == null) {
            return holiday.date.loe(end);
        }

        return holiday.date.between(start, end);
    }

    private BooleanExpression typeEq(String type) {
        return hasLength(type) ? holiday.types.contains(type) : null;
    }
}

package none.cvg.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DateTime in Streams.
 *
 * @see TemporalAdjuster
 * @see TemporalAdjusters
 * @see DayOfWeek
 * @see Stream
 * @see IntStream
 * @see Collectors
 */
@DisplayNameGeneration(DateTimeKataDisplayNames.class)
@DisplayName("Temporal Adjusters and Dates in Streams")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSolution6StreamsInDateTime {

    @Test
    @Tag("PASSING")
    @Order(1)
    public void verifyFewTemporalAdjusters() {

        LocalDate tOJDate = LocalDate.of(1997, 8, 29);


        // DONE: Fix the actual to render the "first day of the month".
        //  Check: java.time.temporal.TemporalAdjusters for the right methods.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        assertEquals("1997-08-01",
                tOJDate.with(TemporalAdjusters.firstDayOfMonth()).toString(),
                "First Day Of The Month should be 1997-08-01, 28 days before the date");


        // DONE: Fix the actual to render the "last day of the month".
        //  Check: java.time.temporal.TemporalAdjusters for the right methods.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        assertEquals(tOJDate.plusDays(2),
                tOJDate.with(TemporalAdjusters.lastDayOfMonth()),
                "Last Day Of The Month should be 1997-08-31, 2 days after the date");


        // DONE: Fix the actual to render the "first day of next month".
        //  Check: java.time.temporal.TemporalAdjusters for the right methods.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        assertEquals("1997-09-01",
                tOJDate.with(TemporalAdjusters.firstDayOfNextMonth()).toString(),
                "First Day Of Next Month should be 1997-09-01, 3 days after the date");


        // DONE: Fix the actual to render the "next Wednesday after the date".
        //  Check: java.time.temporal.TemporalAdjusters for the right methods.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        assertEquals(tOJDate.plusDays(5),
                tOJDate.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)),
                "Next Wednesday Month should be 1997-09-03, 5 days after the date");



        // The first day of the next year after the Original Judgement day.
        LocalDate nextYearDate = LocalDate.of(1998, 1, 1);

        // DONE: Fix the actual to render the "first day of next year".
        //  Check: java.time.temporal.TemporalAdjusters for the right methods.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        assertEquals(nextYearDate.toString(),
                tOJDate.with(TemporalAdjusters.firstDayOfNextYear()).toString(),
                "First Day Of Next Year should be 1998-01-01");


        // DONE: Fix the actual to render the "first day of year".
        //  Check: java.time.temporal.TemporalAdjusters for the right methods.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        assertEquals(nextYearDate.minusDays(365),
                tOJDate.with(TemporalAdjusters.firstDayOfYear()),
                "First day of year should be 365 days less than first day of next year");
    }

    @Test
    @Tag("PASSING")
    @Order(2)
    public void verifyStreamOperationsOnTemporalAdjustment() {

        TemporalAdjuster nextOrSameSunday = temporal -> {
            LocalDate localDate = LocalDate.from(temporal);
            // DONE: Add a localDate manipulation that returns
            //  either the next sunday after current date
            //  it is a or current date, if Sunday.
            //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
            //  Check: java.time.temporal.TemporalAdjusters.nextOrSame(DayOfWeek)
            localDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            return temporal.with(localDate);
        };

        Stream<LocalDate> someDates = Stream.of(
                LocalDate.of(1997, 8, 29),
                LocalDate.of(2015, 1, 1),
                LocalDate.of(2015, 1, 4));

        // DONE: Fix the list mapping below.
        //  Check: java.time.LocalDate.with(java.time.temporal.TemporalAdjuster)
        List<LocalDate> collectSundays = someDates
                .map(each -> each.with(nextOrSameSunday))
                .collect(Collectors.toList());

        assertEquals("1997-08-31",
                collectSundays.get(0).toString(),
                "Sunday on or after 1997-08-29 is 1997-08-31");

        assertEquals("2015-01-04",
                collectSundays.get(1).toString(),
                "Sunday on or after 2015-01-01 is 2015-01-04");

        assertEquals("2015-01-04",
                collectSundays.get(2).toString(),
                "Sunday on or after 1997-01-04 is 2015-01-04");
    }

    @Test
    @Tag("PASSING")
    @Order(3)
    public void twoBusinessDaysShippingCalculator() {

        // DONE: Fix the TemporalAdjuster below.
        //  Orders placed on Monday, Tuesday and Wednesday ship 2 business days later.
        //  Orders placed on Thursday ship on the next Monday.
        //  Orders places on Friday, Saturday and Sunday ship on the next Tuesday.
        //  =>
        //  HINT: use the zero-based ordinal() or one-based getValue() from the DayOfWeek
        //        enum for simplicity.
        TemporalAdjuster tPlusTwoBusinessDates = temporal -> {
            LocalDate localDate = LocalDate.from(temporal);
            int dayOfWeekValue = localDate.getDayOfWeek().getValue();
            if (dayOfWeekValue < 4) {
                localDate = localDate.plusDays(2);
            } else {
                if (dayOfWeekValue == 4) {
                    localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                } else {
                    localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
                }
            }
            return temporal.with(localDate);
        };

        LocalDate localDate = LocalDate.of(2015, 11, 18);

        List<LocalDate> sampleShippingDates = new ArrayList<>();

        IntStream.iterate(0, i -> i + 1)
                .limit(10)
                .forEach(i -> sampleShippingDates.add(localDate.plusDays(i)));

        Map<LocalDate, LocalDate> shippingDateMap = sampleShippingDates.stream()
                .collect(Collectors.toMap(each -> each, each -> each.with(tPlusTwoBusinessDates)));

        assertEquals(DayOfWeek.WEDNESDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)))
                        .getDayOfWeek(),
                "Items purchased on Monday should ship on the following Wednesday");

        assertEquals(DayOfWeek.THURSDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)))
                        .getDayOfWeek(),
                "Items purchased on Tuesday should ship on the following Thursday");

        assertEquals(DayOfWeek.FRIDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)))
                        .getDayOfWeek(),
                "Items purchased on Wednesday should ship on the following Friday");

        assertEquals(DayOfWeek.MONDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)))
                        .getDayOfWeek(),
                "Items purchased on Thursday should ship on the following Monday");

        assertEquals(DayOfWeek.TUESDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)))
                        .getDayOfWeek(),
                "Items purchased on Friday should ship on the following Tuesday");

        assertEquals(DayOfWeek.TUESDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)))
                        .getDayOfWeek(),
                "Items purchased on Saturday should ship on the following Tuesday");

        assertEquals(DayOfWeek.TUESDAY,
                shippingDateMap
                        .get(localDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)))
                        .getDayOfWeek(),
                "Items purchased on Sunday should ship on the following Tuesday");

        assertTrue(shippingDateMap
                        .values()
                        .stream()
                        .noneMatch(localDate1 -> localDate1.getDayOfWeek().getValue() > 5),
                "No weekend shipping");

    }

}

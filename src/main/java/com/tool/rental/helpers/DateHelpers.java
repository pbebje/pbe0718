package com.tool.rental.helpers;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateHelpers {

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isNotBlank(format) ? format : "MM/dd/yy");
        return sdf.format(date);
    }

    /*
     * This Method is find any Holidays in a range of Dates. Method is null safe.
     * @param begDate - begin LocalDate value of a range of Dates to check
     * @param endDate - begin LocalDate value of a range of Dates to check
     * @return - List of LocalDate objects that are found to be a Holiday
     * @throws - no exceptions, any bad values sent into params or any errors in date calculations will return an empty list
     */
    public static List<LocalDate> holidaysInDateRange(LocalDate begDate, int numDays) {
        List<LocalDate> holidays = new ArrayList<>();
        if (begDate == null || numDays == 0) {
            return holidays;
        }
        Set<LocalDate> dateSet = new HashSet<>();
        LocalDate dateToCheck = LocalDate.of(begDate.getYear(), begDate.getMonthValue(), begDate.getDayOfMonth());
        dateSet.add(getFirstMondayOfMonth(begDate.getYear(), 9));
        dateSet.add(getClosestWeekDay(LocalDate.of(begDate.getYear(), 7, 4)));
        for (int i = 0; i < numDays; i++)  {
            if (dateSet.contains(dateToCheck)) {
                holidays.add(dateToCheck);
            }
            dateToCheck = dateToCheck.plusDays(1);
        }
        return holidays;
    }

    public static int numHolidaysInDateRange(LocalDate begDate, int numDays) {
        if (begDate == null || numDays == 0) {
            return 0;
        }
        return holidaysInDateRange(begDate, numDays).size();
    }

    public static List<LocalDate> weekendDaysInDateRange(LocalDate begDate, int numDays) {
        List<LocalDate> weekendDays = new ArrayList<>();
        if (begDate == null || numDays == 0) {
            return weekendDays;
        }
        LocalDate dateToCheck = LocalDate.of(begDate.getYear(), begDate.getMonthValue(), begDate.getDayOfMonth());
        for (int i = 0; i < numDays; i++)  {
            if (dateIsOnWeekEnd(dateToCheck)) {
                weekendDays.add(dateToCheck);
            }
            dateToCheck = dateToCheck.plusDays(1);
        }
        return weekendDays;
    }

    public static int numWeekendDaysInDateRange(LocalDate begDate, int numDays) {
        if (begDate == null || numDays == 0) {
            return 0;
        }
        return weekendDaysInDateRange(begDate, numDays).size();
    }

    /*
     * This Method is used to see if a Date is on a weekend. Method is null safe.
     * @param date - LocalDate value to see if it is a weekend day
     * @return - boolean primitive - True if weekend day, else False
     * @throws - no exceptions, any bad values sent into params or any errors in date calculations will return false
     */
    public static boolean dateIsOnWeekEnd(LocalDate date) {
        if (date != null) {
            DayOfWeek day = date.getDayOfWeek();
            return DayOfWeek.SATURDAY.equals(day) || DayOfWeek.SUNDAY.equals(day);
        }
        return false;
    }

    /*
     * This Method is used to Find the first Monday in a month and year.
     * @param year - int value of year
     * @param month - int value of month
     * @return - LocalDate object of first Monday in that month and year
     * @throws - no exceptions, any bad values sent into params or any errors in date calculations will return null
     */
    private static LocalDate getFirstMondayOfMonth(int year, int month) {
        if (year < 0 || month < 1 || month > 12) {
            return null;
        }
        LocalDate ld = LocalDate.of(year, Month.of(month), 1);
        return ld.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    /*
     * This Method is used to see if a Date value is on weekend and finds closest weekday if needed.
     * @param date - LocalDate value to check
     * @return - if LocalDate sent in is on weekend it returns closet weekday LocalDate, else LocalDate sent in
     * @throws - no exceptions, any bad values sent into params or any errors in date calculations will return null
     */
    private static LocalDate getClosestWeekDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY ? date :
                (day == DayOfWeek.SATURDAY ? date.minusDays(1) : date.plusDays(1));
    }
}

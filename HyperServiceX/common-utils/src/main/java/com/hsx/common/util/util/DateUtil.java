package com.hsx.common.util.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    public static final ZoneId SG_ZONE = ZoneId.of("UTC+8");
    public static final ZoneId TH_ZONE = ZoneId.of("UTC+7");
    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    public static final String localISODateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String localISONormalizedDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISODatePattern = "yyyy-MM-dd";
    public static final String utcDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String defaultDateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public enum TimeUnit {
        DAYS, HOURS, MINUTES, SECONDS;

        static final long C0 = 1L;
        static final long C1 = C0 * 1000L;
        static final long C2 = C1 * 60L;
        static final long C3 = C2 * 60L;
        static final long C4 = C3 * 24L;

        public Long convertToMilliSecond(Long d) {

            switch (this) {
                case SECONDS:
                    return d * C1;
                case MINUTES:
                    return d * C2;
                case HOURS:
                    return d * C3;
                case DAYS:
                    return d * C4;
                default:
                    throw new AssertionError("Unknown TIME UNIT " + this);
            }

        }
    }

   /* public static DateTimeFormatter ISO_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static DateTimeFormatter ISO_ZONED = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static DateTimeFormatter YYYY_MM_DDTHH_MM_SS_SSS = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalStart()
            .appendFraction(MILLI_OF_SECOND, 0, 3, true)
            .toFormatter();

    public static DateTimeFormatter YYYY_MM_DDTHH_MM_SS = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter();

    public static String now() {
        return ZonedDateTime.now().format(ISO_ZONED);
    }

    public static String now(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId).format(ISO_ZONED);
    }

    public static String now(DateTimeFormatter toFormatter) {
        return ZonedDateTime.now().format(toFormatter);
    }
    public static String formatDateFromDefaultFomat(String date, DateTimeFormatter toFormatter, ZoneId toZone) {
        return formatDate(date, ISO_ZONED, toFormatter, SG_ZONE, toZone);
    }

    public static String formatDateToDefaultFormat(String date, DateTimeFormatter fromFormatter, ZoneId fromId) {
        return formatDate(date, fromFormatter, ISO_ZONED, fromId, SG_ZONE);
    }

   public static String formatDate(String date, DateTimeFormatter fromFormatter, DateTimeFormatter toFormatter, ZoneId fromId, ZoneId toZoneId) {
        LocalDateTime dateTime = LocalDateTime.parse(date, fromFormatter);
        return formatDate(dateTime, toFormatter, fromId, toZoneId);
    }

    public static String formatDate(LocalDateTime dateTime, DateTimeFormatter toFormatter, ZoneId fromId, ZoneId toZoneId) {
        ZonedDateTime zonedDateTime = dateTime.atZone(fromId);
        return zonedDateTime.withZoneSameInstant(toZoneId).format(toFormatter);
    }*/

    //////////////////////////////////////////

    public static XMLGregorianCalendar getISONormalisedDateTime() {
        return getISO(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }

    public static XMLGregorianCalendar getISODateTime() {
        return getISO(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static XMLGregorianCalendar getISODateTimeWithMillis() {
        return getISO(DateTimeFormatter.ofPattern(localISODateTimePattern));
    }

    private static XMLGregorianCalendar getISO(DateTimeFormatter formatter) {
        try {
            LocalDateTime currentUTCTime = LocalDateTime.now();
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(currentUTCTime.format(formatter));
            return xmlCalendar;
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
            return null;
        }
    }

    public static String getCurrentDate_YYYYMMDD() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDateTime.now().format(formatter);
    }


    public static String getCurrentDate_YYYY_MM_DD() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(formatter);
    }

    public static String getCurrentDate_YYMMDD() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return LocalDateTime.now().format(formatter);
    }

    public static String getCurrentDateTime_YYYYMMDDHHmmss() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }


    public static Long convertDateTimeToMillis(String xgc) {
        return Long.valueOf(DatatypeConverter.parseDateTime(xgc).getTimeInMillis());
    }

    public static Date convertStringToDate(String dateTime, String format) {
        if(StringUtils.isEmpty(format)){
            format = defaultDateTimePattern;
        }
        try {
            DateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            return sdf.parse(dateTime);
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
            return null;
        }
    }

    public static String convertDateToString(Date dateTime, String format) {
        try {
            if(StringUtils.isEmpty(format)){
                format = defaultDateTimePattern;
            }
            DateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            return sdf.format(dateTime);
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
            return null;
        }
    }



    public static String utcDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_ZONE));
        calendar.setTime(new Date());
        return DatatypeConverter.printDate(calendar);
    }

    public static String localDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return DatatypeConverter.printDateTime(calendar);
    }

    public static String localDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return DatatypeConverter.printDate(calendar);
    }


    public static Date toDateTime(String str) {
        return DatatypeConverter.parseDateTime(str).getTime();
    }

    public static Date toDate(String str) {
        return DatatypeConverter.parseDate(str).getTime();
    }

    public static String toString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DatatypeConverter.printDateTime(calendar);
    }

    public static String toDateString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DatatypeConverter.printDate(calendar);
    }

    public static String getCurrentTime_YYYYMMDDHHmmss(String timeZone) {
        return ZonedDateTime.now(ZoneId.of(timeZone)).format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"));
    }

    public static String getCurrentTime_YYYYMMDD(String timeZone) {
        return ZonedDateTime.now(ZoneId.of(timeZone)).format(DateTimeFormatter.ofPattern("YYYYMMdd"));
    }


    /**/

    public static String  convertFromUTCToSGLocal(String utcTime){
        LocalDateTime dateTime = LocalDateTime.parse(utcTime, DateTimeFormatter.ofPattern(utcDateTimePattern));
        ZonedDateTime zonedDateTime = dateTime.atZone(SG_ZONE);
        return zonedDateTime.withZoneSameInstant(SG_ZONE).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }


    public static String convertTimeToUTCFromSGLocal(String localTime){
        LocalDateTime dateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern(localISODateTimePattern));
        ZonedDateTime zonedDateTime = dateTime.atZone(UTC_ZONE);
        return zonedDateTime.withZoneSameInstant(UTC_ZONE).format(DateTimeFormatter.ofPattern(utcDateTimePattern));
    }


    public static String utcDateTimeWithMillis() {
        final String dateFormat = utcDateTimePattern;
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone(UTC_ZONE));
        return sdf.format(new Date());
    }

    public static String localDateTimeWithMillis() {
        final String dateFormat = localISODateTimePattern;
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone(SG_ZONE));
        return sdf.format(new Date());
    }

    public static String localNormalizedDateTimeWithMillis() {
        final String dateFormat = localISONormalizedDateTimePattern;
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone(SG_ZONE));
        return sdf.format(new Date());
    }

    public static Long getCurrentUTCDateTimeInMillis() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_ZONE));
        calendar.setTime(new Date());
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static Long getCurrentLocalDateTimeInMillis() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(SG_ZONE));
        calendar.setTime(new Date());
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static String getDatebyZoneNLong( Long longVal,  ZoneId zone ) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zone));
        cal.setTimeInMillis(longVal);
        String dateTimeString = DatatypeConverter.printDateTime(cal);
        return dateTimeString;
    }

    public static Date addMinutes(Date date, int minutes){
         //TODO if date is empty
         Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private static Calendar getCalendar(Date date){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static long getCurrentTimeDifferenceInMinutes(String date, int addMinutes){
        return  ChronoUnit.MINUTES.between(addMinutes(date, addMinutes), LocalDateTime.now());
    }

    public static String convertDateToString(LocalDateTime dateTime, String format) {
        try {
            if(StringUtils.isEmpty(format)){
                format = defaultDateTimePattern;
            }
            DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern(format);
            return dateTimeFormatter.format(dateTime);
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
            return null;
        }
    }

    public static LocalDateTime addMinutes(String date,int addMinutes){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultDateTimePattern);
        LocalDateTime  dateTime = LocalDateTime.parse(date, formatter);
        return dateTime.plusMinutes(addMinutes);

    }


}

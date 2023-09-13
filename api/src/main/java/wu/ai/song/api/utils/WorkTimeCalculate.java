package wu.ai.song.api.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author 75318070
 */
public class WorkTimeCalculate {
    /**
     * 上班时间
     */
    private static final LocalTime WORKING_START_TIME = LocalTime.of(8, 30);
    /**
     * 下班时间
     */
    private static final LocalTime WORKING_END_TIME = LocalTime.of(17, 30);
    /**
     * 午休开始时间
     */
    private static final LocalTime NOON_BREAK_START_TIME = LocalTime.of(12, 0);
    /**
     * 午休结束时间
     */
    private static final LocalTime NOON_BREAK_END_TIME = LocalTime.of(13, 0);

    private static void getWorkTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.compareTo(endDateTime) > 0) {
            throw new RuntimeException("参数错误");
        }
        int diff = 0;

        while (true) {
            // TODO: 调休日期处理
            // 跳过周六周日
            if (startDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                startDateTime = LocalDateTime.of(startDateTime.toLocalDate(), WORKING_START_TIME).plusDays(2);
            }
            if (startDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                startDateTime = LocalDateTime.of(startDateTime.toLocalDate(), WORKING_START_TIME).plusDays(1);
            }
            if (endDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                endDateTime = LocalDateTime.of(endDateTime.toLocalDate(), WORKING_START_TIME).plusDays(2);
            }
            if (endDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                endDateTime = LocalDateTime.of(endDateTime.toLocalDate(), WORKING_START_TIME).plusDays(1);
            }
            // 跨天处理
            if (startDateTime.getDayOfYear() == endDateTime.getDayOfYear()) {
                int diffSecond = getDiffSecond(startDateTime, endDateTime);
                diff = diffSecond + diff;
                break;
            }
            int diffSecond = getDiffSecond(startDateTime, LocalDateTime.of(startDateTime.toLocalDate(), WORKING_END_TIME));
            diff = diffSecond + diff;
            startDateTime = LocalDateTime.of(startDateTime.toLocalDate(), WORKING_START_TIME).plusDays(1);
        }
        System.out.println("DIFF(min): " + Double.valueOf(diff) / 3600);
    }

    private static int getDiffSecond(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();
        // diff单位：秒
        int diff = 0;

        // 开始时间切移
        if (startTime.isBefore(WORKING_START_TIME)) {
            startTime = WORKING_START_TIME;
        } else if (startTime.isAfter(NOON_BREAK_START_TIME) && startTime.isBefore(NOON_BREAK_END_TIME)) {
            startTime = NOON_BREAK_START_TIME;
        } else if (startTime.isAfter(WORKING_END_TIME)) {
            startTime = WORKING_END_TIME;
        }
        // 结束时间切移
        if (endTime.isBefore(WORKING_START_TIME)) {
            endTime = WORKING_START_TIME;
        } else if (endTime.isAfter(NOON_BREAK_START_TIME) && endTime.isBefore(NOON_BREAK_END_TIME)) {
            endTime = NOON_BREAK_START_TIME;
        } else if (endTime.isAfter(WORKING_END_TIME)) {
            endTime = WORKING_END_TIME;
        }
        // 午休时间判断处理
        if (startTime.compareTo(NOON_BREAK_START_TIME) <= 0 && endTime.compareTo(NOON_BREAK_END_TIME) >= 0) {
            diff = diff + 60 * 60;
        }
        diff = endTime.toSecondOfDay() - startTime.toSecondOfDay() - diff;

        return diff;
    }

    public static void main(String[] args) {
        WorkTimeCalculate.getWorkTime(LocalDateTime.of(2022, 4, 28, 20, 30, 0),
                LocalDateTime.of(2022, 4, 29, 20, 30, 0));
    }
}

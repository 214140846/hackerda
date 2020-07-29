package com.hackerda.platform.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * @author junrong.chen
 * @date 2018/10/31
 */
public class SchoolTimeUtil {
	/**
	 * 开学日期
	 */
	private static final DateTime TERM_START = new DateTime(2019, 8, 26, 0, 0);

	/**
	 * 教学周
	 */
	public static int getSchoolWeek(){
		return (int) new Duration(TERM_START, new DateTime()).getStandardDays() / 7 + 1;
	}

	/**
	 * 星期几
	 */
	public static int getDayOfWeek(){
		return new DateTime().getDayOfWeek();
	}

	public static String getDayOfWeekChinese() {
		return getDayOfWeekChinese(getDayOfWeek());
	}

    public static String getDayOfWeekChinese(int day) {
        switch (day){
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                throw new IllegalArgumentException("never happen");
        }
    }



    /**
     * 根据教学周和星期几算出今天是第几天
     * @param schoolWeek 教学周
     * @param dayOfWeek 星期几
     * @return 可操作时间对象
     */
	public static DateTime getDateBySchoolTime(int schoolWeek, int dayOfWeek){

        return TERM_START.plusDays((schoolWeek - 1) * 7 + dayOfWeek);
    }

	public static void main(String[] args) {
		System.out.println(getDateBySchoolTime(1, 3));
	}
}

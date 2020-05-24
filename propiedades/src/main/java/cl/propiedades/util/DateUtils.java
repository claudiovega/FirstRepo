package cl.propiedades.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;



public class DateUtils {

	public DateUtils() {
		// TODO Auto-generated constructor stub
	}
	public static String dateToString(Date date, String mask){
		String result = "";
		if (date != null && (mask != null && !mask.trim().isEmpty())){
			SimpleDateFormat sdf = new SimpleDateFormat(mask);
			result = sdf.format(date);
		}
		return result;
	}
	public static Date stringToDate(String date, String mask){
		SimpleDateFormat formatter = new SimpleDateFormat(mask);
		Date result = null;

        try {

             result = formatter.parse(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
	}
	public static LocalDate stringToLocalDate(String date, String mask){

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mask);

		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate;
	}
	public static java.sql.Date stringToSqlDate(String date, String mask){
		java.sql.Date result = null;
		if (date != null && !date.trim().isEmpty()){
			try {
				SimpleDateFormat format = new SimpleDateFormat(mask);
				Date parsed = format.parse(date);
				result = new java.sql.Date(parsed.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = null;
			}
		}
		
        return result;
	}
	public static boolean isCorrectMaskForDate(String date, String mask){
		SimpleDateFormat formatter = new SimpleDateFormat(mask);
		boolean result = false;

        try {

             formatter.parse(date);
             result = true;

        } catch (ParseException e) {
        	result = false;
            //e.printStackTrace();
        }
        return result;
	}
	public static long getSecondsBetweenDates(Date endDate, Date initDate){
		long seconds = (endDate.getTime()-initDate.getTime())/1000;
		return seconds;
	}
}

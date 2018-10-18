import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
	
	public static String convertCalendarToString(Calendar birthdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = null;
		if (birthdate != null) {
			dateString = sdf.format(birthdate.getTime());
		}
		return dateString;
	}
	
	public static Calendar findBirthDate(String text) {
		Calendar calendar = Calendar.getInstance();
		// nn/nn/nnnn
		Pattern pattern = Pattern.compile("([0-9]?[0-9](\\/|\\.)[0-9]?[0-9](\\/|\\.)[0-9]?[0-9]?[0-9][0-9])");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			calendar = convertNumericBirthDateToCalendar(matcher.group(1));
		}
		else {
			// e.g 30 de agosto de 1947
			pattern = Pattern.compile("([0-9]?[0-9] de [a-zç]* de [0-9]?[0-9]?[0-9][0-9])");
			matcher = pattern.matcher(text);
			if (matcher.find()) {
				calendar = convertTextualBirthDateToCalendar(matcher.group(1));
			}
			else {
				return null;
			}
		}
		return calendar;
	}
	
	public static Calendar convertTextualBirthDateToCalendar(String birthdate) {
		// creates array with date parts, like [9,de,janeiro,de,1957]
		Calendar calendar = Calendar.getInstance();
		String[] dateParts = birthdate.split("\\s+");
		if (dateParts != null && dateParts.length >= 5) {
			int day = Integer.parseInt(dateParts[0]);
			int month = convertMonthFromStringToInt(dateParts[2]);
			int year = append19ToYear(dateParts[4]);
			calendar.set(year, month, day);
			return calendar;
		}
		return null;
	}
	
	private static int append19ToYear (String yearString) {
		int year = 0;
		if (yearString.length() == 2) {
			String fullYearString = "19" + yearString;
			year = Integer.parseInt(fullYearString);
		}
		else {
			year = Integer.parseInt(yearString);
		}
		return year;
	}
	
	private static int convertMonthFromStringToInt(String month) {
		int monthInt = 0;
		switch(month.toLowerCase()) {
			case "janeiro":
				monthInt = Calendar.JANUARY;
				break;
			case "fevereiro":
				monthInt = Calendar.FEBRUARY;
				break;
			case "março":
				monthInt = Calendar.MARCH;
				break;
			case "marco":
				monthInt = Calendar.MARCH;
				break;
			case "abril":
				monthInt = Calendar.APRIL;
				break;
			case "maio":
				monthInt = Calendar.MAY;
				break;
			case "junho":
				monthInt = Calendar.JUNE;
				break;
			case "julho":
				monthInt = Calendar.JULY;
				break;
			case "agosto":
				monthInt = Calendar.AUGUST;
				break;
			case "setembro":
				monthInt = Calendar.SEPTEMBER;
				break;
			case "outubro":
				monthInt = Calendar.OCTOBER;
				break;
			case "novembro":
				monthInt = Calendar.NOVEMBER;
				break;
			case "dezembro":
				monthInt = Calendar.DECEMBER;
				break;
		}
		return monthInt;
	}
	
	public static Calendar convertNumericBirthDateToCalendar(String birthdate) {
		Calendar calendar = Calendar.getInstance();
		int month = 0;
		int day = 0;
		int year = 0;
		// splits birthdate by /
		String[] dateParts = birthdate.split("\\/");
		if (dateParts != null && dateParts.length >= 3) {
			day = Integer.parseInt(dateParts[0]);
			// month starts at 0
			month = Integer.parseInt(dateParts[1]) - 1;
			year = append19ToYear(dateParts[2]);
		}
		// splits birthdate by .
		else {
			dateParts = birthdate.split("\\.");
			if (dateParts != null && dateParts.length >= 3) {
				day = Integer.parseInt(dateParts[0]);
				// month starts at 0
				month = Integer.parseInt(dateParts[1]) - 1;
				year = append19ToYear(dateParts[2]);
			}
			// split was not successful
			else {
				return null;
			}
		}
		calendar.set(year, month, day);
		return calendar;
	}
	

}

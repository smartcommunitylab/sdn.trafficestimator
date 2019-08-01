package it.smartcommunitylab.trafficestimator.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtility {

	public DateFormatUtility() {
		// TODO Auto-generated constructor stub
	}
	
	public static Timestamp convertStringtoTimeStamp(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	    Date parsedDate = dateFormat.parse(date);
	    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
	    
	    return timestamp;
	}
	
	public static Timestamp getCurrentTime() {
		Date date= new Date();
		  long time = date.getTime();
		  Timestamp ts = new Timestamp(time);
		  return ts;
	}

}

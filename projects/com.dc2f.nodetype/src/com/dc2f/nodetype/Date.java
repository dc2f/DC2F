package com.dc2f.nodetype;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;

public class Date extends BaseNodeType {

	public Calendar getCalendar(Node node) {
		TimeZone timeZone = TimeZone.getDefault();

		Integer offset = (Integer) node.get("offset");
		String[] timeZones = TimeZone.getAvailableIDs(offset);
		if (timeZones.length > 0) {
			TimeZone.getTimeZone(timeZones[0]);
		}

		Long milliseconds = (Long) node.get("timestamp");
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.setTimeInMillis(milliseconds);

		return calendar;
	}

	public java.util.Date getDate(Node node) {
		return new java.util.Date(((Integer)node.get("timestamp")).intValue() * 1000L);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + " xyz";
	}

}

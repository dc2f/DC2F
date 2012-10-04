package com.dc2f.nodetype;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;

public class Date extends BaseNodeType {

	public Calendar getCalendar(Node node) {
		Long milliseconds = (Long) node.get("timestamp");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(milliseconds);

		return calendar;
	}

	public java.util.Date getDate(Node node) {
		if (node.get("timestamp") == null) {
			return null;
		}
		return new java.util.Date(((Integer)node.get("timestamp")).intValue() * 1000L);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + " xyz";
	}

}

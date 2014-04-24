package org.ezuce.archive.model;

import java.util.Calendar;
import java.util.Date;

public class DateRange {

	private long start;
	private long end;
	private Date endDate;
	private Date startDate;

	public DateRange() {
		Calendar today = Calendar.getInstance();
		setEnd(today.getTimeInMillis());
		today.add(Calendar.DATE, -1);
		setStart(today.getTimeInMillis());
	}

	public DateRange(long start, long end) {
		setStart(start);
		setEnd(end);
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
		this.startDate = new Date(start);
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
		this.endDate = new Date(end);
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public boolean isInRange(Date date) {
		return date.equals(getStartDate()) || date.equals(getEndDate())
				|| (date.after(getStartDate()) && date.before(getEndDate()));
	}

	@Override
	public String toString() {
		return "DateRange: from=" + startDate + " to=" + endDate;
	}

	public static DateRange getTomorrow() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, 1);
		long start = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		long end = calendar.getTimeInMillis();

		return new DateRange(start, end);
	}

	public static DateRange getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long start = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		long end = calendar.getTimeInMillis();

		return new DateRange(start, end);
	}

	public static DateRange getPrevMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		long end = calendar.getTimeInMillis();

		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long start = calendar.getTimeInMillis();

		return new DateRange(start, end);
	}

	public static DateRange getLast20Years() {
		Calendar calendar = Calendar.getInstance();
		long end = calendar.getTimeInMillis();

		calendar.add(Calendar.YEAR, -20);
		long start = calendar.getTimeInMillis();
		return new DateRange(start, end);
	}

	public DateRange copy() {
		DateRange copy = new DateRange();
		copy.setStart(getStart());
		copy.setEnd(getEnd());
		return copy;
	}
}

package de.bcxp.challenge.mapping;

import java.util.Objects;

import com.opencsv.bean.CsvBindByName;


public class MockBean {
	
	@CsvBindByName(column = "Day")
	private int dayOfTheMonth;
	
	@CsvBindByName(column = "Month")
	private String month;
    
	@CsvBindByName(column = "Temp")
	private float temp;
	

	public MockBean(String month, int dayOfTheMonth,  float temp) {
		
		this.dayOfTheMonth = dayOfTheMonth;
		this.month = month;
		this.temp = temp;
	}
	
	public MockBean() {
		this.dayOfTheMonth = -1;
		this.month = "";
		this.temp = -1;
	}

	public int getDayOfTheMonth() {
		return dayOfTheMonth;
	}

	public void setDayOfTheMonth(int dayOfTheMonth) {
		this.dayOfTheMonth = dayOfTheMonth;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	@Override
	public String toString() {
		return "MockBean [dayOfTheMonth=" + dayOfTheMonth + ", month=" + month + ", temp=" + temp + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dayOfTheMonth, month, temp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MockBean other = (MockBean) obj;
		return dayOfTheMonth == other.dayOfTheMonth && Objects.equals(month, other.month)
				&& Float.floatToIntBits(temp) == Float.floatToIntBits(other.temp);
	}
	
	

}

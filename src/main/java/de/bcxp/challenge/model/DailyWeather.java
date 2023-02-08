package de.bcxp.challenge.model;

import java.util.Objects;

/**
 * DailyWeather model follows JavaBeans conventions, and defines fields and methods to save daily weather data.
 * @author catherine heyart
 *
 */
public class DailyWeather {
	
	private int dayOfTheMonth;
	
	private int minTempInFahrenheit;
	
	private int maxTempInFahrenheit;
	
	private int tempDiffInFahrenheit;
	
	private final static int DEFAULT_VALUE = -1;
	
	public DailyWeather() {
		dayOfTheMonth = DEFAULT_VALUE;
		minTempInFahrenheit = DEFAULT_VALUE;
		maxTempInFahrenheit = DEFAULT_VALUE;
		tempDiffInFahrenheit = DEFAULT_VALUE;
	}

	public DailyWeather(int dayOfTheMonth, int minTemp, int maxTemp) {
		this.dayOfTheMonth = dayOfTheMonth;
		this.minTempInFahrenheit = minTemp;
		this.maxTempInFahrenheit = maxTemp;
	}

	public int getDayOfTheMonth() {
		return dayOfTheMonth;
	}

	public void setDayOfTheMonth(int dayOfTheMonth) {
		this.dayOfTheMonth = dayOfTheMonth;
	}

	public int getMinTempInFahrenheit() {
		return minTempInFahrenheit;
	}

	public void setMinTempInFahrenheit(int minTempInFahrenheit) {		
		this.minTempInFahrenheit = minTempInFahrenheit;
		calculateTempDiffInFahrenheit();
	}

	public int getMaxTempInFahrenheit() {
		return maxTempInFahrenheit;
	}

	public void setMaxTempInFahrenheit(int maxTempInFahrenheit) {
		this.maxTempInFahrenheit = maxTempInFahrenheit;
		calculateTempDiffInFahrenheit();
	}
	
	public int getTempDiffInFahrenheit() {
		return tempDiffInFahrenheit;
	}

	private void calculateTempDiffInFahrenheit() {
		if(minTempInFahrenheit == DEFAULT_VALUE || maxTempInFahrenheit == DEFAULT_VALUE) {
			tempDiffInFahrenheit = DEFAULT_VALUE;
			return;
		} 
		
		tempDiffInFahrenheit = maxTempInFahrenheit - minTempInFahrenheit;
	}

	@Override
	public String toString() {
		return "DailyWeather [dayOfTheMonth=" + dayOfTheMonth + ", minTempInFahrenheit=" + minTempInFahrenheit
				+ ", maxTempInFahrenheit=" + maxTempInFahrenheit + ", tempDiffInFahrenheit=" + tempDiffInFahrenheit
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dayOfTheMonth, maxTempInFahrenheit, minTempInFahrenheit, tempDiffInFahrenheit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DailyWeather other = (DailyWeather) obj;
		return dayOfTheMonth == other.dayOfTheMonth && maxTempInFahrenheit == other.maxTempInFahrenheit
				&& minTempInFahrenheit == other.minTempInFahrenheit
				&& tempDiffInFahrenheit == other.tempDiffInFahrenheit;
	}
	
	
	
	

}

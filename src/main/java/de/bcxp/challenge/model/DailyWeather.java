package de.bcxp.challenge.model;

import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

/**
 * DailyWeather model defines fields and methods to save daily weather data.
 * This class follows JavaBeans conventions and contains annotations from the OpenCSV library, so that it can be mapped from csv files.
 * @author catherine heyart
 *
 */
public class DailyWeather {
	
	/**
	 * Day of the month.
	 */
	@CsvBindByName(column = "Day")
	private int dayOfTheMonth;
	
	/**
	 * Minimum temperature measured on this day (in Fahrenheit):
	 */
	@CsvBindByName(column = "MnT")
	private int minTempInF;
	
	/**
	 * Maximum temperature measured on this day (in Fahrenheit):
	 */
	@CsvBindByName(column = "MxT")
	private int maxTempInF;
	
	/**
	 * Temperature difference on this day (in Fahrenheit):
	 */
	private int tempDiffInF;
	
	/**
	 * Default value assigned to all int attributes which cannot hold negative numbers.
	 */
	private final static int DEFAULT_VALUE = -1;
	
	public DailyWeather() {
		dayOfTheMonth = DEFAULT_VALUE;
		minTempInF = DEFAULT_VALUE;
		maxTempInF = DEFAULT_VALUE;
		tempDiffInF = DEFAULT_VALUE;
	}

	public DailyWeather(int dayOfTheMonth, int minTemp, int maxTemp) {
		this.dayOfTheMonth = dayOfTheMonth;
		this.minTempInF = minTemp;
		this.maxTempInF = maxTemp;
		setTempDiffInF();
	}

	public int getDayOfTheMonth() {
		return dayOfTheMonth;
	}

	public void setDayOfTheMonth(int dayOfTheMonth) {
		this.dayOfTheMonth = dayOfTheMonth;
	}

	public int getMinTempInF() {
		return minTempInF;
	}

	public void setMinTempInF(int minTempInFahrenheit) {		
		this.minTempInF = minTempInFahrenheit;
		setTempDiffInF();
	}

	public int getMaxTempInF() {
		return maxTempInF;
	}

	public void setMaxTempInF(int maxTempInFahrenheit) {
		this.maxTempInF = maxTempInFahrenheit;
		setTempDiffInF();
	}
	
	public int getTempDiffInF() {
		return tempDiffInF;
	}

	/**
	 * This methods calculates and sets the temperature difference, based on the minimum and maximum temperature. 
	 * Sets the the tempDifference to the default value if minTemp or maxTemp don't have valid values.
	 */
	private void setTempDiffInF() {
		if(minTempInF == DEFAULT_VALUE || maxTempInF == DEFAULT_VALUE || maxTempInF < minTempInF) {
			tempDiffInF = DEFAULT_VALUE;
			return;
		} 
		
		tempDiffInF = maxTempInF - minTempInF;
	}

	@Override
	public String toString() {
		return "DailyWeather [dayOfTheMonth=" + dayOfTheMonth + ", minTempInF=" + minTempInF
				+ ", maxTempInF=" + maxTempInF + ", tempDiffInF=" + tempDiffInF
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dayOfTheMonth, maxTempInF, minTempInF, tempDiffInF);
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
		return dayOfTheMonth == other.dayOfTheMonth && maxTempInF == other.maxTempInF
				&& minTempInF == other.minTempInF
				&& tempDiffInF == other.tempDiffInF;
	}
	
}

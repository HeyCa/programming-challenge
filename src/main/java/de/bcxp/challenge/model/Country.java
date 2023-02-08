package de.bcxp.challenge.model;

import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

/**
 * Model class which holds information of a specific country
 * @author catherine heyart
 *
 */
public class Country {
	
	/**
	 * name of the country.
	 */
	@CsvBindByName(column = "Name")
	private String name;
	
	/**
	 * population of the country.
	 */
	@CsvBindByName(column = "Population")
	private int population;
	
	/**
	 * area of the country in square km.
	 */
	@CsvBindByName(column = "Area (km²)")
	private int area;
	
	/**
	 * population density of the country
	 */
	private int populationDensity;
	
	private static final int DEFAULT_VALUE = -1;

	public Country(String name, int population, int area) {
		this.name = name;
		this.population = population;
		this.area = area;
		calculatePopulationDensity();
	}

	public Country() {
		name = "";
		population = DEFAULT_VALUE;
		area = DEFAULT_VALUE;
		populationDensity = DEFAULT_VALUE;	
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
		calculatePopulationDensity();
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
		calculatePopulationDensity();
	}

	public int getPopulationDensity() {
		return populationDensity;
	}


	private void calculatePopulationDensity() {
		if(population == DEFAULT_VALUE || area == DEFAULT_VALUE || area == 0) {
			populationDensity = DEFAULT_VALUE;
			return;
		}	
		populationDensity = population/area;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", population=" + population + ", area=" + area + ", populationDensity="
				+ populationDensity + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(area, name, population, populationDensity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		return area == other.area && Objects.equals(name, other.name) && population == other.population
				&& populationDensity == other.populationDensity;
	}
	

}

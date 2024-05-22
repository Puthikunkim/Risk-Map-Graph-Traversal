package nz.ac.auckland.se281;

/**
 * Class to represent a country with its name, continent and tax rate
 */
public class Country {
  private String name;
  private String continent;
  private int tax;

  /**
   * Constructor to create a country object
   * 
   * @param name      Name of the country
   * @param continent Continent of the country
   * @param tax       Tax rate of the country
   */
  public Country(String name, String continent, int tax) {
    this.name = name;
    this.continent = continent;
    this.tax = tax;
  }

  public String getName() {
    return name;
  }

  public String getContinent() {
    return continent;
  }

  public int getTax() {
    return tax;
  }

  @Override
  public int hashCode() {
    // Method to generate the hashcode for the object
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((continent == null) ? 0 : continent.hashCode());
    result = prime * result + tax;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    // Method to check if two objects are equal based on hashcode
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Country other = (Country) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (continent == null) {
      if (other.continent != null) {
        return false;
      }
    } else if (!continent.equals(other.continent)) {
      return false;
    }
    if (tax != other.tax) {
      return false;
    }
    return true;
  }
}

package nz.ac.auckland.se281;

/** Class to represent a country with its name, continent and tax rate. */
public class Country {
  private String name;
  private String continent;
  private int tax;

  /**
   * Constructor to create a country object.
   *
   * @param name Name of the country
   * @param continent Continent of the country
   * @param tax Tax rate of the country
   * @return Country object
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
    if (this == obj) { // Check if the objects are the same
      return true;
    }
    if (obj == null) { // Check if the object is null
      return false;
    }
    if (getClass() != obj.getClass()) { // Check if the objects are of the same class
      return false;
    }
    Country other = (Country) obj; // Cast the object to a Country object
    if (name == null) { // Check if the name is null and compare it with the other object name
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) { // Compare the names of the two objects
      return false;
    }
    if (continent
        == null) { // Check if the continent is null and compare it with the other object continent
      if (other.continent != null) {
        return false;
      }
    } else if (!continent.equals(other.continent)) { // Compare the continents of the two objects
      return false;
    }
    if (tax != other.tax) { // Compare the tax rates of the two objects
      return false;
    }
    return true;
  }
}

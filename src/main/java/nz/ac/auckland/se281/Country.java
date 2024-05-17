package nz.ac.auckland.se281;

public class Country {
  private String name;
  private String continent;

  public Country(String name, String continent) {
    this.name = name;
    this.continent = continent;
  }

  public String getName() {
    return name;
  }

  public String getContinent() {
    return continent;
  }
}

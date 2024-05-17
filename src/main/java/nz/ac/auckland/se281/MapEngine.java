package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {
  public Map<Country, List<Country>> adjCountries;

  public MapEngine() {
    this.adjCountries = new HashMap<>();
    loadMap(); // keep this mehtod invocation
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();

    // Parse Countries and add them to the map.
    for (String line : countries) {
      String[] parts = line.split(",");
      Country country = new Country(parts[0], parts[1], Integer.parseInt(parts[2]));
      addCountry(country);
    }

    // Parse adjacencies and add them to the map for each country.
    for (String line : adjacencies) {
      String[] parts = line.split(",");
      String countryName = parts[0]; // the first part is the country name
      Country country = findCountryByName(countryName); // stores country of interest

      if (country != null) {
        List<Country> neighbors = adjCountries.get(country); // stores neighbors of the country
        for (int i = 1;
            i < parts.length;
            i++) { // iterate over the rest of the parts which is the neighbors
          String neighborName = parts[i]; // stores the neighbor name
          Country neighbor = findCountryByName(neighborName); // stores the neighbor country
          if (neighbor != null) {
            neighbors.add(neighbor); // add the neighbor to the neighbors list
          }
        }
      }
    }
  }

  public void printAdjacencies() {
    for (Map.Entry<Country, List<Country>> entry : adjCountries.entrySet()) {
      Country country = entry.getKey();
      List<Country> neighbors = entry.getValue();
      System.out.print("Country: " + country.getName() + " | Adjacencies: ");
      for (Country neighbor : neighbors) {
        System.out.print(neighbor.getName() + " ");
      }
      System.out.println();
    }
  }

  public void addCountry(Country country) {
    adjCountries.putIfAbsent(country, new ArrayList<>());
  }

  public Country findCountryByName(String name) {
    for (Country country : adjCountries.keySet()) {
      if (country.getName().equals(name)) {
        return country;
      }
    }
    return null;
  }

  public void addBorder(Country country1, Country country2) {
    adjCountries.get(country1).add(country2);
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    // Ask for name of country from messagecli file
    String insertCountryMessage = MessageCli.INSERT_COUNTRY.getMessage();
    System.out.print(insertCountryMessage);
    // Read the country name from the user
    String countryName = Utils.scanner.nextLine();
    boolean validCountry = false;
    while (!validCountry) {
      try {
        checkInput(countryName);
        validCountry = true;
      } catch (MyCoolException e) {
        countryName = Utils.scanner.nextLine();
      }
    }
  }

  public class MyCoolException extends Exception {
    public MyCoolException(String message) {
      super(message);
    }
  }

  public void checkInput(String countryName) throws MyCoolException {
    Country country = findCountryByName(countryName);
    if (country == null) {
      String invalidCountryMessage = MessageCli.INVALID_COUNTRY.getMessage(countryName);
      System.out.println(invalidCountryMessage);
    } else {
      String countryInfoMessage =
          MessageCli.COUNTRY_INFO.getMessage(
              country.getName(), country.getContinent(), String.valueOf(country.getTax()));
      System.out.println(countryInfoMessage);
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}

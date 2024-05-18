package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
    boolean validCountry = false;
    while (!validCountry) {
      String countryName = Utils.scanner.nextLine();
      countryName = Utils.capitalizeFirstLetterOfEachWord(countryName);
      try {
        Country countryFound = checkInput(countryName);
        String countryInfoMessage =
            MessageCli.COUNTRY_INFO.getMessage(
                countryFound.getName(),
                countryFound.getContinent(),
                String.valueOf(countryFound.getTax()));
        System.out.println(countryInfoMessage);
        validCountry = true;
      } catch (MyCoolException e) {
        String invalidCountryMessage = MessageCli.INVALID_COUNTRY.getMessage(countryName);
        System.out.println(invalidCountryMessage);
      }
    }
  }

  public class MyCoolException extends Exception {
    public MyCoolException() {
      super();
    }
  }

  public Country checkInput(String countryName) throws MyCoolException {
    Country country = findCountryByName(countryName);
    if (country == null) {
      throw new MyCoolException();
    } else {
      return country;
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {
    String insertSourceMessage = MessageCli.INSERT_SOURCE.getMessage();
    System.out.print(insertSourceMessage);
    // Read the country name from the user
    boolean validSourceCountry = false;
    Country sourceCountryFound = null;
    while (!validSourceCountry) {
      String sourceCountryName = Utils.scanner.nextLine();
      sourceCountryName = Utils.capitalizeFirstLetterOfEachWord(sourceCountryName);
      try {
        sourceCountryFound = checkInput(sourceCountryName);
        validSourceCountry = true;
      } catch (MyCoolException e) {
        String invalidCountryMessage = MessageCli.INVALID_COUNTRY.getMessage(sourceCountryName);
        System.out.println(invalidCountryMessage);
      }
    }
    // Ask for name of destination country from messagecli file
    String insertDestinationMessage = MessageCli.INSERT_DESTINATION.getMessage();
    System.out.print(insertDestinationMessage);
    // Read the country name from the user
    boolean validDestinationCountry = false;
    Country destinationCountryFound = null;
    while (!validDestinationCountry) {
      String destinationCountryName = Utils.scanner.nextLine();
      destinationCountryName = Utils.capitalizeFirstLetterOfEachWord(destinationCountryName);
      try {
        destinationCountryFound = checkInput(destinationCountryName);
        validDestinationCountry = true;
      } catch (MyCoolException e) {
        String invalidCountryMessage =
            MessageCli.INVALID_COUNTRY.getMessage(destinationCountryName);
        System.out.println(invalidCountryMessage);
      }
    }

    // Find the shortest path between the source and destination countries
    List<Country> path = mapTraversal(sourceCountryFound, destinationCountryFound);
    if (path.isEmpty()) {
      System.out.println(MessageCli.NO_CROSSBORDER_TRAVEL.getMessage());
      return;
    } else {
      List<String> continentsVisisted = new LinkedList<>();

      // String builder for the route in the format [country1, country2, ...]
      StringBuilder routeInfoMessage = new StringBuilder("[");
      for (Country country : path) {
        routeInfoMessage.append(country.getName()).append(", ");
        if (!continentsVisisted.contains(country.getContinent())) {
          continentsVisisted.add(country.getContinent());
        }
      }
      routeInfoMessage.delete(routeInfoMessage.length() - 2, routeInfoMessage.length());
      routeInfoMessage.append("]");
      String finalRouteInfoMessage = routeInfoMessage.toString();

      // String builder for continents in the format [continent1, continent2, ...]
      StringBuilder continentInfoMessage = new StringBuilder("[");
      for (String continent : continentsVisisted) {
        continentInfoMessage.append(continent).append(", ");
      }
      continentInfoMessage.delete(continentInfoMessage.length() - 2, continentInfoMessage.length());
      continentInfoMessage.append("]");
      String finalContinentInfoMessage = continentInfoMessage.toString();

      // Calculate taxes for the route
      int taxes = 0;
      for (int i = 1; i < path.size(); i++) {
        Country currentCountry = path.get(i);
        taxes += currentCountry.getTax();
      }

      // Print the route, continents, and taxes
      System.out.println(MessageCli.ROUTE_INFO.getMessage(finalRouteInfoMessage));
      System.out.println(MessageCli.CONTINENT_INFO.getMessage(finalContinentInfoMessage));
      System.out.println(MessageCli.TAX_INFO.getMessage(String.valueOf(taxes)));
    }
  }

  // Find the shortest path between two countries using BFS
  public List<Country> mapTraversal(Country sourceCountry, Country destinationCountry) {
    Set<Country> visited = new HashSet<>(); // Set to keep track of visited countries
    Queue<Country> queue = new LinkedList<>(); // Queue for BFS
    Map<Country, Country> predecessorMap =
        new HashMap<>(); // Map to store the predecessors of each country in the path
    // Check if the source and destination countries are the same
    if (sourceCountry.equals(destinationCountry)) {
      return new ArrayList<>();
    }
    // Start BFS from the start country
    queue.add(sourceCountry);
    visited.add(sourceCountry);

    // BFS loop until the queue is empty
    while (!queue.isEmpty()) {
      Country currentCountry = queue.poll(); // Get the current country to do BFS on
      // If we reach the end country, reconstruct the path
      if (currentCountry.equals(destinationCountry)) {
        return reconstructRoute(predecessorMap, sourceCountry, destinationCountry);
      }

      // Visit all adjacent countries of the current country
      for (Country adjacentCountry : adjCountries.get(currentCountry)) {
        if (!visited.contains(adjacentCountry)) { // If the country is not visited
          visited.add(adjacentCountry); // Mark the country as visited
          predecessorMap.put(
              adjacentCountry,
              currentCountry); // Store the predecessor of the country which is the current country
          queue.add(
              adjacentCountry); // Add the country to the queue to visit its neighbors in the next
          // iteration of the loop
        }
      }
    }
    // Return an empty list if no path is found or the source and destination countries are the same
    return new ArrayList<>();
  }

  // Reconstruct the path from start to end using the predecessory map
  private List<Country> reconstructRoute(
      Map<Country, Country> predecessorMap, Country sourceCountry, Country destinationCountry) {
    List<Country> route = new ArrayList<>();
    for (Country i = destinationCountry; i != null; i = predecessorMap.get(i)) {
      route.add(i);
    }
    Collections.reverse(route);
    return route;
  }
}

package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {
  private Map<Country, List<Country>> adjCountries;

  public MapEngine() {
    this.adjCountries = new HashMap<>();
    loadMap(); // keep this mehtod invocation
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();
  }

  public void addCountry(Country country) {
    adjCountries.putIfAbsent(
        country, new ArrayList<>()); // DO I NEED PUTIFABSENT? ##############???????????????????????
  }

  public void addBorder(Country country1, Country country2) {
    adjCountries.get(country1).add(country2);
    adjCountries.get(country2).add(country1);
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    // add code here
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}

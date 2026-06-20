package io.muehlbachler.bswe.service;

import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.service.model.nearestairport.NearestAirportResultStation;
import tools.jackson.databind.JsonNode;

/**
 * A service to handle all aviation related actions.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface AviationService {
  /**
   * Returns the nearest airport to the given coordinates.
   *
   * @param coordinates the coordinates to search for the nearest airport
   * @return the nearest airport to the given coordinates
   */
  NearestAirportResultStation getNearestAirport(Coordinates coordinates);

  /**
   * Returns the latest METAR report for the given airport.
   *
   * @param icao the airport ICAO code
   * @return the METAR report
   */
  JsonNode getMetar(String icao);
}

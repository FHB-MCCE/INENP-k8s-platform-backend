package io.muehlbachler.bswe.controller;

import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.service.AviationService;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

/**
 * Controller to expose METAR data without exposing the AVWX token to clients.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/{userId}/{locationId}/metar")
@CrossOrigin
public class MetarController {
  @Autowired
  private final FavoriteLocationService favoriteLocationService;
  @Autowired
  private final AviationService aviationService;

  /**
   * Returns the latest METAR report for the location's nearest airport.
   *
   * @param userId the user id
   * @param locationId the favorite location id
   * @return the METAR report
   */
  @GetMapping("/")
  public ResponseEntity<JsonNode> get(@PathVariable final String userId,
      @PathVariable final String locationId) {
    final FavoriteLocation location = favoriteLocationService.get(userId, locationId);
    if (location == null || location.getNearestAirport() == null
        || location.getNearestAirport().isBlank()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    final JsonNode metar = aviationService.getMetar(location.getNearestAirport());
    if (metar == null) {
      return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
    return new ResponseEntity<>(metar, HttpStatus.OK);
  }
}

package io.muehlbachler.bswe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.service.AviationService;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
public class MetarControllerTest {
  private MetarController controller;

  @Mock
  private FavoriteLocationService favoriteLocationService;
  @Mock
  private AviationService aviationService;

  @BeforeEach
  public void setUp() {
    controller = new MetarController(favoriteLocationService, aviationService);
    reset(favoriteLocationService, aviationService);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(favoriteLocationService, aviationService);
  }

  @Test
  public void testGet() throws Exception {
    FavoriteLocation location = new FavoriteLocation();
    location.setNearestAirport("LOWW");
    JsonNode metar = JsonMapper.builder().build().readTree("{\"station\":\"LOWW\"}");
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);
    when(aviationService.getMetar("LOWW")).thenReturn(metar);

    ResponseEntity<JsonNode> result = controller.get("userId", "locationId");

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(metar, result.getBody());
    verify(favoriteLocationService, times(1)).get("userId", "locationId");
    verify(aviationService, times(1)).getMetar("LOWW");
  }

  @Test
  public void testGetNoLocation() {
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(null);

    ResponseEntity<JsonNode> result = controller.get("userId", "locationId");

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    verify(favoriteLocationService, times(1)).get("userId", "locationId");
  }

  @Test
  public void testGetNoAirport() {
    FavoriteLocation location = new FavoriteLocation();
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);

    ResponseEntity<JsonNode> result = controller.get("userId", "locationId");

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    verify(favoriteLocationService, times(1)).get("userId", "locationId");
  }

  @Test
  public void testGetUpstreamFailure() {
    FavoriteLocation location = new FavoriteLocation();
    location.setNearestAirport("LOWW");
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);
    when(aviationService.getMetar("LOWW")).thenReturn(null);

    ResponseEntity<JsonNode> result = controller.get("userId", "locationId");

    assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
    verify(favoriteLocationService, times(1)).get("userId", "locationId");
    verify(aviationService, times(1)).getMetar("LOWW");
  }
}

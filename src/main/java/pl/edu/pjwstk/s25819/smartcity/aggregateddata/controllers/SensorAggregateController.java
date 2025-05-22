package pl.edu.pjwstk.s25819.smartcity.aggregateddata.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AvailableSensorsRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AvailableSensorsResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.SensorAggregateResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.SensorAggregatesService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/aggregates")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SensorAggregateController {

    private final SensorAggregatesService sensorAggregatesService;

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<SensorAggregateResponseDto>> getBySensorId(
            @PathVariable String sensorId,
            @RequestParam String sensorType,
            @RequestParam String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1min") String resolution
    ) {
        return ResponseEntity.ok(sensorAggregatesService.getAggregates(sensorType, sensorId, startTime, endTime, resolution));
    }

    @GetMapping("/type/{sensorType}")
    public ResponseEntity<List<SensorAggregateResponseDto>> getBySensorType(
            @PathVariable String sensorType,
            @RequestParam String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1min") String resolution
    ) {
        return ResponseEntity.ok(sensorAggregatesService.getAggregates(sensorType, null, startTime, endTime, resolution));
    }

    @GetMapping
    public ResponseEntity<List<SensorAggregateResponseDto>> queryGeneral(
            @RequestParam String sensorType,
            @RequestParam(required = false) String sensorId,
            @RequestParam String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1min") String resolution
    ) {
        return ResponseEntity.ok(sensorAggregatesService.getAggregates(sensorType, sensorId, startTime, endTime, resolution));
    }

    @GetMapping("/available-types")
    public ResponseEntity<List<String>> getSensorTypes() {
        return ResponseEntity.ok(List.of("AIR_QUALITY", "TRAFFIC_FLOW"));
    }

    @GetMapping("/available-sensors")
    public ResponseEntity<List<AvailableSensorsResponseDto>> getAvailableSensors(@ModelAttribute AvailableSensorsRequestDto request) {

        log.info("Pobranie listy czujnikow z zagregowanych danych: {}", request);

        var results = sensorAggregatesService.getAvailableSensors(request);

        return ResponseEntity.ok(results);
    }
}

package pl.edu.pjwstk.s25819.smartcity.aggregateddata.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageResponseDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.services.AirQualityAveragesService;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/averages")
@RequiredArgsConstructor
@Slf4j
public class AveragesController {

    private final AirQualityAveragesService airQualityAveragesService;

    @PostMapping
    public ResponseEntity<List<AirQualityAverageResponseDto>> getAveragesForTimeWindow(@RequestBody AirQualityAverageRequestDto airQualityAverageRequestDto) {

        log.info("Obsługa żądania: " + airQualityAverageRequestDto);

        var results = airQualityAveragesService.getAverages(airQualityAverageRequestDto);

        return ResponseEntity.ok(results);
    }
}

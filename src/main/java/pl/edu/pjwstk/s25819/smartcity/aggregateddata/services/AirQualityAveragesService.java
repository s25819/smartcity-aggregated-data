package pl.edu.pjwstk.s25819.smartcity.aggregateddata.services;

import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageRequestDto;
import pl.edu.pjwstk.s25819.smartcity.aggregateddata.dto.AirQualityAverageResponseDto;

import java.util.List;

public interface AirQualityAveragesService {

    /**
     * Pobiera średnie wartości jakości powietrza na podstawie parametrów zapytania.
     *
     * @param request obiekt zawierający parametry zapytania (sensorId, startTime, endTime)
     * @return lista średnich wartości jakości powietrza
     * @throws IllegalArgumentException gdy parametry zapytania są nieprawidłowe
     */
    public List<AirQualityAverageResponseDto> getAverages(AirQualityAverageRequestDto request);

}

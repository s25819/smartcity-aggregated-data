package pl.edu.pjwstk.s25819.smartcity.aggregateddata.streamprocessing;

import java.time.Instant;

public interface Aggregator<T, R> {
    /**
     * Dodaje nowe dane obserwacyjne do agregatora.
     *
     * @param data Dane wejściowe typu T rozszerzające ObservedData
     * @return Ten sam obiekt agregatora z dodanymi danymi
     */
    Aggregator<T, R> add(T data);

    /**
     * Oblicza średnią wartość dla zebranych danych w określonym przedziale czasowym.
     *
     * @param sensorId    Identyfikator czujnika
     * @param windowStart Początek okna czasowego
     * @param windowEnd   Koniec okna czasowego
     * @return Zagregowany wynik typu R
     */
    R computeAverage(String sensorId, Instant windowStart, Instant windowEnd);
}
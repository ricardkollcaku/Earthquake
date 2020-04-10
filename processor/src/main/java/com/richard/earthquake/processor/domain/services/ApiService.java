package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.dto.EarthquakesDto;
import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.domain.util.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ApiService {
    @Autowired
    StreamProvider streamProvider;
    AtomicLong lastEventTime;
    @Autowired
    EarthquakeService earthquakeService;
    WebClient myBufferedWebClient;

    @PostConstruct
    void getDataFromApi() {
        lastEventTime = new AtomicLong();
        streamProvider.subscribe(listenToAllEarthquakes().log());
        earthquakeService.saveAll(getDailyEarthquake()).log().subscribe();

    }

    private Flux<Earthquake> listenToAllEarthquakes() {

        return earthquakeService.getLastEarthquake()
                .map(Earthquake::getModifiedTime)
                .map(this::setLastEvent)
                .flatMapMany(aLong -> Flux.concat(getFirsAllNewElements(), Flux.merge(getQueriedEarthquake(), getLastHoursEarthquake())))
                .filter(this::isNewEvent)
                .map(this::updateLastEvent)
                .switchIfEmpty(firstTimeServerStart())
                ;

    }

    private Flux<Earthquake> firstTimeServerStart() {
        return Flux.merge(getLastMonthEarthquakes(), getLast30MonthEarthquakes());
    }

    private Flux<Earthquake> getLast30MonthEarthquakes() {
        return Flux.range(0, 1440)
                .map(integer -> integer * 10)
                .delayElements(Duration.ofSeconds(4))
                .flatMap(this::getByMonthRemoving);
    }

    private Flux<Earthquake> getByMonthRemoving(Integer integer) {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);
        String currentTime, previewTime;
        currentTime = dateTime.minusDays(integer).toString();
        previewTime = dateTime.minusDays(integer + 10).toString();
        System.out.println(currentTime + " " + previewTime + "  " + integer);
        return getQueriedEarthquake(currentTime, previewTime, getWebClient()).retryBackoff(30, Duration.ofSeconds(10));
    }

    private Flux<Earthquake> getLastMonthEarthquakes() {
        return getEarthquakeByUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson", getWebClient())
                .retryBackoff(30, Duration.ofSeconds(10));
    }

    private Flux<Earthquake> getFirsAllNewElements() {

        DateTime dateTime = DateTime.now(DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime(Long.valueOf(lastEventTime.get()), DateTimeZone.UTC);
        String currentTime, previewTime;
        currentTime = dateTime.toString();
        previewTime = dateTime2.toString();
        return getQueriedEarthquake(currentTime, previewTime, getWebClient())
                .retryBackoff(30, Duration.ofSeconds(10));

    }

    public WebClient getWebClient() {
        if (myBufferedWebClient == null) {
            ExchangeStrategies exchangeStrategies = ExchangeStrategies
                    .builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(524288000))
                    .build();
            myBufferedWebClient = WebClient.builder().exchangeStrategies(exchangeStrategies).build();
        }

        return myBufferedWebClient;
    }

    private Flux<Earthquake> getQueriedEarthquake(String currentTime, String previewTime, WebClient webClient) {
        return getEarthquakeByUrl("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + previewTime + "&endtime=" + currentTime, webClient);
    }

    private Flux<Earthquake> getLastHoursEarthquake() {

        return Flux.interval(Duration.ofMinutes(1))
                .flatMap(aLong -> getEarthquakeByUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson"))
                .retryBackoff(30, Duration.ofSeconds(10));

    }

    private Long setLastEvent(Long aLong) {
        lastEventTime.set(aLong);
        return aLong;
    }

    private Flux<Earthquake> getQueriedEarthquake() {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);
        String currentTime, previewTime;
        currentTime = dateTime.toString();
        previewTime = dateTime.minusMinutes(5).toString();
        return Flux.interval(Duration.ofSeconds(30))
                .flatMap(aLong -> getQueriedRequestByTime(previewTime, currentTime)
                        .onErrorResume(throwable -> Flux.empty()));

    }

    private Flux<Earthquake> getDailyEarthquake() {
        return Flux.interval(Duration.ofHours(3)).flatMap(aLong -> getEarthquakeByUrl(
                "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson"))
                .retryBackoff(30, Duration.ofSeconds(10));
    }

    Flux<Earthquake> getEarthquakeByUrl(String url, WebClient webClient) {
        return webClient.get()
                .uri(url)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(EarthquakesDto.class))
                .flatMapIterable(EarthquakesDto::getEarthquakes)
                .flatMap(ObjectMapper::mapMono)
                .log()
                .retryBackoff(100, Duration.ofSeconds(30));
    }

    Flux<Earthquake> getEarthquakeByUrl(String url) {
        return WebClient.create(url).get().exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(EarthquakesDto.class))
                .flatMapIterable(EarthquakesDto::getEarthquakes)
                .flatMap(ObjectMapper::mapMono);
    }

    private Flux<Earthquake> getQueriedRequestByTime(String previewTime, String currentTime) {
        return getEarthquakeByUrl(
                "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + previewTime + "&endtime=" + currentTime);
    }

    private Earthquake updateLastEvent(Earthquake earthquake) {
        lastEventTime.set(earthquake.getModifiedTime());
        return earthquake;
    }

    private boolean isNewEvent(Earthquake earthquake) {
        return earthquake.getModifiedTime() > lastEventTime.get();
    }

}

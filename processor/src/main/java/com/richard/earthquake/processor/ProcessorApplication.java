package com.richard.earthquake.processor;

import com.richard.earthquake.processor.data.dto.EarthquakesDto;
import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.data.repo.EarthquakeRepo;
import com.richard.earthquake.processor.domain.services.ApiService;
import com.richard.earthquake.processor.domain.services.EarthquakeService;
import com.richard.earthquake.processor.domain.services.StreamProvider;
import com.richard.earthquake.processor.domain.util.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProcessorApplication implements CommandLineRunner {
    @Autowired
    StreamProvider streamProvider;
    @Autowired
    ApiService apiService;
    @Autowired
    EarthquakeService earthquakeService;
    @Autowired
    EarthquakeRepo earthquakeRepo;

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    private Flux<Earthquake> getRequest(int month) {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);
        String currentTime, previewTime;
        currentTime = dateTime.minusMonths((month - 1)).toString();
        previewTime = dateTime.minusMonths(month).toString();
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(524288000)).build();
        WebClient webClient = WebClient.builder().exchangeStrategies(exchangeStrategies).build();
        System.out.println("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + previewTime + "&endtime=" + currentTime);
        return webClient.get()
                .uri("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + previewTime + "&endtime=" + currentTime)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(EarthquakesDto.class))
                .flatMapIterable(EarthquakesDto::getEarthquakes)
                .map(ObjectMapper::map)
                ;
    }

    @Override
    public void run(String... args) throws Exception {


        earthquakeRepo.saveAll(earthquakeRepo.findAll().map(earthquake -> {
            earthquake.setCountry(earthquake.getProperties().getPlace().substring(earthquake.getProperties().getPlace().lastIndexOf(" ") + 1));
            return earthquake;
        })).log().subscribe();
/*        earthquakeRepo.findAll().map(earthquake -> earthquake.getProperties().getPlace())
                .map(s -> s.substring(s.lastIndexOf(" ") + 1))
                .distinct()
                .subscribe(System.out::println);*/
        //https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2019-07-27T19:25:06.310Z&endtime=2019-08-27T19:25:06.310Z

    }
}

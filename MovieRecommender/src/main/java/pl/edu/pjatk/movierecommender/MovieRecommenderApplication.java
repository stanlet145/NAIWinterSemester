package pl.edu.pjatk.movierecommender;

import io.vavr.control.Try;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.pjatk.movierecommender.service.CsvParser;
import pl.edu.pjatk.movierecommender.service.CsvReader;
import pl.edu.pjatk.movierecommender.service.MovieRecommender;

@SpringBootApplication
public class MovieRecommenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieRecommenderApplication.class, args);
        Try.run(() -> new MovieRecommender(new CsvParser()
                .tryParseCsv(new CsvReader()
                        .tryReadCsvFromResources()
                )));
    }

}

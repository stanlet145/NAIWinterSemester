package pl.edu.pjatk.movierecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.pjatk.movierecommender.service.CsvReader;

@SpringBootApplication
public class MovieRecommenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieRecommenderApplication.class, args);
        new CsvReader().readCsvFromResources();
    }

}

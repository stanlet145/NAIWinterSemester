package pl.edu.pjatk.movierecommender.service;

import io.vavr.control.Option;
import org.apache.commons.lang3.StringUtils;
import pl.edu.pjatk.movierecommender.model.MovieRating;
import pl.edu.pjatk.movierecommender.model.MovieUserData;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public List<MovieUserData> parseCsv(List<String[]> stringList) {
        var movieUserData = new ArrayList<MovieUserData>();
        for (String[] strings : stringList) {
            var userMovieRatings = new ArrayList<MovieRating>();
            String userName = strings[0];
            for (int i = 1; i <= 62; i = i + 2) {
                userMovieRatings.add(new MovieRating(strings[i], Option.of(strings[i + 1])
                        .filter(StringUtils::isNotEmpty)
                        .map(Double::parseDouble)
                        .getOrNull()));
            }
            movieUserData.add(new MovieUserData(userName, userMovieRatings));
        }
        return movieUserData;
    }
}

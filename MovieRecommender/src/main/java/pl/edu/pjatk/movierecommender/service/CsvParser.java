package pl.edu.pjatk.movierecommender.service;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import pl.edu.pjatk.movierecommender.model.MovieRating;
import pl.edu.pjatk.movierecommender.model.MovieUserData;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private static final int USER_NAME_INDEX = 0;
    private static final int MOVIE_NAME_START_INDEX = 1;
    private static final int MOVIE_RATING_START_INDEX = 2;
    private static final int NUMBER_OF_INDEXES_IN_CSV_LINE = 62;

    /**
     * Create movieUserData list and process all csv lines adding to this list
     *
     * @param lines csv lines
     * @return movieUserData list parsed from csv
     */
    public List<MovieUserData> tryParseCsv(List<String[]> lines) {
        var movieUserData = new ArrayList<MovieUserData>();
        return Try.success(lines)
                .peek(csvLines -> processAllLines(csvLines, movieUserData))
                .map($ -> movieUserData)
                .getOrElseThrow(throwable -> new RuntimeException(throwable));
    }

    /**
     * iterates over each line and processes it
     */
    private void processAllLines(List<String[]> csvLines, List<MovieUserData> movieUserData) {
        csvLines.forEach(line -> processLineAndCollectToMovieUserData(line, movieUserData));
    }

    /**
     * sets start index for user name, movie name, movie rating and iterates over entire line assigning them to objects
     */
    private void processLineAndCollectToMovieUserData(String[] lines, List<MovieUserData> movieUserData) {
        var userMovieRatings = new ArrayList<MovieRating>();
        var userName = lines[USER_NAME_INDEX];
        for (int i = MOVIE_NAME_START_INDEX; i <= NUMBER_OF_INDEXES_IN_CSV_LINE; i = i + MOVIE_RATING_START_INDEX) {
            var movieRating = new MovieRating(lines[i], getMovieRatingForMovieNameOrNull(lines, i));
            userMovieRatings.add(movieRating);
        }
        movieUserData.add(new MovieUserData(userName, userMovieRatings));
    }

    /**
     * checks either rating can be parsed to double, if not returns null
     */
    private Double getMovieRatingForMovieNameOrNull(String[] strings, int i) {
        return Option.of(strings[i + 1])
                .filter(StringUtils::isNotEmpty)
                .map(Double::parseDouble)
                .getOrNull();
    }

}

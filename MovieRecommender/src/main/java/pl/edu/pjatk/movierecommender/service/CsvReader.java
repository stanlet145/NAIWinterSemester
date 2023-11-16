package pl.edu.pjatk.movierecommender.service;

import com.opencsv.CSVReader;
import io.vavr.control.Try;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class CsvReader {
    private static final String SYSTEM_RESOURCE_CSV_PATH = "csv/movie_ratings.csv";

    /**
     * Starts csv reading from resources
     */
    public void readCsvFromResources() {
        Try.of(() -> ClassLoader.getSystemResource(SYSTEM_RESOURCE_CSV_PATH))
                .map(tryGetUri)
                .map(Paths::get)
                .map(this::readAllLines);
    }

    /**
     * Based on given path returns content for given csv
     *
     * @param filePath csv file path
     * @return file content
     */
    public List<String[]> readAllLines(Path filePath) {
        return Try.of(() -> Files.newBufferedReader(filePath))
                .map(CSVReader::new)
                .map(tryReadAllLines)
                .get();
    }

    /**
     * try get uri form url by URI API
     */
    private static final Function<URL, URI> tryGetUri = url ->
            Try.of(url::toURI)
                    .getOrElseThrow(throwable -> new RuntimeException(throwable));
    /**
     * read lines from file and return them as list of String arrays
     */
    private static final Function<CSVReader, List<String[]>> tryReadAllLines = reader ->
            Try.of(reader::readAll)
                    .getOrElseThrow(throwable -> new RuntimeException(throwable));
}

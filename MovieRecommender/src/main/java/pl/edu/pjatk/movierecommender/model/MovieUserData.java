package pl.edu.pjatk.movierecommender.model;

import java.util.List;

public record MovieUserData(String userName, List<MovieRating> movieRatings) {
}

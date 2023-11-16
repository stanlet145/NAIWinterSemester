package pl.edu.pjatk.movierecommender.service;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import pl.edu.pjatk.movierecommender.model.MovieRating;
import pl.edu.pjatk.movierecommender.model.MovieUserData;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MovieRecommender {

    private static int currentID = 0;
    private static final HashMap<String, Integer> movieNameIDMap = new HashMap<>();

    /**
     * for each unique movie string assign unique int id
     *
     * @param movieRatings list of movie ratings
     */
    public static void assignIDs(List<MovieRating> movieRatings) {
        for (MovieRating movieRating : movieRatings) {
            String movieName = movieRating.movieName();

            if (!movieNameIDMap.containsKey(movieName)) {
                movieNameIDMap.put(movieName, currentID);
                currentID++;
            }
        }
    }

    /**
     * Create preferences from movie User Data
     * maps user name to user id,
     * sets movie id and rating for preference
     *
     * @param movieUserData
     */
    private void createPreferences(List<MovieUserData> movieUserData) {
        movieUserData.forEach(user -> assignIDs(user.likedItems()));
        AtomicLong counter = new AtomicLong();
        movieUserData.forEach(user -> {
            movieNameIDMap.forEach((s, integer) ->
                    new GenericPreference(counter.get(), integer, user
                            .likedItems()
                            .stream().
                            filter(movieRating -> movieRating.movieName().equals(s))
                            .findFirst()
                            .map(MovieRating::rating)
                            .get()));
            counter.getAndIncrement();
        });
    }

    /**
     * Constructor starts Mahout algorithms usage for Movie recommendations
     *
     * @param movieUserData given user data from csv with user name and user likings
     * @throws TasteException in case Mahout library fails to execute
     */
    public MovieRecommender(List<MovieUserData> movieUserData) throws TasteException {
        createPreferences(movieUserData);

        FastByIDMap<PreferenceArray> preferenceArrayMap = new FastByIDMap<>();

        //Creating data model
        DataModel datamodel = new GenericDataModel(preferenceArrayMap);

        //Creating UserSimilarity object.
        UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(datamodel);

        //Creating UserNeighbourHHood object.
        UserNeighborhood userneighborhood = new ThresholdUserNeighborhood(3.0, usersimilarity, datamodel);

        //Create UserRecomender
        UserBasedRecommender recommender = new GenericUserBasedRecommender(datamodel, userneighborhood, usersimilarity);

        List<RecommendedItem> recommendations = recommender.recommend(2, 3);

        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }
    }
}
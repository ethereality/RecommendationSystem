package recommender.main;

import java.util.*;

import recommender.main.Ratings.RATING;

public class Recommendations {

	private static final double LIKING_PROBABILITY_THRESHOLD = 0.0;
	private Map<User, Set<Item>> suggestions = new HashMap<>();	
	
	public Set<Item> get(User user)
	{
		return Optional.ofNullable(suggestions.get(user))
				.orElse(Collections.emptySet());
	}
	
	public void update(User user, Set<Item> allItems,
			Similarity similarity, Ratings ratings)
	{
		Set<Item> newSuggestions = new HashSet<>();
		
		for(Item unratedItem : ratings.itemsNotRatedByUser(user, allItems)) {
			Set<User> usersWhoLikedThis = ratings.usersWhoRatedItem(unratedItem, RATING.LIKE);
			Set<User> usersWhoDislikedThis = ratings.usersWhoRatedItem(unratedItem, RATING.DISLIKE);
			double similarityWithUsersWhoLikedThis = similarity.similarityIndexOf(user, usersWhoLikedThis, ratings);
			double similarityWithUsersWhoDislikedThis = similarity.similarityIndexOf(user, usersWhoDislikedThis, ratings);
			
			double probabilityOfLikingThis = 
					(similarityWithUsersWhoLikedThis - similarityWithUsersWhoDislikedThis)
					/(usersWhoLikedThis.size() + usersWhoDislikedThis.size());
			if(probabilityOfLikingThis > LIKING_PROBABILITY_THRESHOLD) {
				newSuggestions.add(unratedItem);
			}
		}
		
		suggestions.put(user, newSuggestions);
	}

}

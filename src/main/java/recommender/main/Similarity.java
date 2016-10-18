package recommender.main;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Similarity {	
	
	private static final double SIMILARITY_THRESHOLD = 0.0; 
	private Map<User, Set<User>> similarUsers = new HashMap<>();
	
	public Set<User> getSimilarUsers(User user)
	{
		return Optional.ofNullable(similarUsers.get(user))
				.orElse(Collections.emptySet());
	}
	
	public void update(Set<User> allUsers, Ratings ratings)
	{
		for(User user : allUsers) {
			Set<User> similars = getSimilarUsers(user, allUsers, ratings);
			similarUsers.put(user, similars);
		}
	}
	
	private Set<User> getSimilarUsers(User user, Set<User> allUsers, Ratings ratings)
	{		
		Set<User> similars = new HashSet<>();		
		for(User otherUser: allUsers) {
			double similarityIndex = similarityIndexOf(user, otherUser, ratings);
			if(similarityIndex > SIMILARITY_THRESHOLD) {
				similars.add(otherUser);
			}
		}
		return similars;	
	}
	
	protected double similarityIndexOf(User user, Set<User> usersToCompare, Ratings ratings)
	{
		return usersToCompare.stream()
				.collect(Collectors.summingDouble(userToCompare -> calculateSimilarityIndexOf(user, userToCompare, ratings)));
	}
	
	private double similarityIndexOf(User firstUser, User secondUser, Ratings ratings)
	{
		if(firstUser.equals(secondUser)) {
			return 1.0;
		} else if(ratings.itemsRatedByUser(firstUser).isEmpty() &&
				ratings.itemsRatedByUser(secondUser).isEmpty()) {
			return 0.0;
		} else {
			return calculateSimilarityIndexOf(firstUser, secondUser, ratings);			
		}		
	}

	@SuppressWarnings("unchecked")
	private double calculateSimilarityIndexOf(User firstUser, User secondUser, Ratings ratings) 
	{
		Set<Item> firstUserLikes = ratings.likes(firstUser);
		Set<Item> secondUserLikes = ratings.likes(secondUser);
		Set<Item> firstUserDislikes = ratings.dislikes(firstUser);
		Set<Item> secondUserDislikes = ratings.dislikes(secondUser);
		
		double intersection = intersection(firstUserLikes, secondUserLikes).size()
				+ intersection(firstUserDislikes, secondUserDislikes).size()
				- intersection(firstUserLikes, secondUserDislikes).size()
				- intersection(secondUserLikes, firstUserDislikes).size();
		double union = union(firstUserLikes, secondUserLikes, firstUserDislikes, secondUserDislikes).size();
		double similarityIndex = intersection/union;
		
		return similarityIndex;
	}
	
	private Set<Item> intersection(Set<Item> firstSet, Set<Item> secondSet)
	{
		return firstSet.stream()
				.filter(secondSet::contains)
				.collect(Collectors.toSet());
	}
	
	@SuppressWarnings("unchecked")
	private Set<Item> union(Set<Item>... sets)
	{
		return Stream.of(sets)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}
}

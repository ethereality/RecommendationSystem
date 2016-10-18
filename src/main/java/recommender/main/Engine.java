package recommender.main;

import java.util.*;

import recommender.main.Ratings.RATING;

public class Engine {

	private Set<User> users = new HashSet<>();
	private Set<Item> items = new HashSet<>();
	
	private Ratings ratings = new Ratings();
	private Similarity similarity = new Similarity();
	private Recommendations recommendations = new Recommendations();
	
	public void addUsers(User... usersToAdd)
	{
		users.addAll(Arrays.asList(usersToAdd));
	}
	
	public void addItems(Item... itemsToAdd)
	{
		items.addAll(Arrays.asList(itemsToAdd));
	}
	
	public Set<User> getUsers()
	{
		return Collections.unmodifiableSet(users);
	}
	
	public Set<Item> getItems()
	{
		return Collections.unmodifiableSet(items);
	}
	
	public void like(User user, Item item) 
	{
		ratings.add(user, item, RATING.LIKE);		
	}
	
	public void dislike(User user, Item item) 
	{
		ratings.add(user, item, RATING.DISLIKE);		
	}
	
	public void removeRating(User user, Item item)
	{
		ratings.remove(user, item);
	}
	
	public RATING getRating(User user, Item item) 
	{
		return ratings.get(user, item);
	}
	
	public Set<Item> getRecommendations(User user) 
	{		
		similarity.update(users, ratings);
		recommendations.update(user, items, similarity, ratings);
		
		return recommendations.get(user);
	}		
}

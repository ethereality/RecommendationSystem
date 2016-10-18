package recommender.main;

import java.util.*;
import java.util.stream.*;

public class Ratings {
	
	public enum RATING {LIKE, DISLIKE, UNSPECIFIED};
	
	private Map<User, Set<Item>> likes = new HashMap<>();
	private Map<User, Set<Item>> dislikes = new HashMap<>();	
	
	public Set<Item> likes(User user) 
	{
		return Optional.ofNullable(likes.get(user))
				.orElse(Collections.emptySet());
	}

	public Set<Item> dislikes(User user) 
	{
		return Optional.ofNullable(dislikes.get(user))
				.orElse(Collections.emptySet());
	}
	
	public void add(User user, Item item, RATING rating)
	{
		remove(user, item);
		
		if(rating.equals(RATING.LIKE)) {
			addLike(user, item);
		} else if(rating.equals(RATING.DISLIKE)) {
			addDislike(user, item);
		}
	}
	
	private void addLike(User user, Item item) 
	{		
		if(!likes.containsKey(user)) {
			likes.put(user, new HashSet<>());
		}
		
		likes.get(user).add(item);
	}

	private void addDislike(User user, Item item) 
	{
		if(!dislikes.containsKey(user)) {
			dislikes.put(user, new HashSet<>());
		}
		
		dislikes.get(user).add(item);		
	}

	public void remove(User user, Item item)
	{
		if(likes.containsKey(user)) {
			likes.get(user).remove(item);
		}
		
		if(dislikes.containsKey(user)) {
			dislikes.get(user).remove(item);
		}
	}

	public RATING get(User user, Item item)
	{
		if(likes.containsKey(user) && likes.get(user).contains(item)) {
			return RATING.LIKE;
		} else if(dislikes.containsKey(user) && dislikes.get(user).contains(item)) {
			return RATING.DISLIKE;
		} else {
			return RATING.UNSPECIFIED;
		}
	}

	public Set<Item> itemsRatedByUser(User user)
	{
		return Stream.concat(likes(user).stream(), dislikes(user).stream())
				.collect(Collectors.toSet());
	}
	
	public Set<Item> itemsNotRatedByUser(User user, Set<Item> allItems)
	{
		return allItems.stream()
				.filter(i -> !itemsRatedByUser(user).contains(i))
				.collect(Collectors.toSet());
	}

	public Set<User> usersWhoRatedItem(Item item, RATING ratingType)
	{
		if(ratingType.equals(RATING.UNSPECIFIED)) {
			throw new UnsupportedOperationException();			
		}
		
		Map<User, Set<Item>> ratingsToSearch = ratingType == RATING.LIKE ? likes : dislikes;
		return ratingsToSearch.entrySet().stream()
				.filter(like -> like.getValue().contains(item))
				.map(e -> e.getKey())
				.collect(Collectors.toSet());		
	}	
}

package com.recommender;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;

import recommender.main.Item;
import recommender.main.Ratings;
import recommender.main.Similarity;
import recommender.main.User;
import recommender.main.Ratings.RATING;

public class SimilarityTest {
	
	@Test
	public void givenAUserWhoHasNotRatedAnyItemsThenTheUserIsSimilarToHimself()
	{
		Similarity similarity = new Similarity();
		User Alice = new User("Alice");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice));		
		
		Ratings ratings = new Ratings();
		similarity.update(allUsers, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), contains(Alice));	
	}
	
	@Test
	public void givenAUserWhoHasRatedSomeItemsThenTheUserIsSimilarToHimself()
	{
		Similarity similarity = new Similarity();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice));		
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		similarity.update(allUsers, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), contains(Alice));
	}
		
	@Test
	public void givenNeitherUserANorUserBHasRatedAnyItemsThenUserAAndUserBAreNotSimilar()
	{
		Similarity similarity = new Similarity();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));		
		
		Ratings ratings = new Ratings();
		similarity.update(allUsers, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItem(Bob)));
		assertThat(similarity.getSimilarUsers(Bob), not(hasItem(Alice)));
	}
	
	@Test
	public void givenUserAAndUserBLikeAllTheSameItemsAndDislikeAllTheSameItemsThenUserAAndUserBAreSimilar()
	{
		Similarity similarity = new Similarity();
		Ratings ratings = new Ratings();
		
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.LIKE);
		ratings.add(Alice, curling, RATING.DISLIKE);

		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		ratings.add(Bob, curling, RATING.DISLIKE);
		
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		similarity.update(allUsers, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItem(Bob));
		assertThat(similarity.getSimilarUsers(Bob), hasItem(Alice));		
	}
	
	@Test
	public void givenUserANeitherLikesAnyItemsUserBLikesNorDislikesAnyItemsUserBDislikesThenUserAAndUserBAreDissimilar()
	{
		Similarity similarity = new Similarity();
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);

		ratings.add(Bob, aerobics, RATING.DISLIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		ratings.add(Bob, curling, RATING.DISLIKE);
		
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		similarity.update(allUsers, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItem(Bob)));
		assertThat(similarity.getSimilarUsers(Bob), not(hasItem(Alice)));		
	}
	
	@Test
	public void givenUserAAndUserBHaveSameNumberOfLikesAndDislikesInCommonAsNotInCommonThenUserAAndUserBAreNotSimilar()
	{
		Similarity similarity = new Similarity();
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		User Bob = new User("Bobr");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.DISLIKE);
		ratings.add(Alice, dodgeball, RATING.LIKE);

		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);		
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.DISLIKE);
		
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		similarity.update(allUsers, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItem(Bob)));
		assertThat(similarity.getSimilarUsers(Bob), not(hasItem(Alice)));		
	}

	@Test
	public void givenUserAAndUserBHaveMoreLikesAndDislikesInCommonThanUncommonLikesAndDislikesThenUserAAndUserBAreSimilar()
	{
		Similarity similarity = new Similarity();
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);

		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.DISLIKE);
		
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		similarity.update(allUsers, ratings);		
		
		assertThat(similarity.getSimilarUsers(Alice), hasItem(Bob));
		assertThat(similarity.getSimilarUsers(Bob), hasItem(Alice));		
	}
	
	@Test
	public void givenUserAAndUserBHaveLessLikesAndDislikesInCommonThanUncommonLikesAndDislikesThenUserAAndUserBAreNotSimilar()
	{
		Similarity similarity = new Similarity();
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);

		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		ratings.add(Bob, curling, RATING.DISLIKE);
		
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		similarity.update(allUsers, ratings);				
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItem(Bob)));
		assertThat(similarity.getSimilarUsers(Bob), not(hasItem(Alice)));		
	}	
}

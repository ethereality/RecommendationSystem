package com.recommender;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.*;

import recommender.main.*;
import recommender.main.Ratings.RATING;

public class RecommendationsTest {

	@Test
	public void givenAUserWithNoRatedItemsThenTheUserGetsNoRecommendations()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = Collections.singleton(new Item("aerobics"));
		
		Ratings ratings = new Ratings();
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);		
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(ratings.itemsRatedByUser(Alice), is(empty()));
		assertThat(recommendations.get(Alice), is(empty()));	
	}
	
	@Test
	public void givenAUserWithNoUnratedItemsThenTheUserGetsNoRecommendations()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");		
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball));
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);				
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(ratings.itemsNotRatedByUser(Alice, allItems), is(empty()));
		assertThat(recommendations.get(Alice), is(empty()));		
	}
	
	@Test
	public void givenAUserWithNoSimilarUsersOtherThanHerselfThenTheUserGetsNoRecommendations()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");		
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball));
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), contains(Alice));
		assertThat(recommendations.get(Alice), is(empty()));
	}
	
	@Test
	public void givenUserAIsSimilarToUserBThenItemsLikedByUserBAndNotYetRatedByUserAAreRecommendedToUserA()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball));
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItem(Bob));
		assertThat(recommendations.get(Alice), contains(basketball));		
	}
	
	@Test
	public void givenUserAIsSimilarToUserBThenItemsDisLikedByUserBAreNotRecommendedToUserA()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling));
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItem(Bob));
		assertThat(recommendations.get(Alice), not(hasItem(basketball)));			
	}
	
	@Test
	public void givenUserAIsDissmilarToUserBThenItemsLikedByUserBAreNotRecommendedToUserA()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball));
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Bob, aerobics, RATING.DISLIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItem(Bob)));
		assertThat(recommendations.get(Alice), not(hasItem((basketball))));			
	}
	
	@Test
	public void givenUserAIsDissmilarToUserBThenItemsDislikedByUserBAreRecommendedToUserA()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball));
		
		Ratings ratings = new Ratings();
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Bob, aerobics, RATING.DISLIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItem(Bob)));
		assertThat(recommendations.get(Alice), hasItem((basketball)));					
	}	
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanUserCAndAnItemIsLikedByBothUsersThenUserAIsRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.LIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.DISLIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.LIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItems(Bob, Cam));
		assertThat(recommendations.get(Alice), hasItem((dodgeball)));					
	}
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanUserCAndAnItemIsDislikedByBothUsersThenUserAIsNotRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.DISLIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.DISLIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.DISLIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItems(Bob, Cam));
		assertThat(recommendations.get(Alice), not(hasItem((dodgeball))));		
	}
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanUserCAndAnItemIsLikedByUserBButDislikedByUserCThenAIsRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.LIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.DISLIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.DISLIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItems(Bob, Cam));
		assertThat(recommendations.get(Alice), hasItem((dodgeball)));		
	}

	@Test
	public void givenUserAIsMoreSimilarToUserBThanUserCAndAnItemIsDislikedByUserBButLikedByUserCThenAIsNotRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.DISLIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.DISLIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.LIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), hasItems(Bob, Cam));
		assertThat(recommendations.get(Alice), not(hasItem((dodgeball))));		
	}
	
	@Test
	public void givenUserAIsDissimilarToBothUserBAndUserCThenUserAIsNotRecommendedAnyItemsLikedByUserBOrUserC()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.DISLIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		ratings.add(Bob, curling, RATING.DISLIKE);
		ratings.add(Bob, dodgeball, RATING.LIKE);
		
		ratings.add(Cam, aerobics, RATING.DISLIKE);
		ratings.add(Cam, basketball, RATING.LIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItems(Bob, Cam)));
		assertThat(recommendations.get(Alice), not(hasItem((dodgeball))));		
	}
	
	@Test
	public void givenUserAIsDissimilarToBothUserBAndUserCThenUserAIsRecommendedAnyItemsLikedByUserBOrUserC()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.DISLIKE);
		ratings.add(Bob, basketball, RATING.LIKE);
		ratings.add(Bob, curling, RATING.DISLIKE);
		ratings.add(Bob, dodgeball, RATING.DISLIKE);
		
		ratings.add(Cam, aerobics, RATING.DISLIKE);
		ratings.add(Cam, basketball, RATING.LIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItems(Bob, Cam)));
		assertThat(recommendations.get(Alice), hasItem((dodgeball)));			
	}
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanDissimilarToUserCAndAnItemIsLikedByBothUsersThenUserAIsRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.LIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.LIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.LIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItems(Bob, Cam)));
		assertThat(recommendations.get(Alice), hasItem((dodgeball)));					
	}
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanDissimilarToUserCAndAnItemIsDislikedByBothUsersThenUserAIsNotRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.DISLIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.LIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.DISLIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItems(Bob, Cam)));
		assertThat(recommendations.get(Alice), not(hasItem((dodgeball))));			
	}
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanDissimilarToUserCAndAnItemIsLikedByUserBButDislikedByUserCThenUserAIsRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.LIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.LIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.DISLIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItems(Bob, Cam)));
		assertThat(recommendations.get(Alice), hasItem((dodgeball)));			
	}
	
	@Test
	public void givenUserAIsMoreSimilarToUserBThanDissimilarToUserCAndAnItemIsDislikedByUserBButLikedByUserCThenUserAIsNotRecommendedThatItem()
	{
		Recommendations recommendations = new Recommendations();
		User Alice = new User("Alice");	
		User Bob = new User("Bob");
		User Cam = new User("Cam");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		Item dodgeball = new Item("dodgeball");
		Set<User> allUsers = new HashSet<>(Arrays.asList(Alice, Bob, Cam));
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics, basketball, curling, dodgeball));
		
		Ratings ratings = new Ratings();
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Alice, basketball, RATING.DISLIKE);
		ratings.add(Alice, curling, RATING.LIKE);
		
		ratings.add(Bob, aerobics, RATING.LIKE);
		ratings.add(Bob, basketball, RATING.DISLIKE);
		ratings.add(Bob, curling, RATING.LIKE);
		ratings.add(Bob, dodgeball, RATING.DISLIKE);
		
		ratings.add(Cam, aerobics, RATING.LIKE);
		ratings.add(Cam, basketball, RATING.LIKE);
		ratings.add(Cam, curling, RATING.DISLIKE);
		ratings.add(Cam, dodgeball, RATING.LIKE);
		
		Similarity similarity = new Similarity();
		similarity.update(allUsers, ratings);
		
		recommendations.update(Alice, allItems, similarity, ratings);
		
		assertThat(similarity.getSimilarUsers(Alice), not(hasItems(Bob, Cam)));
		assertThat(recommendations.get(Alice), not(hasItem((dodgeball))));			
	}
}

package com.recommender;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

import org.junit.Test;

import recommender.main.*;
import recommender.main.Ratings.RATING;

public class EngineTest {	
	
	@Test
	public void givenANamedUserWhenUserIsAddedThenUserGetsAddedSuccessfully()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		
		engine.addUsers(Alice);
		
		assertThat(engine.getUsers(), contains(Alice));		
	}
	
	@Test
	public void givenTwoUsersWithSameNameWhenUsersAreAddedThenOnlyOneUserIsRecorded()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User anotherAlice = new User("Alice");
		
		engine.addUsers(Alice, anotherAlice);
		
		assertThat(engine.getUsers().size(), is(1));		
	}
	
	@Test
	public void givenANamedItemWhenItemIsAddedThenItemGetsAddedSuccessfully()
	{
		Engine engine = new Engine();
		Item aerobics = new Item("Aerobics");
		
		engine.addItems(aerobics);
		
		assertThat(engine.getItems(), contains(aerobics));	
	}
	
	@Test
	public void givenTwoItemsWithSameNameWhenItemsAreAddedThenOnlyOneItemIsRecorded()
	{
		Engine engine = new Engine();
		Item aerobics = new Item("Aerobics");
		Item moreAerobics = new Item("Aerobics");
		
		engine.addItems(aerobics, moreAerobics);
		
		assertThat(engine.getItems().size(), is(1));		
	}
	
	@Test
	public void givenNoUsersExistWhenUsersAreQueriedThenNoUsersAreReturned()
	{
		Engine engine = new Engine();
		
		assertThat(engine.getUsers(), is(empty()));	
	}
	
	@Test
	public void givenUsersExistWhenUsersAreQueriedThenAllUsersAreReturned()
	{
		Engine engine = new Engine();
		
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		engine.addUsers(Alice, Bob);
		
		assertThat(engine.getUsers(), containsInAnyOrder(Alice, Bob));	
	}
	
	@Test
	public void givenNoItemsExistWhenItemsAreQueriedThenNoItemsAreReturned()
	{
		Engine engine = new Engine();
		
		assertThat(engine.getItems(), is(empty()));
	}
	
	@Test
	public void givenItemsExistWhenItemsAreQueriedThenAllItemsAreReturned()
	{
		Engine engine = new Engine();
		
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		engine.addItems(aerobics, basketball);
		
		assertThat(engine.getItems(), containsInAnyOrder(aerobics, basketball));
	}
	
	@Test
	public void givenAnItemNotRatedByUserWhenUserLikesItemThenLikeIsRecordedForThatUserItemPair()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		
		engine.like(Alice, aerobics);
		
		assertThat(engine.getRating(Alice, aerobics), is(RATING.LIKE));		
	}
	
	@Test
	public void givenAnItemNotRatedByUserWhenUserDislikesItemThenDislikeIsRecordedForThatUserItemPair()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		Item basketball = new Item("basketball");
		
		engine.dislike(Alice, basketball);
		
		assertThat(engine.getRating(Alice, basketball), is(RATING.DISLIKE));
	}
	
	@Test
	public void givenItemLikedByUserWhenUserDislikesItemThenOnlyDislikeIsRecordedForThatUserItemPair()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");		
		engine.like(Alice, aerobics);
		
		engine.dislike(Alice, aerobics);
		
		assertThat(engine.getRating(Alice, aerobics), is(RATING.DISLIKE));		
	}
	
	@Test
	public void givenItemDislikedByUserWhenUserLikesItemThenOnlyLikeIsRecordedForThatUserItemPair()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");		
		engine.dislike(Alice, aerobics);
		
		engine.like(Alice, aerobics);
		
		assertThat(engine.getRating(Alice, aerobics), is(RATING.LIKE));		
	}
	
	@Test
	public void givenAUserWhoLikesOrDislikesAnItemWhenTheUserUnratesThatItemThenNoRatingExistsForThatUserItemPair()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		engine.like(Alice, aerobics);
		engine.dislike(Alice, basketball);
		
		engine.removeRating(Alice, aerobics);
		engine.removeRating(Alice, basketball);
		
		assertThat(engine.getRating(Alice, aerobics), is(RATING.UNSPECIFIED));
		assertThat(engine.getRating(Alice, basketball), is(RATING.UNSPECIFIED));
	}
	
	@Test
	public void givenNoItemsExistWhenAnyUserAsksForRecommendationsThenNoRecommendationsAreGiven() 
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		engine.addUsers(Alice, Bob);
		
		assertThat(engine.getRecommendations(Alice), is(empty()));
		assertThat(engine.getRecommendations(Bob),is(empty()));	
	}
	
	@Test
	public void givenOnlyOneUserExistsWhenThatUserAsksForRecommendationsThenNoRecommendationsAreGiven()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		engine.addUsers(Alice);
		engine.addItems(aerobics, basketball);
		
		assertThat(engine.getRecommendations(Alice), is(empty()));	
	}
	
	@Test
	public void givenAUserWhoHasNotRatedAnyItemsWhenThatUserAsksForRecommendationsThenNoRecommendationsAreGiven()
	{		
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		engine.addUsers(Alice, Bob);
		engine.addItems(aerobics, basketball);
		
		engine.like(Bob, basketball);
		
		assertThat(engine.getRecommendations(Alice), is(empty()));		
	}

	@Test
	public void givenAUserWhoHasRatedSomeItemsWhenThatUserAsksForRecommendationsThenItemsNotYetRatedByUserAndLikedByAllSimilarUsersAreReturned()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball, curling);
		
		engine.like(Alice, aerobics);
		
		engine.like(Bob, aerobics);
		engine.like(Bob, basketball);
		
		engine.like(Cory, aerobics);		
		engine.like(Cory, curling);
		
		assertThat(engine.getRecommendations(Alice), contains(basketball, curling));			
	}

	@Test	
	public void givenAUserWithTwoSimilarUsersWhenThatUserAsksForRecommendationThenRecommendationsExcludeItemsDislikedByEitherUser()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball, curling);
		
		engine.like(Alice, aerobics);
		
		engine.like(Bob, aerobics);
		engine.dislike(Bob, basketball);
		
		engine.like(Cory, aerobics);		
		engine.dislike(Cory, curling);
		
		assertThat(engine.getRecommendations(Alice), not(hasItems(basketball, curling)));	
	}

	@Test
	public void givenAUserWithTwoDissimilarUsersWhenThatUserAsksForRecommendationsThenRecommendationsExcludeItemsLikedByEitherUser()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball, curling);
		
		engine.like(Alice, aerobics);
		
		engine.dislike(Bob, aerobics);
		engine.like(Bob, basketball);
		
		engine.dislike(Cory, aerobics);		
		engine.like(Cory, curling);
		
		assertThat(engine.getRecommendations(Alice), not(hasItems(basketball, curling)));		
	}
	
	@Test
	public void givenAUserWithTwoDissimilarUsersWhenThatUserAsksForRecommendationsThenRecommendationsIncludeItemsDislikedByEitherUser()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball, curling);
		
		engine.like(Alice, aerobics);
		
		engine.dislike(Bob, aerobics);
		engine.dislike(Bob, basketball);
		
		engine.dislike(Cory, aerobics);		
		engine.dislike(Cory, curling);
		
		assertThat(engine.getRecommendations(Alice), hasItems(basketball, curling));				
	}

	@Test
	public void givenAUserWithOneSimilarAndOneDissimilarUserWhenThatUserAsksForRecommendationsThenRecommendationsExcludeItemsLikedByBothUsers()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball);
		
		engine.like(Alice, aerobics);
		
		engine.like(Bob, aerobics);
		engine.like(Bob, basketball);
		
		engine.dislike(Cory, aerobics);		
		engine.like(Cory, basketball);
		
		assertThat(engine.getRecommendations(Alice), not(hasItem(basketball)));
	}

	@Test
	public void givenAUserWithOneSimilarAndOneDissimilarUserWhenThatUserAsksForRecommendationsThenRecommendationsExcludeItemsDislikedByBothUsers()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball);
		
		engine.like(Alice, aerobics);
		
		engine.like(Bob, aerobics);
		engine.dislike(Bob, basketball);
		
		engine.dislike(Cory, aerobics);		
		engine.dislike(Cory, basketball);
		
		assertThat(engine.getRecommendations(Alice), not(hasItem(basketball)));		
	}
	
	@Test
	public void givenAUserWithOneSimilarUserWhoLikesAnItemAndALessSimilarUserWhoDislikesTheItemWhenThatUserAsksForRecommendationsThenTheItemIsRecommended()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball, curling);
		
		engine.like(Alice, aerobics);
		engine.like(Alice, basketball);
		
		engine.like(Bob, aerobics);
		engine.like(Bob, basketball);
		engine.like(Bob, curling);
		
		engine.like(Cory, aerobics);		
		engine.dislike(Cory, basketball);
		engine.dislike(Cory, curling);		
		
		assertThat(engine.getRecommendations(Alice), hasItem(curling));		
	}

	@Test
	public void givenAUserWithOneSimilarUserWhoDislikesAnItemAndALessSimilarUserWhoLikesTheItemWhenThatUserAsksForRecommendationsThenTheItemIsNotRecommended()
	{
		Engine engine = new Engine();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		User Cory = new User("Cory");
		Item aerobics = new Item("aerobics");
		Item basketball = new Item("basketball");
		Item curling = new Item("curling");
		engine.addUsers(Alice, Bob, Cory);
		engine.addItems(aerobics, basketball, curling);
		
		engine.like(Alice, aerobics);
		engine.like(Alice, basketball);
		
		engine.like(Bob, aerobics);
		engine.like(Bob, basketball);
		engine.dislike(Bob, curling);
		
		engine.like(Cory, aerobics);		
		engine.dislike(Cory, basketball);
		engine.like(Cory, curling);		
		
		assertThat(engine.getRecommendations(Alice), not(hasItem(curling)));	
	}
}

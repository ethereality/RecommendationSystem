package com.recommender;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import recommender.main.Item;
import recommender.main.Ratings;
import recommender.main.Ratings.RATING;
import recommender.main.User;

public class RatingsTest {
	
	@Rule 
	public ExpectedException exceptionThrown = ExpectedException.none();
	
	@Test
	public void givenAUserAndAnItemThenTheUserCanHaveNoRatingForThatItem()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics));
		
		assertThat(ratings.itemsRatedByUser(Alice), is(empty()));
		assertThat(ratings.itemsNotRatedByUser(Alice, allItems), contains(aerobics));
		assertThat(ratings.get(Alice, aerobics), is(RATING.UNSPECIFIED));
	}
	
	@Test
	public void givenAUserWhoHasNotRatedAnItemThenTheUserCanLikeThatItem()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.LIKE));		
		assertThat(ratings.likes(Alice), hasItem(aerobics));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.LIKE), contains(Alice));		
	}
	
	@Test
	public void givenAUserWhoHasNotRatedAnItemThenTheUserCanDislikeThatItem()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		
		ratings.add(Alice, aerobics, RATING.DISLIKE);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.DISLIKE));	
		assertThat(ratings.dislikes(Alice), hasItem(aerobics));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.DISLIKE), contains(Alice));		
	}	
	
	@Test
	public void givenMultipleUsersRateTheSameItemThenTheirRatingsDoNotConflictWithEachOther()
	{		
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		User Bob = new User("Bob");
		Item aerobics = new Item("aerobics");
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		ratings.add(Bob, aerobics, RATING.DISLIKE);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.LIKE));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.LIKE), contains(Alice));
		
		assertThat(ratings.get(Bob, aerobics), is(RATING.DISLIKE));	
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.DISLIKE), contains(Bob));
	}

	@Test
	public void givenAUserHasLikedAnItemWhenTheUserDislikesThatItemThenThatItemIsDislikedByTheUser()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		ratings.add(Alice, aerobics, RATING.LIKE);
		
		ratings.add(Alice, aerobics, RATING.DISLIKE);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.DISLIKE));
		assertThat(ratings.dislikes(Alice), contains(aerobics));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.DISLIKE), contains(Alice));
	}
	
	@Test
	public void givenAUserHasDislikedAnItemWhenTheUserLikesThatItemThenThatItemIsLikedByTheUser()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		ratings.add(Alice, aerobics, RATING.DISLIKE);
		
		ratings.add(Alice, aerobics, RATING.LIKE);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.LIKE));
		assertThat(ratings.likes(Alice), contains(aerobics));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.LIKE), contains(Alice));
	}
	
	@Test
	public void givenAUserHasLikedAnItemWhenTheUserUnratesThatItemThenNoRatingExistsForThatUserItemPair()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics));
		ratings.add(Alice, aerobics, RATING.LIKE);		
		
		ratings.remove(Alice, aerobics);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.UNSPECIFIED));
		assertThat(ratings.itemsRatedByUser(Alice), is(empty()));
		assertThat(ratings.itemsNotRatedByUser(Alice, allItems), hasItem(aerobics));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.LIKE), not(hasItem(Alice)));
		
		exceptionThrown.expect(UnsupportedOperationException.class);
		ratings.usersWhoRatedItem(aerobics, RATING.UNSPECIFIED);
	}
	
	@Test
	public void givenAUserHasDislikedAnItemWhenTheUserUnratesThatItemThenNoRatingExistsForThatUserItemPair()
	{
		Ratings ratings = new Ratings();
		User Alice = new User("Alice");
		Item aerobics = new Item("aerobics");
		Set<Item> allItems = new HashSet<>(Arrays.asList(aerobics));		
		ratings.add(Alice, aerobics, RATING.DISLIKE);		
		
		ratings.remove(Alice, aerobics);
		
		assertThat(ratings.get(Alice, aerobics), is(RATING.UNSPECIFIED));	
		assertThat(ratings.itemsRatedByUser(Alice), is(empty()));
		assertThat(ratings.itemsNotRatedByUser(Alice, allItems), hasItem(aerobics));
		assertThat(ratings.usersWhoRatedItem(aerobics, RATING.DISLIKE), not(hasItem(Alice)));
		
		exceptionThrown.expect(UnsupportedOperationException.class);
		ratings.usersWhoRatedItem(aerobics, RATING.UNSPECIFIED);
	}
}

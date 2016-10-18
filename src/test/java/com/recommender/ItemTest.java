package com.recommender;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;
import recommender.main.Item;

public class ItemTest {
	
	@Test
	public void anItemIsEqualToItself()
	{
		Item item = new Item("item");
		
		assertThat(item, is(equalTo(item)));
	}

	@Test
	public void twoItemsAreEqualIfTheyHaveSameName()
	{
		Item item1 = new Item("item");
		Item item2 = new Item("item");

		assertThat(item1, is(equalTo(item2)));
	}
	
	@Test
	public void twoItemsAreNotEqualIfTheyDoNotHaveSameID()
	{
		Item item1 = new Item("item1");
		Item item2 = new Item("item2");

		assertThat(item1, is(not(equalTo(item2))));
	}
	
	@Test
	public void hashRemainsSameIfItemDoesNotChange()
	{
		Item item = new Item("item");
		
		int firstHash = item.hashCode();
		int secondHash = item.hashCode();
		int thirdHash = item.hashCode();
		
		assertThat(firstHash, is(equalTo(secondHash)));
		assertThat(firstHash, is(equalTo(thirdHash)));
	}
	
	@Test
	public void hashOfTwoEqualItemsIsSame()
	{
		Item firstItem = new Item("some name");
		Item secondItem = new Item("some name");
		
		assertThat(firstItem, is(equalTo(secondItem)));
		assertThat(firstItem.hashCode(), is(equalTo(secondItem.hashCode())));
	}
}

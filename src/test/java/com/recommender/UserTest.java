package com.recommender;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import recommender.main.User;

public class UserTest {
	
	@Test
	public void aUserIsEqualToHerself()
	{
		User Alice = new User("Alice");
		
		assertThat(Alice, is(equalTo(Alice)));
	}

	@Test
	public void twoUsersAreEqualIfTheyHaveSameName()
	{
		User Alice = new User("Alice");
		User anotherAlice = new User("Alice");

		assertThat(Alice, is(equalTo(anotherAlice)));
	}
	
	@Test
	public void twoUsersAreNotEqualIfTheyDoNotHaveSameName()
	{
		User Alice = new User("Alice");
		User Bob = new User("Bob");

		assertThat(Alice, is(not(equalTo(Bob))));
	}	
	
	@Test
	public void hashRemainsSameIfUserDoesNotChange()
	{
		User Alice = new User("Alice");
		
		int firstHash = Alice.hashCode();
		int secondHash = Alice.hashCode();
		int thirdHash = Alice.hashCode();
		
		assertThat(firstHash, is(equalTo(secondHash)));
		assertThat(firstHash, is(equalTo(thirdHash)));
	}
	
	@Test
	public void hashOfTwoEqualUsersIsSame()
	{
		User firstUser = new User("Alice");
		User secondUser = new User("Alice");
		
		assertThat(firstUser, is(equalTo(secondUser)));
		assertThat(firstUser.hashCode(), is(equalTo(secondUser.hashCode())));
	}
}

package recommender.main;

import java.util.Objects;

public class User {

	private String name;
	
	public User(String name) 
	{
		this.name = name;
	}
	
	@Override
	public boolean equals(Object toCompare)
	{
		if(!(toCompare instanceof User)) {
			return false;
		}
		
		User otherUser = (User) toCompare;
		return otherUser.name.equals(this.name);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	@Override
	public String toString()
	{
		return name;
	}

}

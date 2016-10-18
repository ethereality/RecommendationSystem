package recommender.main;

import java.util.Objects;

public class Item {

	private String name;
	
	public Item(String name)
	{
		this.name = name;
	}
	
	@Override
	public boolean equals(Object toCompare)
	{
		if(!(toCompare instanceof Item)) {
			return false;
		}
		
		Item otherItem = (Item)toCompare;		
		return otherItem.name.equals(this.name);
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

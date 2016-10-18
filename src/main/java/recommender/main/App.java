package recommender.main;

/**
 * A class that demonstrates the application's functionality with an end-to-end test.
 *
 */
public class App 
{	
    public static void main( String[] args )
    {  	
    	Engine engine = new Engine();

    	User Alice = new User("Alice");
    	User John = new User("John");
    	User Mary = new User("Mary");
    	engine.addUsers(Alice, John, Mary);
    	
    	Item coffee = new Item("coffee");
    	Item coding = new Item("coding");
    	Item reading = new Item("reading");
    	Item running = new Item("running");
    	Item travelling = new Item("travelling");
    	engine.addItems(coffee, coding, reading, running, travelling);
    	
    	engine.like(Alice, running);
    	engine.like(Alice, travelling);
    	engine.dislike(Alice, coding);
    	
    	engine.like(John, travelling);
    	engine.like(John, coffee);
    	engine.like(John, coding);
    	engine.dislike(John, running);
    	
    	engine.like(Mary, reading);
    	engine.dislike(Mary, travelling);    	
    	
    	engine.getUsers().stream()
    		.forEach(u -> System.out.println(
    				"Recommendations for " + u + ":\n" + engine.getRecommendations(u)));
    }

}

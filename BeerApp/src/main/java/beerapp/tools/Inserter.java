package beerapp.tools;

import beerapp.dal.*;
import beerapp.model.*;

import java.sql.SQLException;
import java.util.List;


/**
 * main() runner, used for the app demo.
 * 
 * Instructions:
 * 1. Create a new MySQL schema and then run the CREATE TABLE statements from lecture:
 * http://goo.gl/86a11H.
 * 2. Update ConnectionManager with the correct user, password, and schema.
 */
public class Inserter {

	public static void main(String[] args) throws SQLException {
		// DAO instances.
		PersonsDao personsDao = PersonsDao.getInstance();
		AdministratorsDao administratorsDao = AdministratorsDao.getInstance();
		UsersDao usersDao = UsersDao.getInstance();
		
		BeerStylesDao beerStyleDao = BeerStylesDao.getInstance();
		FoodDao foodDao = FoodDao.getInstance();

		// INSERT objects from our model.
		Person person1 = new Person("b");
		person1 = personsDao.create(person1);
		Person person2 = new Person("b1");
		person2 = personsDao.create(person2);

		// READ.
		Person p1 = PersonsDao.getPersonFromUserName("b");
		System.out.format("Reading person: u:%s f:%s l:%s \n",
				p1.getUserName());

		// Update
		Person p3 = PersonsDao.updateUserName(p1, "b2");
		System.out.format("Updating Person: name:%s \n",
				p3.getUserName());

		// Delete.
		// PersonsDao.delete(p3);

		// INSERT objects from our model.
		Administrator admin1 = new Administrator("a");
		admin1 = administratorsDao.create(admin1);
		Administrator admin2 = new Administrator("a1");
		admin2 = administratorsDao.create(admin2);
		
		// READ.
		Administrator a1 = administratorsDao.getAdministratorFromUserName("a");
		System.out.format("Reading administrator: u:%s \n",
				a1.getUsername());
		
		// Update
		Administrator a3 = administratorsDao.updateUserName(a1, "a2");
		System.out.format("Updating administrators: name:%s \n",
				a3.getUsername());
		
		// Delete.
		// administratorsDao.delete(p3);
		
		// INSERT objects from our model.
		User user1 = new User("u");
		user1 = usersDao.create(user1);
		User user2 = new User("u1");
		user2 = usersDao.create(user2);
		
		// READ.
		User u1 = usersDao.getUserFromUserName("u");
		System.out.format("Reading user: u:%s \n",
				u1.getUsername());
		
		// Update.
		User u3 = usersDao.updateUserName(u1, "u2");
		System.out.format("Updating users: name:%s \n",
				u3.getUsername());
		
		// Delete.
		// usersDao.delete(p3);
		
		// Insert BeerStyle & Food
		BeerStyles beerStyle1 = new BeerStyles("BS1");
		beerStyle1 = beerStyleDao.create(beerStyle1);
		BeerStyles beerStyle2 = new BeerStyles("BS2");
		beerStyle2 = beerStyleDao.create(beerStyle2);
				
		Food food1 = new Food("f1", beerStyle1);
		food1 = foodDao.create(food1);
		Food food2 = new Food("f2", beerStyle2);
		food2 = foodDao.create(food2);
			
		// Read BeerStyles & Food
		BeerStyles bs1 = beerStyleDao.getBeerStyleByStyle("BS1");
		System.out.format("Reading beerStyle: s:%s \n",
			bs1.getStyle());
								
		List<Food> fList1 = foodDao.getFoodByStyle(beerStyle1);
		for (Food f : fList1) {
			System.out.format("Reading food: s:%s \n",
				f.getStyle().getStyle());
		}
				
		// Update BeerStyle & food
		BeerStyles bs2 = beerStyleDao.updateStyle(beerStyle1, "BS3");
		System.out.format("Updating beerStyle: s:%s \n",
			bs2.getStyle());

		Food f1 = foodDao.updateFoodName(food2, "f3");
		System.out.format("Updating food: f:%s \n",
			f1.getFoodName());
				
		// Delete BeerStyle & Food
		beerStyleDao.delete(beerStyle1);
		beerStyleDao.delete(beerStyle2);
		foodDao.delete(food1);
		foodDao.delete(food2);		
	}
  
  private static void mockBrewers() {
		BrewersDao brewersDao = BrewersDao.getInstance();
    
		// INSERT objects from our model.
		Brewer brewer1 = new Brewer(1234);
		brewer1 = brewersDao.create(brewer1);
		Brewer brewer2 = new Brewer(5678);
		brewer2 = brewersDao.create(brewer2);

		// READ.
		Brewer b1 = brewersDao.getBrewerById(1234);
		System.out.println(b1.getBrewerId());

		// Update.
		Brewer b3 = brewersDao.updateBrewerId(b1, 5678);
		System.out.println(b3.getBrewerId());

		// Delete.
		brewersDao.delete(brewer1);
		brewersDao.delete(brewer2);
		brewersDao.delete(b1);
		brewersDao.delete(b3);
  }
}

package BeerApp.tools;

import BeerApp.dal.*;
import BeerApp.model.*;

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
		Persons person1 = new Persons("b");
		person1 = personsDao.create(person1);
		Persons person2 = new Persons("b1");
		person2 = personsDao.create(person2);
		
		// READ.
		Persons p1 = personsDao.getPersonFromUserName("b");
		System.out.format("Reading person: u:%s f:%s l:%s \n",
				p1.getUserName());
		
		// Update
		Persons p3 = personsDao.updateUserName(p1, "b2");
		System.out.format("Updating persons: name:%s \n",
				p3.getUserName());
		
		// Delete.
//		personsDao.delete(p3);
		
		
		// INSERT objects from our model.
		Administrators admin1 = new Administrators("a");
		admin1 = administratorsDao.create(admin1);
		Administrators admin2 = new Administrators("a1");
		admin2 = administratorsDao.create(admin2);
		
		// READ.
		Administrators a1 = administratorsDao.getAdministratorFromUserName("a");
		System.out.format("Reading administrator: u:%s \n",
				a1.getUserName());
		
		// Update
		Administrators a3 = administratorsDao.updateUserName(a1, "a2");
		System.out.format("Updating administrators: name:%s \n",
				a3.getUserName());
		
		// Delete.
		// administratorsDao.delete(p3);
		
		// INSERT objects from our model.
		Users user1 = new Users("u");
		user1 = usersDao.create(user1);
		Users user2 = new Users("u1");
		user2 = usersDao.create(user2);
		
		// READ.
		Users u1 = usersDao.getUserFromUserName("u");
		System.out.format("Reading user: u:%s \n",
				u1.getUserName());
		
		// Update
		Users u3 = usersDao.updateUserName(u1, "u2");
		System.out.format("Updating users: name:%s \n",
				u3.getUserName());
		
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
}

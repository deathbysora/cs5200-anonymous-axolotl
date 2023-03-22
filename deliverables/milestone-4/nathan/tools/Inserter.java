package BeerApp.tools;

import BeerApp.dal.*;
import BeerApp.model.*;
import org.junit.Assert;
import java.sql.SQLException;


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
		BrewersDao brewersDao = BrewersDao.getInstance();

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
		// personsDao.delete(p3);

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
		
		// Update.
		Users u3 = usersDao.updateUserName(u1, "u2");
		System.out.format("Updating users: name:%s \n",
				u3.getUserName());
		
		// Delete.
		// usersDao.delete(p3);

		// INSERT objects from our model.
		Brewers brewer1 = new Brewers(1234);
		brewer1 = brewersDao.create(brewer1);
		Brewers brewer2 = new Brewers(5678);
		brewer2 = brewersDao.create(brewer2);

		// READ.
		Brewers b1 = brewersDao.getBrewerById(1234);
		Assert.assertEquals(b1.getBrewerId(), 1234);

		// Update.
		Brewers b3 = brewersDao.updateBrewerId(b1, 5678);
		Assert.assertEquals(b3.getBrewerId(), 5678);

		// Delete.
		// brewersDao.delete(b3);
	}
}

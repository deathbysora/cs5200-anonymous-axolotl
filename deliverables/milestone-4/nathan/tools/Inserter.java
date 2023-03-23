package BeerApp.tools;

import BeerApp.dal.*;
import BeerApp.model.*;
import org.junit.Assert;
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
		BeerCommentDao beerCommentDao = BeerCommentDao.getInstance();
		ViewHistoryDao viewHistoryDao = ViewHistoryDao.getInstance();

		Date date = new Date();
		
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

		BeerComment bc = new BeerComment("Really good!", date, beerReview, person1);
		bc = beerCommentDao.create(bc);

		BeerComment bc1 = beerCommentDao.getBeerCommentFromCommentId(1);
		System.out.format("Reading BeerComment: id:%d content:%s created:%s reviewid:%d username:%s \n",
				bc1.getCommentId(), bc1.getContent().toString(), bc1.getCreated().toString(), bc1.getBeerReview().getReviewId(), 
				bc1.getPerson().getUserName());

		List<BeerComment> bc2 = beerCommentDao.getBeerCommentArrayForPerson(person1);
		for (BeerComment b : bc2) {
			System.out.format("Reading BeerComment: id:%d content:%s created:%s reviewid:%d username:%s \n",
				b.getCommentId(), b.getContent().toString(), b.getCreated().toString(), b.getBeerReview().getReviewId(), 
				b.getPerson().getUserName());
		}

		List<BeerComment> bc3 = beerCommentDao.getBeerCommentArrayForReview(beerReview);
		for (BeerComment b : bc3) {
			System.out.format("Reading BeerComment: id:%d content:%s created:%s reviewid:%d username:%s \n",
				b.getCommentId(), b.getContent().toString(), b.getCreated().toString(), b.getBeerReview().getReviewId(), 
				b.getPerson().getUserName());
		}

		BeerComment b4 = beerCommentDao.updateContent(bc, "Light and crisp");
		System.out.format("Updated content:%s \n",
				b4.getContent());

		//Delete
		// beerCommentDao.delete(bc1);

		ViewHistory vh = new ViewHistory(date, person1, beer);
		vh = viewHistoryDao.create(vh);

		ViewHistory vh1 = viewHistoryDao.getViewHistoryByViewId(1);
		System.out.format("Reading ViewHistory: id:%d created:%s username:%s beerid:%s \n",
				vh1.getViewId(), vh1.getCreated().toString(), vh1.getBeer().getBeerId().toString(), 
				vh1.getPerson().getUserName());

		List<ViewHistory> vh2 = viewHistoryDao.getViewHistoryArrayForPerson(person1);
		for (ViewHistory v : vh2) {
			System.out.format("Reading ViewHistory: id:%d created:%s username:%s beerid:%d \n",
				v.getViewId(), v.getCreated().toString(), v.getBeer().getBeerId(), 
				v.getPerson().getUserName());
		}
		List<ViewHistory> vh3 = viewHistoryDao.getViewHistoryArrayForBeer(Beer beer);
		for (ViewHistory v : vh3) {
			System.out.format("Reading ViewHistory: id:%d created:%s username:%s beerid:%d \n",
				v.getViewId(), v.getCreated().toString(), v.getBeer().getBeerId().toString(), 
				v.getPerson().getUserName());
		}

		//Delete
		// viewHistoryDao.delete(bc1);
		
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
		brewersDao.delete(brewer1);
		brewersDao.delete(brewer2);
		brewersDao.delete(b1);
		brewersDao.delete(b3);
  }
}

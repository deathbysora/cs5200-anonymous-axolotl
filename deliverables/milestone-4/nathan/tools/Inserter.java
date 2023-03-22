package BeerApp.tools;

import BeerApp.dal.*;
import BeerApp.model.*;

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
		BeerCommentDao beerCommentDao = BeerCommentDao.getInstance();
		ViewHistoryDao viewHistoryDao = ViewHistoryDao.getInstance();

		Date date = new Date();

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
	}
}

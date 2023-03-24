package beerapp.tools;

import beerapp.dal.AdministratorsDao;
import beerapp.dal.BeerStylesDao;
import beerapp.dal.BeersDao;
import beerapp.dal.BrewersDao;
import beerapp.dal.FoodDao;
import beerapp.dal.PersonsDao;
import beerapp.dal.UsersDao;
import beerapp.model.Administrator;
import beerapp.model.Beer;
import beerapp.model.BeerStyle;
import beerapp.model.Brewer;
import beerapp.model.Food;
import beerapp.model.Person;
import beerapp.model.User;
import java.sql.SQLException;
import java.util.List;

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

        // READ.
        Person p1 = personsDao.getPersonByUsername("b");
        System.out.format("Reading person: %s\n", p1.getUsername());

        // Update
        Person p3 = personsDao.updateUsername(p1, "b2");
        System.out.format("Updating Person: name:%s \n",
          p3.getUsername());

        // Delete.
        personsDao.delete(p3);

        // INSERT objects from our model.
        Administrator admin1 = new Administrator("a");
        admin1 = administratorsDao.create(admin1);

        // READ.
        Administrator a1 = administratorsDao.getAdministratorFromUserName("a");
        System.out.format("Reading administrator: %s \n",
          a1.getUsername());

        // Update
        Administrator a3 = administratorsDao.updateUserName(a1, "a2");
        System.out.format("Updating administrators: name:%s \n",
          a3.getUsername());

        // Delete.
        administratorsDao.delete(a3);

        // INSERT objects from our model.
        User user1 = new User("u");
        user1 = usersDao.create(user1);

        // READ.
        User u1 = usersDao.getUserFromUserName("u");
        System.out.format("Reading user: u:%s \n",
          u1.getUsername());

        // Update.
        User u2 = usersDao.updateUserName(u1, "u2");
        System.out.format("Updating users: name:%s \n",
          u2.getUsername());

        // Delete.
        usersDao.delete(u2);

        // Insert BeerStyle & Food
        BeerStyle beerStyle1 = new BeerStyle("BS1");
        beerStyle1 = beerStyleDao.create(beerStyle1);
        BeerStyle beerStyle2 = new BeerStyle("BS2");
        beerStyle2 = beerStyleDao.create(beerStyle2);

        Food food1 = new Food("f1", beerStyle1);
        food1 = foodDao.create(food1);
        Food food2 = new Food("f2", beerStyle2);
        food2 = foodDao.create(food2);

        // Read BeerStyles & Food
        BeerStyle bs1 = beerStyleDao.getBeerStyle("BS1");
        System.out.format("Reading beerStyle: s:%s \n",
          bs1.getStyle());

        List<Food> fList1 = foodDao.getFoodByStyle(beerStyle1);
        for (Food f : fList1) {
            System.out.format("Reading food: s:%s \n",
              f.getStyle().getStyle());
        }

        // Update BeerStyle & food
        BeerStyle bs2 = beerStyleDao.updateStyle(beerStyle1, "BS3");
        System.out.format("Updating beerStyle: s:%s \n",
          bs2.getStyle());

        Food f1 = foodDao.updateFoodName(food2, "f3");
        System.out.format("Updating food: f:%s \n",
          f1.getFoodName());

        // Delete BeerStyle & Food
        foodDao.getFoodByName("f1").forEach((f) -> foodDao.delete(f));
        foodDao.getFoodByName("f3").forEach((f) -> foodDao.delete(f));
        beerStyleDao.delete(beerStyleDao.getBeerStyle("bs2"));
        beerStyleDao.delete(beerStyleDao.getBeerStyle("bs3"));

        mockBrewers();
        mockFindSimilarBeers();
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

    private static void mockFindSimilarBeers() {
        BeersDao beersDao = BeersDao.getInstance();
        Beer beer = new Beer(2, "Abeta Amber Lager", Float.valueOf(5), 1, new BeerStyle("Vienna"));

        beersDao.getSimilarBeers(beer).forEach((b) -> System.out.println(b.getBeer()));
    }
}
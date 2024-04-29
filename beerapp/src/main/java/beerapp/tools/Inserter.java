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

public class Inserter {

    public static void main(String[] args) throws SQLException {
       mockApp();
       mockBrewers();
       mockFindSimilarBeers();
    }

    private static void mockApp() throws SQLException {
        // DAO instances.
        PersonsDao personsDao = PersonsDao.getInstance();
        AdministratorsDao administratorsDao = AdministratorsDao.getInstance();
        UsersDao usersDao = UsersDao.getInstance();

        BeerStylesDao beerStyleDao = BeerStylesDao.getInstance();
        FoodDao foodDao = FoodDao.getInstance();

        // INSERT objects from our model.
        Person person1 = new Person("nathan");
        person1 = personsDao.create(person1);

        // READ.
        Person p1 = personsDao.getPersonByUsername("nathan");
        System.out.format("Created and Reading person: %s\n", p1.getUsername());

        // Update
        Person p3 = personsDao.updateUsername(p1, "nathanUpdated");
        System.out.format("Updating Person: name:%s \n",
          p3.getUsername());

        // Delete.
        System.out.println("Successfully deleted Person nathanUpdated.");
        personsDao.delete(p3);

        // Spacing
        System.out.println();

        // INSERT objects from our model.
        Administrator admin1 = new Administrator("elsaTa");
        admin1 = administratorsDao.create(admin1);

        // READ.
        Administrator a1 = administratorsDao.getAdministratorFromUserName("elsaTa");
        System.out.format("Created and reading administrator: %s \n",
          a1.getUsername());

        // Update
        Administrator a3 = administratorsDao.updateUserName(a1, "panXu1");
        System.out.format("Updating administrators from name elsaTa to :%s \n",
          a3.getUsername());

        // Delete.
        System.out.println("Successfully deleted the administrator panXu1");
        administratorsDao.delete(a3);

        // Spacing
        System.out.println();

        // INSERT objects from our model.
        User user1 = new User("nathanY56");
        user1 = usersDao.create(user1);

        User user2 = new User("deathBySora");
        user2 = usersDao.create(user2);

        // READ.
        User u1 = usersDao.getUserFromUserName("nathanY56");
        System.out.format("Successfully created and found user1: u:%s using the .getUserFromUserName() method\n",
          u1.getUsername());

        User u2 = usersDao.getUserFromUserName("deathBySora");
        System.out.format("Successfully created and found user2: u:%s using the .getUserFromUserName() method \n",
          u2.getUsername());

        // Update.
        User u3 = usersDao.updateUserName(u1, "newNathanY56");
        System.out.format("Updating users: from nathanY56 to new name:%s \n",
          u3.getUsername());

        // Delete.
        System.out.format("Successfully deleted deathbysora \n");
        usersDao.delete(usersDao.getUserFromUserName("deathbysora"));
        System.out.format("Successfully deleted " + u2.getUsername() + "\n");
        usersDao.delete(u3);

        // Spacing
        System.out.println();


        // Insert BeerStyle & Food
        BeerStyle beerStyle1 = new BeerStyle("smaller");
        beerStyle1 = beerStyleDao.create(beerStyle1);
        BeerStyle beerStyle2 = new BeerStyle("Larger");
        beerStyle2 = beerStyleDao.create(beerStyle2);

        Food food1 = new Food("Pasta", beerStyle1);
        food1 = foodDao.create(food1);
        Food food2 = new Food("Pizza", beerStyle2);
        food2 = foodDao.create(food2);

        // Read BeerStyles & Food
        BeerStyle bs1 = beerStyleDao.getBeerStyle("smaller");
        System.out.format("Successfully created and reading beerStyle: s:%s \n",
          bs1.getStyle());

        BeerStyle bs2 = beerStyleDao.getBeerStyle("Larger");
        System.out.format("Successfully created and reading beerStyle: s:%s \n",
          bs2.getStyle());

        // Update BeerStyle & food
        BeerStyle bs3 = beerStyleDao.updateStyle(beerStyle1, "IPA");
        System.out.format("Updating beerStyle1 to new beerStyle3: s:%s \n",
          bs3.getStyle());

        Food f1 = foodDao.updateFoodName(food2, "Fries");
        System.out.format("Updating food: from pizza to f:%s \n",
          f1.getFoodName());

        beerStyleDao.delete(beerStyleDao.getBeerStyle("larger"));
        beerStyleDao.delete(beerStyleDao.getBeerStyle("ipa"));
        // Delete BeerStyle & Food
        foodDao.getFoodByName("fries").forEach((f) -> foodDao.delete(f));
        foodDao.getFoodByName("pasta").forEach((f) -> foodDao.delete(f));
        foodDao.getFoodByName("pizza").forEach((f) -> foodDao.delete(f));
    }
    private static void mockBrewers() {
        System.out.println();
        BrewersDao brewersDao = BrewersDao.getInstance();

        // INSERT objects from our model.
        Brewer brewer1 = new Brewer(1234);
        brewer1 = brewersDao.create(brewer1);
        Brewer brewer2 = new Brewer(5678123);
        brewer2 = brewersDao.create(brewer2);

        // READ.
        Brewer b1 = brewersDao.getBrewerById(1234);
        System.out.println("Successfully created and read brewer: " + b1.getBrewerId());

        // Update.
        Brewer b3 = brewersDao.updateBrewerId(b1, 56781234);
        System.out.println("Successfully created and read brewer: " + b3.getBrewerId());

        // Delete.
        brewersDao.delete(brewer1);
        brewersDao.delete(brewer2);
        brewersDao.delete(b1);
        brewersDao.delete(b3);
        System.out.println();
    }

    private static void mockFindSimilarBeers() {
        BeersDao beersDao = BeersDao.getInstance();

        Beer beer = new Beer(2, "Abeta Amber Lager", Float.valueOf(5), 1, new BeerStyle("Vienna"));
        System.out.println("Find similarly rated beers for: " + beer.getName());
        beersDao.getSimilarBeers(beer).forEach((b) -> System.out.println("* " + b.getName()));
    }
}
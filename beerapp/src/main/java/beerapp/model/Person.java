package beerapp.model;

public class Person {

    protected String username;

    public Person(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Person{" +
          "userName='" + username + '\'' +
          '}';
    }
}
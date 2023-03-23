package beerapp.model;

public class Brewer {

    protected int brewerId;

    public Brewer(int brewerId) {
        this.brewerId = brewerId;
    }

    public int getBrewerId() {
        return brewerId;
    }

    @Override
    public String toString() {
        return "Brewer{" +
          "brewerId=" + brewerId +
          '}';
    }
}
package beerapp.model;

public class BeersCount{
  private String beerName;
  private int cnt;

  public BeersCount(String beerName, int cnt) {
    this.beerName = beerName;
    this.cnt = cnt;
  }

  public String getBeerName() {
    return this.beerName;
  }

  public int getCnt() {
    return this.cnt;
  }
}
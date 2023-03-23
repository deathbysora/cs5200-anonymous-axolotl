package beerapp.model;

public class Beer {

    protected Integer id;
    protected String name;
    protected Float abv;
    protected Integer brewerId;
    protected BeerStyle style;

    public Beer(Integer id, String name, Float abv, Integer brewerId, BeerStyle style) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.brewerId = brewerId;
        this.style = style;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Float getAbv() {
        return this.abv;
    }

    public Integer getBrewerId() {
        return this.brewerId;
    }

    public BeerStyle getBeerStyle() {
        return this.style;
    }

    @Override
    public String toString() {
        return "Beer{" +
          "id=" + id +
          ", name='" + name + '\'' +
          ", abv=" + abv +
          ", brewerId=" + brewerId +
          ", style=" + style +
          '}';
    }
}
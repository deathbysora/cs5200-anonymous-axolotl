package beerapp.model;

public class Food {

    protected String foodName;
    protected BeerStyle style;

    public Food(String foodName, BeerStyle style) {
        this.foodName = foodName;
        this.style = style;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BeerStyle getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "Food{" +
          "foodName='" + foodName + '\'' +
          ", style=" + style +
          '}';
    }
}
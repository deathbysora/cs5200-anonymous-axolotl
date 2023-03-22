package beerapp.model;

public class Food {
    protected String foodName;
    protected BeerStyles style;
    public Food(String foodName, BeerStyles style) {
        this.foodName = foodName;
        this.style = style;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BeerStyles getStyle() {
        return style;
    }

    public void setStyle(BeerStyles style) {
        this.style = style;
    }
}
package beerapp.model;

public class BeerStyle {

    protected String style;

    public BeerStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "BeerStyle{" +
          "style='" + style + '\'' +
          '}';
    }
}
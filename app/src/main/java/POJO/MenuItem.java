package POJO;

import java.util.List;

/**
 * Created by olile on 2018-03-17.
 */

public class MenuItem {
    private String itemName;
    private double price;
    private String description;
    private List<String> ingredients;

    public MenuItem(String name, double price){
        this.itemName = name;
        this.price = price;
    }

    public MenuItem(String name, double price, String desc){
        this.itemName = name;
        this.price = price;
        this.description = desc;
    }

    public MenuItem(String name, double price, String desc, List<String> ings){
        this.itemName = name;
        this.price = price;
        this.description = desc;
        this.ingredients = ings;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

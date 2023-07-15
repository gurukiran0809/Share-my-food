package project.sharemyfood;

/**
 * Created by admin on 18/01/2017.
 */

public class FoodObject {
    String id;
    String  user_id;
    String name;
    String food_type;
    String cuisine;
    String location;
    String quantity;
    String cooked_time;
    String validity;
    String imgsrc;

    public FoodObject(String id,String user_id,String name,String food_type,String cuisine,String location,String quantity,String cooked_time,String validity,String imgsrc)
    {
        this.id=id;
        this.user_id=user_id;
        this.name=name;
        this.food_type=food_type;
        this.cuisine=cuisine;
        this.location=location;
        this.quantity=quantity;
        this.cooked_time=cooked_time;
        this.validity=validity;
        this.imgsrc=imgsrc;

    }

    public String getCuisine() {
        return cuisine;
    }

    public String getCooked_time() {
        return cooked_time;
    }

    public String getFood_type() {
        return food_type;
    }

    public String getId() {
        return id;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getValidity() {
        return validity;
    }

    @Override
    public String toString() {
        return name;
    }
}

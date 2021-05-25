package Model;

import java.util.ArrayList;

public class Cart {
    private static ArrayList<CartItem> cart = null;
    private Cart(){

    }
    public static synchronized ArrayList<CartItem> getInstanceCart() {
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return (cart);
    }
}

package com.example.a2games;

public class ItemMenu {
    private String name;
    private final int imageResource;

    public ItemMenu(String s, int imageResource) {
        this.name = s;
        this.imageResource = imageResource;
    }

    public String getName(){
        return this.name;
    }

    public int getImageResource(){
        return this.imageResource;
    }
}

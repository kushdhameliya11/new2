package com.example.new2;


import java.util.List;

public class OuterItem {
    private String text;
    private List<Integer> innerItems;

    public OuterItem(String text, List<Integer> innerItems) {
        this.text = text;
        this.innerItems = innerItems;
    }

    public String getText() {
        return text;
    }

    public List<Integer> getInnerItems() {
        return innerItems;
    }
}


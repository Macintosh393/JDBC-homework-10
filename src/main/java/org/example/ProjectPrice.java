package org.example;

public class ProjectPrice {
    private String name;
    private long price;

    public ProjectPrice(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProjectPrice{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

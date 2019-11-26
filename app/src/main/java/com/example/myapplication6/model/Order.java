package com.example.myapplication6.model;

public class Order {
    private  String ProductId;
    private  String ProductName;
    private  String Quantity;
    private  String Price;
    private  String Discount;
    private  String Comment;


    public Order() {
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Order(String productId, String productName, String quantity, String price, String discount, String comment) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Comment=comment;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ProductId='" + ProductId + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Price='" + Price + '\'' +
                ", Discount='" + Discount + '\'' +
                '}';
    }
}

package com.example.auction.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Vehicle extends Item{
    public Vehicle(){
        setItemType(ItemType.ART);
    }
    public Vehicle(int id,int sellerId, String name,String description,BigDecimal startingPrice,ItemType itemType, LocalDateTime createAt){
        super(id,sellerId,name,description,startingPrice,ItemType.ART,createAt);
    }
    @Override
    public void printInfo(){
        System.out.println("Vehicle"+getName());
    }
}
package com.example.auction.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Electronics extends Item{
    public Electronics(){
        setItemType(ItemType.ELECTRONICS);
    }
    public Electronics(int id, int sellerId,String name,String description,BigDecimal startingPrice,ItemType itemType,LocalDateTime createAt){
        super(id,sellerId,name,description,startingPrice,ItemType.ELECTRONICS,createAt);
    }
    @Override
    public void printInfo(){
        System.out.println("Electronics"+getName());
    }

}
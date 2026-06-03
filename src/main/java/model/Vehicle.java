package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Vehicle extends Item{
    public Vehicle(){
        setItemType(ItemType.VEHICLE);
    }
    public Vehicle(int id,int sellerId, String name,String description,BigDecimal startingPrice, LocalDateTime createAt){
        super(id,sellerId,name,description,startingPrice,ItemType.VEHICLE,createAt);
    }
    @Override
    public void printInfo(){
        System.out.println("Vehicle"+getName());
    }
}
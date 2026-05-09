package model;
import java.math.BigDecimal;
import  java.time.LocalDateTime;
abstract class Item{
    private int id;
    private int sellerId;
    private String name;
    private String description;
    private BigDecimal startingPrice;
    private ItemType itemType;
    private  LocalDateTime createAt;
    public abstract void printInfo();
    public Item(){};
    public Item(int id, int sellerId, String name, String description, BigDecimal startingPrice, ItemType itemType, LocalDateTime createAt){
        this.id=id;
        this.sellerId=sellerId;
        this.name=name;
        this.description=description;
        this.itemType=itemType;
        this.startingPrice=startingPrice;
        this.createAt=createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStartingPrice(){
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }
    public ItemType getItemType(){
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
    public LocalDateTime getCreateAt(){
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt){
        this.createAt=createAt;
    }
}
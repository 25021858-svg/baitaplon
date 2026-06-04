package service;
import dao.ItemDao;
import model.Item;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private final ItemDao itemDao;

    public ItemService() {
        this.itemDao = new ItemDao();
    }

    public void addItem(Item item) throws SQLException {
        validateItem(item);
        itemDao.save(item);

    }

    public Item getItemByid(int id) throws SQLException {
        if (id < 0) {
            throw new IllegalArgumentException("id khong hopj le");
        }
        Item item = itemDao.findById(id);
        if (item == null) {
            throw new IllegalArgumentException("khong tim thay san pham");
        }
        return item;
    }
    public List<Item> gettAllitems() throws SQLException{
        return itemDao.findAll();
    }
    public List<Item> getItemBySellerId(int sellerId) throws SQLException{
        if(sellerId<0){
            throw new IllegalArgumentException("id nguoi dung khong hop le");
        }
        return itemDao.findBySellerId(sellerId);
    }
    public void updateItem(Item item) throws SQLException{
        if(item==null){
            throw new IllegalArgumentException("san pham khong duoc null");
        }
        if(item.getId()<0){
            throw new IllegalArgumentException("san phan khong hop le");

        }
        validateItem(item);
        Item existingItem=itemDao.findById(item.getId());
        if(existingItem==null){
            throw new IllegalArgumentException("khong tim thay san pham de cap nhat");

        }
        itemDao.update(item);
    }
    public void deleteItem(int id) throws SQLException{
        if(id<0){
            throw new IllegalArgumentException("id khong hop le");
        }
        Item existingItem=itemDao.findById(id);
        if(existingItem==null){
            throw new IllegalArgumentException("khong tim thay san pham");
        }
        itemDao.deleteById(id);

    }
    private void validateItem(Item item){
        if(item==null){
            throw new IllegalArgumentException("san pham khong duoc null");
        }
        if(item.getSellerId()<=0){
            throw new IllegalArgumentException("id seller khong hop le");

        }
        if(item.getName()==null){
            throw new IllegalArgumentException("ten sp khong duoc trong");

        }
        if (item.getDescription()==null){
            throw new IllegalArgumentException("mo ta khong duoc de trong");
        }
        if (item.getStartingPrice() == null ||
                item.getStartingPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("gia ban dau >0");
        }

        if (item.getItemType() == null) {
            throw new IllegalArgumentException("loai sp kh duoc de trong");
        }

        if (item.getCreatedAt() == null) {
            throw new IllegalArgumentException("time kh duoc de trong");
        }
    }
}
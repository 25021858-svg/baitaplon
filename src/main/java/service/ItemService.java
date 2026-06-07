package service;

import dao.ItemDao;
import model.Art;
import model.Electronics;
import model.Item;
import model.ItemType;
import model.Vehicle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    public void createItem(int sellerId, String name, String description,
                           BigDecimal startingPrice, String itemTypeText) throws SQLException {
        Item item = buildItem(
                0,
                sellerId,
                name,
                description,
                startingPrice,
                itemTypeText,
                LocalDateTime.now()
        );

        addItem(item);
    }

    public Item getItemById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("id khong hop le");
        }

        Item item = itemDao.findById(id);

        if (item == null) {
            throw new IllegalArgumentException("khong tim thay san pham");
        }

        return item;
    }

    public List<Item> getAllItems() throws SQLException {
        return itemDao.findAll();
    }

    public List<Item> getItemsBySellerId(int sellerId) throws SQLException {
        if (sellerId <= 0) {
            throw new IllegalArgumentException("id seller khong hop le");
        }

        return itemDao.findBySellerId(sellerId);
    }

    public void updateItem(Item item) throws SQLException {
        if (item == null) {
            throw new IllegalArgumentException("san pham khong duoc null");
        }

        if (item.getId() <= 0) {
            throw new IllegalArgumentException("id san pham khong hop le");
        }

        validateItem(item);

        Item existingItem = itemDao.findById(item.getId());

        if (existingItem == null) {
            throw new IllegalArgumentException("khong tim thay san pham de cap nhat");
        }

        itemDao.update(item);
    }

    public void updateItem(int itemId, String name, String description,
                           BigDecimal startingPrice, String itemTypeText) throws SQLException {
        if (itemId <= 0) {
            throw new IllegalArgumentException("id san pham khong hop le");
        }

        Item existingItem = itemDao.findById(itemId);

        if (existingItem == null) {
            throw new IllegalArgumentException("khong tim thay san pham de cap nhat");
        }

        Item updatedItem = buildItem(
                itemId,
                existingItem.getSellerId(),
                name,
                description,
                startingPrice,
                itemTypeText,
                existingItem.getCreatedAt()
        );

        updateItem(updatedItem);
    }

    public void deleteItem(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("id san pham khong hop le");
        }

        Item existingItem = itemDao.findById(id);

        if (existingItem == null) {
            throw new IllegalArgumentException("khong tim thay san pham");
        }

        itemDao.deleteById(id);
    }

    private Item buildItem(int id, int sellerId, String name, String description,
                           BigDecimal startingPrice, String itemTypeText,
                           LocalDateTime createdAt) {
        if (itemTypeText == null || itemTypeText.isBlank()) {
            throw new IllegalArgumentException("loai san pham khong duoc de trong");
        }

        ItemType itemType = ItemType.valueOf(itemTypeText.trim().toUpperCase());

        switch (itemType) {
            case ELECTRONICS:
                return new Electronics(id, sellerId, name, description, startingPrice, createdAt);

            case ART:
                return new Art(id, sellerId, name, description, startingPrice, createdAt);

            case VEHICLE:
                return new Vehicle(id, sellerId, name, description, startingPrice, createdAt);

            default:
                throw new IllegalArgumentException("loai san pham khong hop le");
        }
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("san pham khong duoc null");
        }

        if (item.getSellerId() <= 0) {
            throw new IllegalArgumentException("id seller khong hop le");
        }

        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("ten san pham khong duoc de trong");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new IllegalArgumentException("mo ta khong duoc de trong");
        }

        if (item.getStartingPrice() == null
                || item.getStartingPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("gia khoi diem phai lon hon 0");
        }

        if (item.getItemType() == null) {
            throw new IllegalArgumentException("loai san pham khong duoc de trong");
        }

        if (item.getCreatedAt() == null) {
            throw new IllegalArgumentException("thoi gian tao khong duoc de trong");
        }
    }
}
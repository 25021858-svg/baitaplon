import dao.ItemDao;
import model.Item;

import java.util.List;

public class TestItemDao {
    public static void main(String[] args) {
        try {
            ItemDao itemDao = new ItemDao();

            List<Item> items = itemDao.findAll();

            if (items.isEmpty()) {
                System.out.println("Khong co item nao trong database");
                return;
            }

            for (Item item : items) {
                System.out.println("ID: " + item.getId());
                System.out.println("Seller ID: " + item.getSellerId());
                System.out.println("Name: " + item.getName());
                System.out.println("Description: " + item.getDescription());
                System.out.println("Starting price: " + item.getStartingPrice());
                System.out.println("Type: " + item.getItemType());
                System.out.println("Created at: " + item.getCreatedAt());
                System.out.println("--------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
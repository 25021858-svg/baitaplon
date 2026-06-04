import com.example.auction.dao.UserDao;
import com.example.auction.model.User;

public class TestUserDao {
    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();

            User user = userDao.findByUsername("admin1");

            if (user == null) {
                System.out.println("Khong tim thay user");
            } else {
                System.out.println("Tim thay user:");
                System.out.println("ID: " + user.getId());
                System.out.println("Username: " + user.getUsername());
                System.out.println("Role: " + user.getRole());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
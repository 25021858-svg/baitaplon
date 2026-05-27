package service;
import dao.UserDao;
import model.Admin;
import model.Bidder;
import model.Role;
import model.Seller;
import model.User;

import java.sql.SQLException;

public class AuthService {
    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User register(String username, String password, Role role) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username khong duoc trong");
        }
        if(password==null|| password.trim().isEmpty()){
            throw new IllegalArgumentException(("password khong duoc de trong"));
        }
        if(role==null){
            throw new IllegalArgumentException("role khong duoc de trong");
        }
        if(userDao.existsByUsername(username)){
            throw new IllegalArgumentException("Username da ton tai");
        }
        User user = createUserByRole(0,username,password,role);
        userDao.save(user);
        return user;
    }
    public User login(String username,String password) throws SQLException{
        if ( username== null||username.trim().isEmpty()){
            throw new IllegalArgumentException("Username khong duoc de trong");
        }
        if (password == null||password.trim().isEmpty()){
            throw new IllegalArgumentException("password khong duoc de trong");
        }
        User user=userDao.findByUsername(username);
        if(user==null){
            throw new IllegalArgumentException("username khong ton tai");
        }
        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("sai mat khau");
        }
        return user;

    }
    private User createUserByRole(int id,String username,String password,Role role){
        if(role==Role.BIDDER){
            return new Bidder(id,username,password);
        }
        if(role==Role.SELLER){
            return new Seller(id,username,password);
        }
        if (role==Role.ADMIN){
            return new Admin(id,username,password);
        }
        throw new IllegalArgumentException("Role khong hop le");
    }
}


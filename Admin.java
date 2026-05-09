package model;
public class Admin extends User{
    public Admin(){
        setRole(Role.ADMIN);
    }
    public Admin(int id,String username,String password){
        super(id,username,password,Role.ADMIN);
    }
    public void printInfo(){
        System.out.println("Admin"+username);
    }
}
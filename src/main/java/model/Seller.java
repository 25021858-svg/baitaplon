package model;
public class Seller extends User{
    public Seller(){
        setRole(Role.SELLER);
    }
    public Seller(int id,String username,String password){
        super(id,username,password,Role.SELLER);
    }
    public void printInfo(){
        System.out.println("Seller"+getUsername());
    }
}
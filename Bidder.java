package model;
public class Bidder extends User{
    public Bidder(){
        setRole(Role.BIDDER);
    }
    public Bidder(int id, String username,String password){
        super(id,username,password,Role.SELLER);
    }
    @Override
    public void printInfo(){
        System.out.println("Bidder"+getUsername());
    }

}
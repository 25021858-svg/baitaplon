package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Auction{
    private int id;
    private int itemId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private Integer currentWinnerId;
    private AuctionStatus status;
    private LocalDateTime createdAt;
    public Auction(){}
    public Auction(int id,int itemId, LocalDateTime startTime,LocalDateTime endTime,BigDecimal currentPrice,Integer currentWinnerId,AuctionStatus status,LocalDateTime createdAt){
        this.id=id;
        this.itemId=itemId;
        this.startTime=startTime;
        this.endTime=endTime;
        this.currentPrice=currentPrice;
        this.currentWinnerId=currentWinnerId;
        this.status=status;
        this.createdAt=createdAt;
    }
    public boolean isRunning(){
        return status==AuctionStatus.RUNNING;
    }
    public boolean isFinished(){
        return status== AuctionStatus.FINISHED|| status==AuctionStatus.PAID|| status==AuctionStatus.CANCELED;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }


    public Integer getCurrentWinnerId() {
        return currentWinnerId;
    }

    public void setCurrentWinnerId(Integer currentWinnerId) {
        this.currentWinnerId = currentWinnerId;
    }


    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


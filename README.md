Auction System - Bài tập lớn Lập trình nâng cao
1. Thông tin sinh viên
Họ và tên: Trương Xuân Lộc
Mã sinh viên: 25021858
Môn học: Lập trình nâng cao (LTNC)
Đề tài: Hệ thống đấu giá trực tuyến
2. Giới thiệu bài toán

Project xây dựng một hệ thống đấu giá trực tuyến cơ bản với ba vai trò chính:

Seller: tạo, xem, sửa và xóa sản phẩm.
Admin: xem sản phẩm của seller, tạo phiên đấu giá và quản lý trạng thái đấu giá.
Bidder: xem danh sách phiên đấu giá, mở chi tiết phiên đấu giá và đặt giá.

Luồng hoạt động chính của hệ thống:

Seller tạo sản phẩm
→ Admin xem sản phẩm và tạo phiên đấu giá
→ Admin start auction
→ Bidder tham gia đặt giá
→ Hệ thống cập nhật giá hiện tại và người đang thắng

Mục tiêu của project là áp dụng kiến thức lập trình hướng đối tượng, mô hình Client-Server, JavaFX GUI, kết nối database SQLite, xử lý nghiệp vụ đấu giá và kiểm thử bằng JUnit.

3. Công nghệ sử dụng
Ngôn ngữ lập trình: Java 17
Giao diện: JavaFX, FXML, CSS
Build tool: Maven
Database: SQLite
Giao tiếp Client-Server: Java Socket
Định dạng dữ liệu truyền giữa client và server: JSON
Thư viện JSON: Gson
Testing: JUnit 5
IDE khuyến nghị: IntelliJ IDEA
4. Yêu cầu môi trường

Cần cài đặt:

Java JDK 17 hoặc cao hơn
Maven
Git

Kiểm tra môi trường:

java -version
mvn -version
git --version
5. Cấu trúc thư mục
baitaplon
├── pom.xml
├── README.md
├── database
│   ├── schema.sql
│   └── seed.sql
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── app
│   │   │   │   └── Main.java
│   │   │   ├── client
│   │   │   │   ├── AuctionClient.java
│   │   │   │   └── TestAuctionClient.java
│   │   │   ├── controller
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── RegisterController.java
│   │   │   │   ├── AuctionListController.java
│   │   │   │   ├── AuctionDetailController.java
│   │   │   │   ├── SellerProductManagementController.java
│   │   │   │   ├── AdminDashboardController.java
│   │   │   │   └── UserSession.java
│   │   │   ├── dao
│   │   │   │   ├── DatabaseConnection.java
│   │   │   │   ├── UserDao.java
│   │   │   │   ├── ItemDao.java
│   │   │   │   ├── AuctionDao.java
│   │   │   │   └── BidDao.java
│   │   │   ├── exception
│   │   │   ├── model
│   │   │   │   ├── User.java
│   │   │   │   ├── Bidder.java
│   │   │   │   ├── Seller.java
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Item.java
│   │   │   │   ├── Electronics.java
│   │   │   │   ├── Art.java
│   │   │   │   ├── Vehicle.java
│   │   │   │   ├── Auction.java
│   │   │   │   └── BidTransaction.java
│   │   │   ├── server
│   │   │   │   ├── AuctionServer.java
│   │   │   │   ├── ClientHandler.java
│   │   │   │   ├── Request.java
│   │   │   │   ├── RequestHandler.java
│   │   │   │   └── Response.java
│   │   │   ├── service
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── ItemService.java
│   │   │   │   ├── AuctionService.java
│   │   │   │   └── BidService.java
│   │   │   └── util
│   │   │       └── SceneManager.java
│   │   └── resources
│   │       ├── view
│   │       │   ├── login.fxml
│   │       │   ├── register.fxml
│   │       │   ├── auction-list.fxml
│   │       │   ├── auction-detail.fxml
│   │       │   ├── seller-product-management.fxml
│   │       │   └── admin-dashboard.fxml
│   │       └── css
│   │           └── style.css
│   └── test
│       └── java
6. Kiến trúc hệ thống

Project được xây dựng theo mô hình Client-Server.

JavaFX Client
    |
    | JSON Request qua Socket
    v
AuctionClient
    |
    v
AuctionServer
    |
    v
RequestHandler
    |
    v
Service Layer
    |
    v
DAO Layer
    |
    v
SQLite Database

Mô tả:

JavaFX Client: hiển thị giao diện và nhận thao tác từ người dùng.
AuctionClient: đóng gói request và gửi tới server.
AuctionServer: lắng nghe kết nối từ client qua port 8888.
RequestHandler: phân loại action như LOGIN, CREATE_ITEM, CREATE_AUCTION, PLACE_BID.
Service Layer: xử lý nghiệp vụ chính.
DAO Layer: thao tác trực tiếp với SQLite database.
SQLite Database: lưu thông tin users, items, auctions và bids.

Client không truy cập database trực tiếp. Mọi thao tác dữ liệu đều được xử lý thông qua server.

7. Build và chạy test

Tại thư mục gốc project, chạy:

mvn clean test

Nếu build và test thành công, terminal sẽ hiển thị:

BUILD SUCCESS
8. Hướng dẫn chạy chương trình

Hệ thống cần chạy server trước, sau đó mới chạy client JavaFX.

Bước 1: Chạy server

Cách 1: chạy trực tiếp trong IntelliJ IDEA class:

server.AuctionServer

Cách 2: chạy bằng Maven command line:

mvn exec:java -Dexec.mainClass=server.AuctionServer

Khi server chạy thành công, console sẽ hiển thị:

AuctionServer dang chay o port 8888

Giữ server tiếp tục chạy trong suốt quá trình sử dụng chương trình.

Bước 2: Chạy client JavaFX

Mở một terminal khác tại thư mục gốc project và chạy:

mvn javafx:run

Thứ tự chạy bắt buộc:

1. Chạy server.AuctionServer
2. Chạy client bằng mvn javafx:run
9. Chạy nhiều client

Để chạy nhiều client cùng lúc:

1. Chạy một AuctionServer duy nhất.
2. Mở terminal thứ nhất và chạy: mvn javafx:run
3. Mở terminal thứ hai và chạy lại: mvn javafx:run

Mỗi client có thể đăng nhập bằng một tài khoản khác nhau để mô phỏng nhiều người dùng cùng truy cập hệ thống.

10. Tài khoản mẫu
Seller:
username: seller1
password: 123456

Admin:
username: admin1
password: 123456

Bidder:
username: bidder1
password: 123456
11. Chức năng đã hoàn thành
11.1. Chức năng chung
Đăng nhập.
Đăng ký tài khoản.
Phân quyền theo role: BIDDER, SELLER, ADMIN.
Chuyển màn hình theo role sau khi đăng nhập.
Client giao tiếp với server bằng Socket và JSON.
Server xử lý request và trả response cho client.
Dữ liệu được lưu trong SQLite database.
11.2. Chức năng Seller

Seller có thể:

Xem danh sách sản phẩm của mình.
Tạo sản phẩm mới.
Cập nhật sản phẩm.
Xóa sản phẩm.
Refresh danh sách sản phẩm.
Logout.

Seller chỉ quản lý sản phẩm. Seller không trực tiếp tạo phiên đấu giá. Sản phẩm sau khi tạo sẽ được Admin xem và tạo auction.

11.3. Chức năng Admin

Admin có thể:

Xem toàn bộ sản phẩm do seller tạo.
Xem danh sách phiên đấu giá.
Tạo phiên đấu giá từ Item ID.
Start auction.
Cancel auction.
Close expired auctions.
Mark auction as paid.
Refresh dữ liệu.
Logout.

Luồng chuẩn:

Seller tạo sản phẩm
→ Admin xem sản phẩm
→ Admin tạo auction cho sản phẩm đó
→ Admin start auction
→ Bidder tham gia đặt giá
11.4. Chức năng Bidder

Bidder có thể:

Xem danh sách auction.
Xem chi tiết auction.
Đặt giá cho auction đang chạy.
Refresh dữ liệu auction.
Logout.

Khi bidder đặt giá, hệ thống kiểm tra:

User phải có role BIDDER.
Auction phải ở trạng thái RUNNING.
Giá đặt phải lớn hơn current_price.
Auction phải tồn tại.
Bid amount phải hợp lệ.

Nếu bid hợp lệ, hệ thống sẽ:

Lưu bid vào bảng bids.
Cập nhật current_price của auction.
Cập nhật current_winner_id của auction.
12. Các tình huống kỹ thuật quan trọng
12.1. Client-Server bằng Socket

Client không gọi DAO hoặc database trực tiếp. Mọi thao tác đều đi qua:

Controller
→ AuctionClient
→ AuctionServer
→ RequestHandler
→ Service
→ DAO
→ Database
12.2. Request/Response dạng JSON

Client gửi request gồm:

action
data

Ví dụ:

action = PLACE_BID
data = auctionId, bidderId, amount

Server xử lý và trả về response gồm:

success
message
data
12.3. Xử lý nghiệp vụ đấu giá

Các logic chính được đặt trong service layer:

AuthService: xử lý login/register.
ItemService: xử lý sản phẩm của seller.
AuctionService: xử lý trạng thái auction.
BidService: xử lý đặt giá.
12.4. Xử lý lỗi

Hệ thống có xử lý các trường hợp:

Sai username/password.
Username đã tồn tại.
Auction không tồn tại.
Item không tồn tại.
Bidder đặt giá thấp hơn hoặc bằng current_price.
User không phải BIDDER nhưng cố đặt giá.
Auction không ở trạng thái RUNNING.
Dữ liệu nhập sai định dạng.
12.5. Concurrent bidding

Logic đặt giá được xử lý tập trung trong BidService. Service kiểm tra trạng thái auction và current price trước khi cập nhật giá mới, giúp hạn chế lỗi khi có nhiều request đặt giá.

12.6. Realtime update

Hệ thống hiện cập nhật dữ liệu bằng thao tác Refresh từ client. Khi bấm Refresh, client gửi request mới lên server để lấy dữ liệu mới nhất từ database.

13. Cơ sở dữ liệu

Các bảng chính:

users
items
auctions
bids

Ý nghĩa:

users: lưu thông tin người dùng và role.
items: lưu sản phẩm do seller tạo.
auctions: lưu phiên đấu giá.
bids: lưu lịch sử đặt giá.

Các giá trị tiền như starting_price, current_price và amount được xử lý trong Java bằng BigDecimal.

14. OOP áp dụng trong project

Project áp dụng các nguyên lý lập trình hướng đối tượng:

Encapsulation

Các model có thuộc tính private và truy cập qua getter/setter.

Inheritance

User có các class con:

Bidder
Seller
Admin

Item có các class con:

Electronics
Art
Vehicle
Abstraction

Item là abstract class và các loại item cụ thể kế thừa từ class này.

Polymorphism

Các class con của Item có thể override phương thức như printInfo() để thể hiện thông tin riêng.

15. Kiểm thử

Project sử dụng JUnit 5 để kiểm thử một số logic service quan trọng.

Chạy test bằng:

mvn clean test

Các test chính kiểm tra logic như:

AuctionService.
BidService.
Validate bid amount.
Validate auction status.
Validate chuyển trạng thái auction.
16. Hạn chế

Một số chức năng chưa hoàn thiện :

Chưa triển khai ví tiền / balance cho bidder.
Realtime update hiện tại được thực hiện bằng refresh thủ công.
Chưa có auto-bidding.
Chưa có biểu đồ lịch sử bid.
Giao diện có thể tiếp tục cải thiện bằng TableView hoặc CardView.
17. Hướng phát triển

Có thể phát triển thêm:

Kiểm tra số dư tài khoản bidder trước khi đặt giá.
Auto-bidding.
Observer/WebSocket để realtime bidding tốt hơn.
Anti-sniping extension.
Bid history chart.
CI/CD bằng GitHub Actions.
Giao diện TableView cho auction/product/bid history.
18. Link nộp

GitHub repository:

https://github.com/25021858-svg/baitaplon

Báo cáo PDF:

https://drive.google.com/drive/folders/1Z_2uXVD-K4fQIPWeu4hHhxatniHMMUaG?dmr=1&ec=wgc-drive-%5Bmodule%5D-goto

Video demo:

https://drive.google.com/drive/folders/1Z_2uXVD-K4fQIPWeu4hHhxatniHMMUaG?dmr=1&ec=wgc-drive-%5Bmodule%5D-goto
19. Phân công công việc
Thành viên	MSSV	Công việc
Trương Xuân Lộc	25021858	Thiết kế database, model, DAO, service, socket server, request/response, bidding logic, JUnit test, tích hợp frontend-backend, hoàn thiện README, báo cáo và video demo, Hỗ trợ giao diện JavaFX/FXML/CSS, kiểm thử giao diện,  báo cáo/video
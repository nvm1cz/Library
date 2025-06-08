Hướng dẫn bạn cài đặt demo database
1. Sử dụng SQL Server Management Studio, chọn Windows Authentication khi kết nối với server
2. Mở SQL Server Configuration Manager, bạn thấy trong tab SQL Server Services có một server là SQLEXPRESS
3. Trong tab SQL Server Network Configuration, mở Protocols for SQLEXPRESS, bạn sẽ thấy mục TCP/IP, chọn Enable TCP/IP
4. Mở Properties của TCP/IP, trong phần IP Addresses, tìm IPAll ở dưới cùng, đặt TCP Port là 1433 còn TCP Dynamic ports thì để trống
5. Restart server SQLEXPRESS trong tab SQL Server Services
6. Xong rồi chạy code Main.java

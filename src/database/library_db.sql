USE library_db;

-- Bảng Admin
CREATE TABLE Admin (
AdminID INT AUTO_INCREMENT PRIMARY KEY,
Username VARCHAR(50) UNIQUE NOT NULL,
Password VARCHAR(100) NOT NULL,
FullName VARCHAR(100)
);

-- Bảng Borrower
CREATE TABLE Borrower (
BorrowerID VARCHAR(50) PRIMARY KEY, -- mã SV hoặc ID cấp cho người ngoài
FullName VARCHAR(100),
Phone VARCHAR(20),
IsStudent BOOLEAN NOT NULL
);

-- Bảng UserAccount
CREATE TABLE UserAccount (
AccountID INT AUTO_INCREMENT PRIMARY KEY,
Username VARCHAR(50) UNIQUE NOT NULL,
Password VARCHAR(100) NOT NULL,
BorrowerID VARCHAR(50),
FOREIGN KEY (BorrowerID)
REFERENCES Borrower(BorrowerID)
ON UPDATE CASCADE
ON DELETE SET NULL
);

-- Bảng Book (không còn cột Author)
CREATE TABLE Book (
BookID INT AUTO_INCREMENT PRIMARY KEY,
Title VARCHAR(200) NOT NULL,
TotalCopies INT NOT NULL,
AvailableCopies INT NOT NULL,
TotalBorrows INT DEFAULT 0
);

-- Bảng Author
CREATE TABLE Author (
AuthorID INT AUTO_INCREMENT PRIMARY KEY,
FullName VARCHAR(100) NOT NULL
);

-- Bảng BookAuthor (nhiều-nhiều)
CREATE TABLE BookAuthor (
BookID INT NOT NULL,
AuthorID INT NOT NULL,
PRIMARY KEY (BookID, AuthorID),
FOREIGN KEY (BookID)
REFERENCES Book(BookID)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (AuthorID)
REFERENCES Author(AuthorID)
ON UPDATE CASCADE
ON DELETE CASCADE
);

-- Bảng Genre
CREATE TABLE Genre (
GenreID INT AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(100) UNIQUE NOT NULL
);

-- Bảng BookGenre
CREATE TABLE BookGenre (
BookID INT,
GenreID INT,
PRIMARY KEY (BookID, GenreID),
FOREIGN KEY (BookID)
REFERENCES Book(BookID)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (GenreID)
REFERENCES Genre(GenreID)
ON UPDATE CASCADE
ON DELETE CASCADE
);

-- Bảng BorrowEntry
CREATE TABLE BorrowEntry (
EntryID INT AUTO_INCREMENT PRIMARY KEY,
BorrowerID VARCHAR(50) NOT NULL,
BookID INT NOT NULL,
BorrowDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ReturnDate DATETIME,
ProcessedBy INT,
FOREIGN KEY (BorrowerID)
REFERENCES Borrower(BorrowerID)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (BookID)
REFERENCES Book(BookID)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (ProcessedBy)
REFERENCES Admin(AdminID)
ON UPDATE CASCADE
ON DELETE SET NULL
);

-- Bảng Reservation
CREATE TABLE Reservation (
ReservationID INT AUTO_INCREMENT PRIMARY KEY,
AccountID INT NOT NULL,
BookID INT NOT NULL,
ReservationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
Status ENUM('Pending', 'Canceled', 'Fulfilled') DEFAULT 'Pending',
FOREIGN KEY (AccountID)
REFERENCES UserAccount(AccountID)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (BookID)
REFERENCES Book(BookID)
ON UPDATE CASCADE
ON DELETE CASCADE
);

-- Bảng Review
CREATE TABLE Review (
ReviewID INT AUTO_INCREMENT PRIMARY KEY,
EntryID INT NOT NULL,
Rating INT CHECK (Rating BETWEEN 1 AND 5),
Comment TEXT,
ReviewDate DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (EntryID)
REFERENCES BorrowEntry(EntryID)
ON UPDATE CASCADE
ON DELETE CASCADE
);

-- Bảng Wishlist
CREATE TABLE Wishlist (
    AccountID INT,
    BookID INT,
    DateAdded DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (AccountID, BookID),
    FOREIGN KEY (AccountID)
        REFERENCES UserAccount(AccountID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (BookID)
        REFERENCES Book(BookID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Notification (
    NotificationID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT NOT NULL,
    Message VARCHAR(255) NOT NULL,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    IsRead BOOLEAN DEFAULT 0,
    FOREIGN KEY (AccountID) REFERENCES UserAccount(AccountID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Dữ liệu mẫu

-- Admin
INSERT INTO Admin (Username, Password, FullName) VALUES
('a', '1', 'Nguyễn Văn A');

-- Borrower
INSERT INTO Borrower (BorrowerID, FullName, Phone, IsStudent) VALUES
('SV001', 'Lê Văn Sinh', '0912345678', TRUE),
('ND001', 'Phạm Quý Khách', '0988888888', FALSE);

-- UserAccount
INSERT INTO UserAccount (Username, Password, BorrowerID) VALUES
('sv1', '1', 'SV001'),
('nguoidung1', 'hashedpw2', 'ND001');

-- Genre
INSERT INTO Genre (Name) VALUES
('Khoa học'), ('Văn học'), ('Lập trình'), ('Kinh tế');

-- Book
INSERT INTO Book (Title, TotalCopies, AvailableCopies, TotalBorrows) VALUES
('Lập trình C cơ bản', 10, 8, 2),
('Những người khốn khổ', 5, 5, 0),
('Kinh tế học vi mô', 7, 7, 0),
('Python nâng cao', 6, 4, 2);

-- Author
INSERT INTO Author (FullName) VALUES
('Nguyễn Văn T'), -- ID = 1
('Victor Hugo'), -- ID = 2
('Paul Krugman'), -- ID = 3
('Trần Minh Đức'); -- ID = 4

-- BookAuthor
INSERT INTO BookAuthor (BookID, AuthorID) VALUES
(1, 1), -- Lập trình C cơ bản - Nguyễn Văn T
(2, 2), -- Những người khốn khổ - Victor Hugo
(3, 3), -- Kinh tế học vi mô - Paul Krugman
(4, 4), -- Python nâng cao - Trần Minh Đức
(4, 1);

-- BookGenre
INSERT INTO BookGenre (BookID, GenreID) VALUES
(1, 3),
(2, 2),
(3, 4),
(4, 3),
(4, 1);

-- BorrowEntry
INSERT INTO BorrowEntry (BorrowerID, BookID, BorrowDate, ReturnDate, ProcessedBy) VALUES
('SV001', 1, '2025-05-20 08:00:00', NULL, 1);

-- Reservation
INSERT INTO Reservation (AccountID, BookID, ReservationDate, Status) VALUES
(1, 2, '2025-06-01 10:00:00', 'Pending'),
(2, 1, '2025-05-25 09:00:00', 'Fulfilled');

-- INDEXES

CREATE INDEX idx_book_title ON Book (Title);
CREATE INDEX idx_borrower_name ON Borrower (FullName);
CREATE INDEX idx_useraccount_username ON UserAccount (Username);
CREATE INDEX idx_borrowentry_bookid ON BorrowEntry (BookID);
CREATE INDEX idx_borrowentry_borrowerid ON BorrowEntry (BorrowerID);
CREATE INDEX idx_borrowentry_processedby ON BorrowEntry (ProcessedBy);
CREATE INDEX idx_reservation_accountid ON Reservation (AccountID);
CREATE INDEX idx_reservation_bookid ON Reservation (BookID);
CREATE INDEX idx_genre_name ON Genre (Name);
CREATE INDEX idx_author_name ON Author (FullName);
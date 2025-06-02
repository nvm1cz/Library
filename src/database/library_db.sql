USE library_db;

-- Table: Admin
CREATE TABLE Admin (
AdminID INT AUTO_INCREMENT PRIMARY KEY,
Username VARCHAR(50) UNIQUE NOT NULL,
Password VARCHAR(100) NOT NULL,
FullName VARCHAR(100)
);

-- Table: Borrower
CREATE TABLE Borrower (
BorrowerID VARCHAR(50) PRIMARY KEY, -- mã SV hoặc ID cấp cho người ngoài
FullName VARCHAR(100) NOT NULL,
Phone VARCHAR(20),
IsStudent BOOLEAN NOT NULL,
StudentCode VARCHAR(20) UNIQUE
);

-- Table: UserAccount
CREATE TABLE UserAccount (
AccountID INT AUTO_INCREMENT PRIMARY KEY,
Username VARCHAR(50) UNIQUE NOT NULL,
Password VARCHAR(100) NOT NULL,
BorrowerID VARCHAR(50),
FOREIGN KEY (BorrowerID) REFERENCES Borrower(BorrowerID)
);

-- Table: Book
CREATE TABLE Book (
BookID INT AUTO_INCREMENT PRIMARY KEY,
Title VARCHAR(200) NOT NULL,
Author VARCHAR(100) NOT NULL,
TotalCopies INT NOT NULL,
AvailableCopies INT NOT NULL,
TotalBorrows INT DEFAULT 0
);

-- Table: Genre
CREATE TABLE Genre (
GenreID INT AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(100) UNIQUE NOT NULL
);

-- Table: BookGenre (many-to-many)
CREATE TABLE BookGenre (
BookID INT,
GenreID INT,
PRIMARY KEY (BookID, GenreID),
FOREIGN KEY (BookID) REFERENCES Book(BookID),
FOREIGN KEY (GenreID) REFERENCES Genre(GenreID)
);

-- Table: BorrowEntry
CREATE TABLE BorrowEntry (
EntryID INT AUTO_INCREMENT PRIMARY KEY,
BorrowerID VARCHAR(50) NOT NULL,
BookID INT NOT NULL,
BorrowDate DATE NOT NULL DEFAULT CURRENT_DATE,
ReturnDate DATE,
ProcessedBy INT,
FOREIGN KEY (BorrowerID) REFERENCES Borrower(BorrowerID),
FOREIGN KEY (BookID) REFERENCES Book(BookID),
FOREIGN KEY (ProcessedBy) REFERENCES Admin(AdminID)
);

-- Table: Reservation
CREATE TABLE Reservation (
ReservationID INT AUTO_INCREMENT PRIMARY KEY,
AccountID INT NOT NULL,
BookID INT NOT NULL,
ReservationDate DATE NOT NULL DEFAULT CURRENT_DATE,
Status ENUM('Pending', 'Canceled', 'Fulfilled') DEFAULT 'Pending',
FOREIGN KEY (AccountID) REFERENCES UserAccount(AccountID),
FOREIGN KEY (BookID) REFERENCES Book(BookID)
);


-- Admin
INSERT INTO Admin (Username, Password, FullName) VALUES
('a', '1', 'Nguyễn Văn A');


-- Borrower
INSERT INTO Borrower (BorrowerID, FullName, Phone, IsStudent, StudentCode) VALUES
('SV001', 'Lê Văn Sinh', '0912345678', TRUE, 'SV001'),
('ND001', 'Phạm Quý Khách', '0988888888', FALSE, NULL);

-- UserAccount
INSERT INTO UserAccount (Username, Password, BorrowerID) VALUES
('sv1', '1', 'SV001'),
('nguoidung1', 'hashedpw2', 'ND001');

-- Genre
INSERT INTO Genre (Name) VALUES
('Khoa học'), ('Văn học'), ('Lập trình'), ('Kinh tế');

-- Book
INSERT INTO Book (Title, Author, TotalCopies, AvailableCopies, TotalBorrows) VALUES
('Lập trình C cơ bản', 'Nguyễn Văn T', 10, 8, 2),
('Những người khốn khổ', 'Victor Hugo', 5, 5, 0),
('Kinh tế học vi mô', 'Paul Krugman', 7, 7, 0),
('Python nâng cao', 'Trần Minh Đức', 6, 4, 2);

-- BookGenre (liên kết sách với thể loại)
INSERT INTO BookGenre (BookID, GenreID) VALUES
(1, 3), -- Lập trình C cơ bản - Lập trình
(2, 2), -- Những người khốn khổ - Văn học
(3, 4), -- Kinh tế học vi mô - Kinh tế
(4, 3), -- Python nâng cao - Lập trình
(4, 1); -- Python nâng cao - Khoa học

-- BorrowEntry
INSERT INTO BorrowEntry (BorrowerID, BookID, BorrowDate, ReturnDate, ProcessedBy) VALUES
('SV001', 1, '2025-05-20', NULL, 1);

-- Reservation
INSERT INTO Reservation (AccountID, BookID, ReservationDate, Status) VALUES
(1, 2, '2025-06-01', 'Pending'),
(2, 1, '2025-05-25', 'Fulfilled');


-- INDEXES

-- Book
CREATE INDEX idx_book_title ON Book (Title);
CREATE INDEX idx_book_author ON Book (Author);

-- Borrower
CREATE INDEX idx_borrower_name ON Borrower (FullName);
CREATE INDEX idx_borrower_studentcode ON Borrower (StudentCode);

-- UserAccount
CREATE INDEX idx_useraccount_username ON UserAccount (Username);

-- BorrowEntry
CREATE INDEX idx_borrowentry_bookid ON BorrowEntry (BookID);
CREATE INDEX idx_borrowentry_borrowerid ON BorrowEntry (BorrowerID);
CREATE INDEX idx_borrowentry_processedby ON BorrowEntry (ProcessedBy);

-- Reservation
CREATE INDEX idx_reservation_accountid ON Reservation (AccountID);
CREATE INDEX idx_reservation_bookid ON Reservation (BookID);

-- Genre
CREATE INDEX idx_genre_name ON Genre (Name);
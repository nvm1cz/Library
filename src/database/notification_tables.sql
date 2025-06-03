-- Create Notification table
CREATE TABLE IF NOT EXISTS Notification (
    NotificationID INT PRIMARY KEY AUTO_INCREMENT,
    AccountID VARCHAR(50),
    BookID INT,
    Message TEXT NOT NULL,
    IsRated BOOLEAN DEFAULT FALSE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AccountID) REFERENCES UserAccount(BorrowerID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Create BookRating table
CREATE TABLE IF NOT EXISTS BookRating (
    RatingID INT PRIMARY KEY AUTO_INCREMENT,
    BookID INT,
    AccountID VARCHAR(50),
    Rating INT CHECK (Rating >= 1 AND Rating <= 5),
    RatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (BookID) REFERENCES Book(BookID),
    FOREIGN KEY (AccountID) REFERENCES UserAccount(BorrowerID)
); 
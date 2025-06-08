--Queries tạo bảng
CREATE TABLE Admin(
	AdminID INT IDENTITY(1,1),
	Username VARCHAR(20) UNIQUE NOT NULL,
	Password VARCHAR(20) NOT NULL,
	Fullname VARCHAR(20) NOT NULL,
	PRIMARY KEY(AdminID)
)
CREATE TABLE Borrower(
	BorrowerID VARCHAR(10),
	Fullname VARCHAR(20) NOT NULL,
	Phone VARCHAR(20) NOT NULL,
	IsStudent BIT NOT NULL,
	PRIMARY KEY(BorrowerID)
)
CREATE TABLE UserAccount(
	BorrowerID VARCHAR(10),
	Username VARCHAR(20) UNIQUE NOT NULL,
	Password VARCHAR(20) NOT NULL,
	PRIMARY KEY(BorrowerID),
	FOREIGN KEY(BorrowerID) REFERENCES Borrower(BorrowerID) ON UPDATE CASCADE
)
CREATE TABLE Book(
	BookID INT,
	Title VARCHAR(20) NOT NULL,
	TotalCopies INT,
	AvailableCopies INT,
	TotalBorrows INT,
	PRIMARY KEY(BookID)
)
CREATE TABLE Genre(
	GenreID INT IDENTITY(1,1),
	Name VARCHAR(20) UNIQUE NOT NULL,
	PRIMARY KEY(GenreID)
)
CREATE TABLE BookGenre (
    BookID INT,
    GenreID INT,
    PRIMARY KEY (BookID, GenreID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (GenreID) REFERENCES Genre(GenreID) ON UPDATE CASCADE ON DELETE CASCADE
)
CREATE TABLE Author(
	AuthorID INT IDENTITY(1,1),
	Name VARCHAR(20) UNIQUE NOT NULL,
	PRIMARY KEY(AuthorID)
)
CREATE TABLE BookAuthor (
    BookID INT,
    AuthorID INT,
    PRIMARY KEY (BookID, AuthorID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (AuthorID) REFERENCES Author(AuthorID) ON UPDATE CASCADE ON DELETE CASCADE
)
CREATE TABLE BorrowEntry(
	EntryID INT IDENTITY(1,1),
	BorrowerID VARCHAR(10),
	BookID INT,
	BorrowDate DATE NOT NULL,
	ReturnDate DATE,
	ProcessedBy INT, 
	PRIMARY KEY(EntryID),
	FOREIGN KEY(BorrowerID) REFERENCES Borrower(BorrowerID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(BookID) REFERENCES Book(BookID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(ProcessedBy) REFERENCES Admin(AdminID) ON UPDATE CASCADE
)
CREATE TABLE Reservation(
    ReservationID INT IDENTITY(1,1),
    BorrowerID VARCHAR(10),
    BookID INT,
    ReservationDate DATE NOT NULL,
    Status VARCHAR(20) DEFAULT 'Pending',
    PRIMARY KEY (ReservationID),
    FOREIGN KEY (BorrowerID) REFERENCES UserAccount(BorrowerID) ON UPDATE CASCADE ON DELETE CASCADE,  
    FOREIGN KEY (BookID) REFERENCES Book(BookID) ON UPDATE CASCADE ON DELETE CASCADE
);
--Procedure thêm sách
CREATE PROCEDURE AddBook
    @book_id INT NULL,
    @book_title VARCHAR(20),
    @copies_to_add INT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @existing_id INT;
    -- Kiểm tra sách đã tồn tại chưa
    SELECT TOP 1 @existing_id = BookID
    FROM Book
    WHERE Title = @book_title;
    IF @existing_id IS NOT NULL
    BEGIN
        -- Sách đã có: cập nhật số lượng
        UPDATE Book
        SET 
            TotalCopies = TotalCopies + @copies_to_add,
            AvailableCopies = AvailableCopies + @copies_to_add
        WHERE BookID = @existing_id;
    END
    ELSE
    BEGIN
        -- Sách chưa có: thêm mới
        INSERT INTO Book (BookID, Title, TotalCopies, AvailableCopies, TotalBorrows)
        VALUES (@book_id, @book_title, @copies_to_add, @copies_to_add, 0);
    END
END;
--Trigger preventing available copies lower than 0
CREATE TRIGGER trg_PreventNegativeAvailableCopies
ON Book
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (
        SELECT 1
        FROM inserted
        WHERE AvailableCopies < 0
    )
    BEGIN
        ROLLBACK ;
    END
END;
--Procedure thêm thể loại
CREATE PROCEDURE AddGenre
    @genreName NVARCHAR(100)
AS
BEGIN
    INSERT INTO Genre (Name) VALUES (@genreName);
END
--Procedure thêm tác giả
CREATE PROCEDURE AddAuthor
    @authorName NVARCHAR(100)
AS
BEGIN
    INSERT INTO Author (Name) VALUES (@authorName);
END
--Procedure update sách
CREATE PROCEDURE UpdateBook
    @bookID INT,
    @newTitle NVARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM Book WHERE BookID = @bookID)
    BEGIN
        UPDATE Book
        SET Title = @newTitle
        WHERE BookID = @bookID;
    END
END
--Procedure xóa sách
CREATE PROCEDURE DeleteBook
    @bookID INT
AS
BEGIN
    SET NOCOUNT ON;
    -- Kiểm tra tồn tại
    IF NOT EXISTS (SELECT 1 FROM Book WHERE BookID = @bookID)
    BEGIN
        RETURN;
    END
    -- Kiểm tra sách đang được mượn
    IF EXISTS (
        SELECT 1
        FROM BorrowEntry
        WHERE BookID = @bookID AND ReturnDate IS NULL
    )
    BEGIN
        RETURN;
    END
    -- Cho phép xóa
    DELETE FROM Book
    WHERE BookID = @bookID;
END;
--Procedure thêm tác giả cho sách
CREATE PROCEDURE AddBookAuthor
    @BookID INT,
    @AuthorID INT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO BookAuthor (BookID, AuthorID)
    VALUES (@BookID, @AuthorID);
END
--Procedure thêm thể loại cho sách
CREATE PROCEDURE AddBookGenre
    @BookID INT,
    @GenreID INT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO BookGenre (BookID, GenreID)
    VALUES (@BookID, @GenreID);
END
--Procedure thêm Admin
CREATE PROCEDURE addAdmin
    @Username VARCHAR(20),
    @Password VARCHAR(20),
    @Fullname VARCHAR(20)
AS
BEGIN
    -- Check if username already exists
    IF EXISTS (SELECT 1 FROM Admin WHERE Username = @Username)
    BEGIN
        RETURN;
    END
    INSERT INTO Admin (Username, [Password], Fullname)
    VALUES (@Username, @Password, @Fullname);
END;
--Procedure thêm Borrower
CREATE PROCEDURE addBorrower
    @Name NVARCHAR(100),
    @Phone VARCHAR(20),
    @IsStudent BIT,
    @StudentCode VARCHAR(20) = NULL  -- Required if student
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @NewBorrowerID VARCHAR(20);
    IF @IsStudent = 1
    BEGIN
        IF @StudentCode IS NULL
        BEGIN
            RETURN;
        END
        IF EXISTS (SELECT 1 FROM Borrower WHERE BorrowerID = @StudentCode)
        BEGIN
            RETURN;
        END
        SET @NewBorrowerID = @StudentCode;
    END
    ELSE
    BEGIN
        -- Get max current numeric guest ID and increment
        DECLARE @NextID INT;
        SELECT @NextID = ISNULL(MAX(CAST(SUBSTRING(BorrowerID, 2, 7) AS INT)), 0) + 1
        FROM Borrower
        WHERE BorrowerID LIKE 'G_______';  -- G + 7 digits
        -- Format to G + 7-digit string
        SET @NewBorrowerID = 'G' + RIGHT('0000000' + CAST(@NextID AS VARCHAR), 7);
    END
    -- Insert new borrower
    INSERT INTO Borrower (BorrowerID, Fullname, Phone, IsStudent)
    VALUES (@NewBorrowerID, @Name, @Phone, @IsStudent);
END;
--Procedure xóa Borrower
CREATE PROCEDURE deleteBorrower
    @BorrowerID VARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    -- 1. Check existence
    IF NOT EXISTS (SELECT 1 FROM Borrower WHERE BorrowerID = @BorrowerID)
    BEGIN
        RETURN;
    END
    -- 2. Check if borrower is currently borrowing books
    IF EXISTS (
        SELECT 1 FROM BorrowEntry 
        WHERE BorrowerID = @BorrowerID AND ReturnDate IS NULL
    )
    BEGIN
        RETURN;
    END
    -- 3. Check if borrower has active reservations
    IF EXISTS (
        SELECT 1 FROM Reservation
        WHERE BorrowerID = @BorrowerID AND Status LIKE 'Pending'
    )
    BEGIN
        RETURN;
    END
    -- 4. Optional: delete user account first (if linked)
    DELETE FROM UserAccount WHERE BorrowerID = @BorrowerID;
    -- 5. Delete borrower
    DELETE FROM Borrower WHERE BorrowerID = @BorrowerID;
END;
--Procedure tạo account
CREATE PROCEDURE createUserAccount
    @BorrowerID VARCHAR(20),
    @Username VARCHAR(20),
    @Password VARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    -- Check if borrower exists
    IF NOT EXISTS (
        SELECT 1 FROM Borrower WHERE BorrowerID = @BorrowerID
    )
    BEGIN
        RETURN;
    END
    -- Check if borrower already has an account
    IF EXISTS (
        SELECT 1 FROM UserAccount WHERE BorrowerID = @BorrowerID
    )
    BEGIN
        RETURN;
    END
    -- Check if username is taken
    IF EXISTS (
        SELECT 1 FROM UserAccount WHERE Username = @Username
    )
    BEGIN
        RETURN;
    END
    -- Create account
    INSERT INTO UserAccount (BorrowerID, Username, Password)
    VALUES (@BorrowerID, @Username, @Password);
END;
--Procedure thêm BorrowEntry
CREATE PROCEDURE borrowBook
    @BorrowerID VARCHAR(20),
	@BookID INT,
	@AdminID INT NULL
AS
BEGIN
    SET NOCOUNT ON;
    -- Step 1: Check borrower exists and is active
    IF NOT EXISTS (
        SELECT 1 FROM Borrower
        WHERE BorrowerID = @BorrowerID
    )
    BEGIN
        RETURN;
    END
    -- Step 2: Insert borrow entry (trigger handles checks and updates)
    INSERT INTO BorrowEntry (BookID, BorrowerID, BorrowDate, ProcessedBy)
    VALUES (
        @BookID,
        @BorrowerID,
        GETDATE(),
		@AdminID
    )
END
--Trigger insert vào borrowEntry
CREATE TRIGGER trg_BorrowEntry_Insert
ON BorrowEntry
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    -- Check each inserted row
    IF EXISTS (
        SELECT 1
        FROM inserted borrow_entry
        JOIN (
            SELECT BorrowerID, COUNT(*) AS currentBorrowCount
            FROM BorrowEntry
            WHERE ReturnDate IS NULL
            GROUP BY BorrowerID
        ) AS B ON borrow_entry.BorrowerID = B.BorrowerID
        WHERE B.currentBorrowCount > 5
    )
    BEGIN
        ROLLBACK;
        RETURN;
    END
    -- Check if book has available copies
    IF EXISTS (
        SELECT 1
        FROM inserted borrow_entry
        JOIN Book B ON borrow_entry.BookID = B.BookID
        WHERE B.AvailableCopies < 1
    )
    BEGIN
        ROLLBACK;
        RETURN;
    END
    -- Decrease available copies
    UPDATE B
    SET B.AvailableCopies = B.AvailableCopies - 1
    FROM Book B
    JOIN inserted borrow_entry ON B.BookID = borrow_entry.BookID;
	-- Increase total borrows
    UPDATE b
    SET b.TotalBorrows = b.TotalBorrows + borrow_count.Times
    FROM Book b
    JOIN (
        SELECT BookID, COUNT(*) AS Times
        FROM inserted
        GROUP BY BookID
    ) AS borrow_count ON b.BookID = borrow_count.BookID;
END
--Procedure trả sách
CREATE PROCEDURE returnBook
    @EntryID INT
AS
BEGIN
    SET NOCOUNT ON;
    -- Check if the entry exists and hasn't been returned yet
    IF NOT EXISTS (
        SELECT 1 FROM BorrowEntry
        WHERE EntryID = @EntryID AND ReturnDate IS NULL
    )
    BEGIN
        RETURN;
    END
    -- Set ReturnDate
    UPDATE BorrowEntry
    SET ReturnDate = GETDATE()
    WHERE EntryID = @EntryID;
END
--Trigger trả sách
CREATE TRIGGER trg_BorrowEntry_Return
ON BorrowEntry
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    -- Only update when ReturnDate is newly set (was NULL, now not NULL)
    UPDATE B
    SET B.AvailableCopies = B.AvailableCopies + 1
    FROM Book B
    JOIN inserted entry_inserted ON B.BookID = entry_inserted.BookID
    JOIN deleted entry_deleted ON entry_inserted.EntryID = entry_deleted.EntryID
    WHERE entry_deleted.ReturnDate IS NULL AND entry_inserted.ReturnDate IS NOT NULL;
END
--Procedure đặt sách
CREATE PROCEDURE makeReservation
    @BorrowerID VARCHAR(20),
    @BookID INT
AS
BEGIN
    SET NOCOUNT ON;
    -- Check borrower and book existence
    IF NOT EXISTS (SELECT 1 FROM Borrower WHERE BorrowerID = @BorrowerID)
        RETURN;
    IF NOT EXISTS (SELECT 1 FROM Book WHERE BookID = @BookID)
        RETURN;
    -- Count active pending reservations
    DECLARE @ReserveCount INT;
    SELECT @ReserveCount = COUNT(*) 
    FROM Reservation 
    WHERE BorrowerID = @BorrowerID AND Status = 'Pending';
    -- Determine max allowed
    DECLARE @IsStudent BIT;
    SELECT @IsStudent = IsStudent FROM Borrower WHERE BorrowerID = @BorrowerID;
    DECLARE @MaxAllowed INT = CASE WHEN @IsStudent = 1 THEN 3 ELSE 1 END;
    IF @ReserveCount >= @MaxAllowed
        RETURN;
    -- Insert reservation (copies not reduced here)
    INSERT INTO Reservation (BorrowerID, BookID, ReservationDate, Status)
    VALUES (@BorrowerID, @BookID, GETDATE(), 'Pending');
END
--Procedure cancel pending reservation
CREATE PROCEDURE cancelPendingReservation
    @ReservationID INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE Reservation
    SET Status = 'Cancelled'
    WHERE ReservationID = @ReservationID AND Status = 'Pending';
END
--Procedure cancel fulfilled reservation
CREATE PROCEDURE cancelFulfilledReservation
    @ReservationID INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @BookID INT;

    SELECT @BookID = BookID
    FROM Reservation
    WHERE ReservationID = @ReservationID AND Status = 'Fulfilled';

    IF @BookID IS NULL
        RETURN;

    -- Restore available copy
    UPDATE Book
    SET AvailableCopies = AvailableCopies + 1
    WHERE BookID = @BookID;

    -- Mark reservation as cancelled
    UPDATE Reservation
    SET Status = 'Cancelled'
    WHERE ReservationID = @ReservationID;
END
--Procedure fulfil reservation
CREATE PROCEDURE fulfilReservation
    @ReservationID INT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @BookID INT;
    SELECT @BookID = BookID
    FROM Reservation
    WHERE ReservationID = @ReservationID AND Status = 'Pending';
    IF @BookID IS NULL
        RETURN;
    -- Check available copies
    DECLARE @Available INT;
    SELECT @Available = AvailableCopies FROM Book WHERE BookID = @BookID;
    IF @Available < 1
        RETURN;
    -- Decrease copies
    UPDATE Book
    SET AvailableCopies = AvailableCopies - 1
    WHERE BookID = @BookID;
    -- Update reservation status
    UPDATE Reservation
    SET Status = 'Fulfilled'
    WHERE ReservationID = @ReservationID;
END
--Procedure complete reservation
CREATE PROCEDURE completeReservation
    @ReservationID INT,
    @AdminID INT = NULL
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @BorrowerID VARCHAR(20);
    DECLARE @BookID INT;
    -- Get borrower and book information for fulfilled reservation
    SELECT @BorrowerID = BorrowerID, @BookID = BookID
    FROM Reservation
    WHERE ReservationID = @ReservationID AND Status = 'Fulfilled';
    -- Exit if reservation is not found or not in 'Fulfilled' status
    IF @BorrowerID IS NULL OR @BookID IS NULL
    BEGIN
        RETURN;
    END
    -- Revert the available copy that was previously deducted
    UPDATE Book
    SET AvailableCopies = AvailableCopies + 1
    WHERE BookID = @BookID;
    -- Create borrow record and update status
    EXEC borrowBook @BorrowerID, @BookID, @AdminID;
    UPDATE Reservation
    SET Status = 'Complete'
    WHERE ReservationID = @ReservationID;
END
--View để xem sách đang được mượn
CREATE VIEW View_CurrentBorrows AS
SELECT 
    be.EntryID,                      
    be.BorrowerID,                    
    b.FullName AS BorrowerName,       
    bk.Title AS BookTitle,           
    be.BorrowDate                  
FROM BorrowEntry be
JOIN Borrower b ON be.BorrowerID = b.BorrowerID
JOIN Book bk ON be.BookID = bk.BookID
WHERE be.ReturnDate IS NULL;      

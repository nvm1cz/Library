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
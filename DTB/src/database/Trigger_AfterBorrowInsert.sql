DELIMITER //

CREATE TRIGGER Trigger_AfterBorrowInsert
AFTER INSERT ON BorrowEntry
FOR EACH ROW
BEGIN
    UPDATE Book
    SET AvailableCopies = AvailableCopies - 1,
        TotalBorrows = TotalBorrows + 1
    WHERE BookID = NEW.BookID;
END$$

CREATE TRIGGER trg_after_return_update
AFTER UPDATE ON BorrowEntry
FOR EACH ROW
BEGIN
    IF OLD.ReturnDate IS NULL AND NEW.ReturnDate IS NOT NULL THEN
        UPDATE Book
        SET AvailableCopies = AvailableCopies + 1
        WHERE BookID = NEW.BookID;
    END IF;
END//

DELIMITER ; 
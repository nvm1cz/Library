DELIMITER $$

CREATE PROCEDURE ReturnBook(
    IN p_entry_id INT
)
BEGIN
    -- No need to get book_id anymore since trigger will handle the update
    UPDATE BorrowEntry 
    SET ReturnDate = NOW() 
    WHERE EntryID = p_entry_id;
END$$

DELIMITER ;

DELIMITER //

CREATE PROCEDURE Procedure_ReturnBook(
    IN p_entry_id INT
)
BEGIN
    UPDATE BorrowEntry 
    SET ReturnDate = NOW() 
    WHERE EntryID = p_entry_id;
END//

DELIMITER ;

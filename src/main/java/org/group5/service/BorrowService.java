package org.group5.service;

import org.group5.Book;
import org.group5.model.BorrowRecord;
import org.group5.model.Member;

import java.util.ArrayList;
import java.util.List;

public class BorrowService {
    public BorrowRecord borrowBook(String bookId, String memberId, String borrowDate) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select or enter a Book ID.");
        }
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select or enter a Member ID.");
        }

        Book book = findBookById(bookId.trim());
        if (book == null) {
            throw new IllegalArgumentException("Book with ID '" + bookId.trim() + "' does not exist.");
        }

        Member member = findMemberById(memberId.trim());
        if (member == null) {
            throw new IllegalArgumentException("Member with ID '" + memberId.trim() + "' does not exist.");
        }

       
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book '" + book.getTitle() + "' is already BORROWED and cannot be borrowed again.");
        }

        
        book.markAsBorrowed();

       
        recordIdSequence++;
        String recId = "REC-" + recordIdSequence;
        String date = (borrowDate != null && !borrowDate.trim().isEmpty()) ? borrowDate.trim() : java.time.LocalDate.now().toString();

        BorrowRecord record = new BorrowRecord(recId, book, member, date);
        borrowRecords.add(0, record); // add most recent at top

        return record;
    }


    public BorrowRecord returnBook(String bookId, String returnDate) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select or enter a Book ID to return.");
        }

        Book book = findBookById(bookId.trim());
        if (book == null) {
            throw new IllegalArgumentException("Book with ID '" + bookId.trim() + "' does not exist.");
        }

        if (book.isAvailable()) {
            throw new IllegalArgumentException("Book '" + book.getTitle() + "' is already AVAILABLE. You cannot return a book that is not borrowed.");
        }

        BorrowRecord targetRecord = null;
        for (BorrowRecord record : borrowRecords) {
            if (record.isActive() && record.getBook().getBookId().equalsIgnoreCase(book.getBookId())) {
                targetRecord = record;
                break;
            }
        }

        String date = (returnDate != null && !returnDate.trim().isEmpty()) ? returnDate.trim() : java.time.LocalDate.now().toString();

        if (targetRecord != null) {
            targetRecord.markAsReturned(date);
        } else {
            // In case no record found, still restore book status
            book.markAsAvailable();
        }

        return targetRecord;
    }

    public BorrowRecord getActiveRecordForBook(String bookId) {
        if (bookId == null) return null;
        for (BorrowRecord r : borrowRecords) {
            if (r.isActive() && r.getBook().getBookId().equalsIgnoreCase(bookId.trim())) {
                return r;
            }
        }
        return null;
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<BorrowRecord>(borrowRecords);
    }
}

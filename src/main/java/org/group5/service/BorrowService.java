
package org.group5.service;

import org.group5.model.Book;
import org.group5.model.BorrowRecord;
import org.group5.model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BorrowService {

    private final List<Book> books;
    private final List<Member> members;
    private final List<BorrowRecord> borrowRecords;
    private int recordIdSequence;

    public BorrowService() {
        this(new ArrayList<Book>(), new ArrayList<Member>(), new ArrayList<BorrowRecord>());
    }

    public BorrowService(List<Book> books, List<Member> members, List<BorrowRecord> borrowRecords) {
        this.books = Objects.requireNonNull(books, "books must not be null");
        this.members = Objects.requireNonNull(members, "members must not be null");
        this.borrowRecords = Objects.requireNonNull(borrowRecords, "borrowRecords must not be null");
        this.recordIdSequence = 1000;
        for (BorrowRecord r : this.borrowRecords) {
            try {
                if (r.getRecordId() != null && r.getRecordId().startsWith("REC-")) {
                    int seq = Integer.parseInt(r.getRecordId().substring(4));
                    if (seq > this.recordIdSequence) {
                        this.recordIdSequence = seq;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private Book findBookById(String bookId) {
        for (Book book : books) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                return book;
            }
        }
        return null;
    }

    private Member findMemberById(String memberId) {
        for (Member member : members) {
            if (member.getMemberId().equalsIgnoreCase(memberId)) {
                return member;
            }
        }
        return null;
    }

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

        // Validate availability
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book '" + book.getTitle() + "' is already BORROWED and cannot be borrowed again.");
        }

        // Update book status
        book.markAsBorrowed();

        // Create and register borrow record
        recordIdSequence++;
        String recId = "REC-" + recordIdSequence;
        String date = (borrowDate != null && !borrowDate.trim().isEmpty()) ? borrowDate.trim() : java.time.LocalDate.now().toString();

        BorrowRecord record = new BorrowRecord(recId, book, member, date);
        borrowRecords.add(0, record); // add most recent at top

        return record;
    }

    /*
      Returns a borrowed book.
      Updates status to Available and updates the borrowing record.
     */

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

        // Find active borrow record for this book
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

    /*
     Finds active borrow record for a book.
     */

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




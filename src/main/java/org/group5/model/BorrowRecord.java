package org.group5.model;

public class BorrowRecord {
    private String recordId;
    private Book book;
    private Member member;
    private String borrowDate;
    private String returnDate; 
    private String status;     

    public static final String STATUS_BORROWED = "Borrowed";
    public static final String STATUS_RETURNED = "Returned";

    
    public BorrowRecord() {
        this.status = STATUS_BORROWED;
    }

    public BorrowRecord(String recordId, Book book, Member member, String borrowDate) {
        this.recordId = recordId;
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.returnDate = "-";
        this.status = STATUS_BORROWED;
    }

    public BorrowRecord(String recordId, Book book, Member member, String borrowDate, String returnDate, String status) {
        this.recordId = recordId;
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    
    public void markAsReturned(String returnDate) {
        this.returnDate = returnDate;
        this.status = STATUS_RETURNED;
        if (this.book != null) {
            this.book.markAsAvailable();
        }
    }

    
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return STATUS_BORROWED.equalsIgnoreCase(this.status);
    }
}

package org.group5.service;

import org.group5.model.Book;
import org.group5.model.BorrowRecord;
import org.group5.model.Member;

import java.util.ArrayList;
import java.util.List;

public class LibraryData {

    private List<Book> books;
    private List<Member> members;
    private List<BorrowRecord> borrowRecords;
    private int recordIdSequence;

    private BookService bookService;
    private MemberService memberService;
    private BorrowService borrowService;
    private DashboardService dashboardService;

    public LibraryData() {
        this.books = new ArrayList<Book>();
        this.members = new ArrayList<Member>();
        this.borrowRecords = new ArrayList<BorrowRecord>();
        this.recordIdSequence = 1000;
        seedSampleData();
        this.bookService = new BookService(books);
        this.memberService = new MemberService(members, borrowRecords);
        this.borrowService = new BorrowService(books, members, borrowRecords);
        this.dashboardService = new DashboardService(bookService, memberService, borrowService);
    }


    private void seedSampleData() {
        // Books
        books.add(new Book("B001", "Introduction to Java Programming", "Y. Daniel Liang", "Technology", Book.STATUS_AVAILABLE));
        books.add(new Book("B002", "Clean Code: Agile Software Craftsmanship", "Robert C. Martin", "Technology", Book.STATUS_AVAILABLE));
        books.add(new Book("B003", "Design Patterns: Elements of Reusable Object-Oriented Software", "Erich Gamma et al.", "Computer Science", Book.STATUS_AVAILABLE));
        books.add(new Book("B004", "Effective Java", "Joshua Bloch", "Technology", Book.STATUS_BORROWED));
        books.add(new Book("B005", "Database System Concepts", "Abraham Silberschatz", "Database", Book.STATUS_AVAILABLE));
        books.add(new Book("B006", "Head First Java", "Kathy Sierra & Bert Bates", "Technology", Book.STATUS_BORROWED));
        books.add(new Book("B007", "Artificial Intelligence: A Modern Approach", "Stuart Russell", "AI & Data", Book.STATUS_AVAILABLE));
        books.add(new Book("B008", "Object-Oriented Analysis and Design", "Grady Booch", "Software Engineering", Book.STATUS_AVAILABLE));

        // Members
        members.add(new Member("M001", "Kasun Perera", "0771234567"));
        members.add(new Member("M002", "Nimal Fernando", "0719876543"));
        members.add(new Member("M003", "Dilini Jayawardena", "0765554321"));
        members.add(new Member("M004", "Sachithra Silva", "0751122334"));
        members.add(new Member("M005", "Anuki Bandara", "0789988776"));

        // Borrow Records
        BorrowRecord rec1 = new BorrowRecord("REC-1001", books.get(3), members.get(0), "2026-09-25", "-", BorrowRecord.STATUS_BORROWED);
        BorrowRecord rec2 = new BorrowRecord("REC-1002", books.get(5), members.get(2), "2026-09-28", "-", BorrowRecord.STATUS_BORROWED);
        BorrowRecord rec3 = new BorrowRecord("REC-1003", books.get(0), members.get(1), "2026-09-10", "2026-09-20", BorrowRecord.STATUS_RETURNED);

        borrowRecords.add(rec1);
        borrowRecords.add(rec2);
        borrowRecords.add(rec3);
        recordIdSequence = 1003;
    }

    // Book operations
    public void addBook(Book book) {
        bookService.addBook(book);
    }

    public boolean removeBook(String bookId) {
        return bookService.removeBook(bookId);
    }

    public List<Book> searchBooks(String keyword) {
        return bookService.searchBooks(keyword);
    }

    public Book findBookById(String bookId) {
        return bookService.findBookById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    public List<Book> getBorrowedBooks() {
        return bookService.getBorrowedBooks();
    }

    // Member operations
    public void addMember(Member member) {
        memberService.addMember(member);
    }

    public boolean removeMember(String memberId) {
        return memberService.removeMember(memberId);
    }

    public List<Member> searchMembers(String keyword) {
        return memberService.searchMembers(keyword);
    }

    public Member findMemberById(String memberId) {
        return memberService.findMemberById(memberId);
    }

    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // Borrow and Return operations
    public BorrowRecord borrowBook(String bookId, String memberId, String borrowDate) {
        return borrowService.borrowBook(bookId, memberId, borrowDate);
    }

    public BorrowRecord returnBook(String bookId, String returnDate) {
        return borrowService.returnBook(bookId, returnDate);
    }

    public BorrowRecord getActiveRecordForBook(String bookId) {
        return borrowService.getActiveRecordForBook(bookId);
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowService.getAllBorrowRecords();
    }

    // Dashboard operations
    public int getTotalBooksCount() {
        return dashboardService.getTotalBooksCount();
    }

    public int getAvailableBooksCount() {
        return dashboardService.getAvailableBooksCount();
    }

    public int getBorrowedBooksCount() {
        return dashboardService.getBorrowedBooksCount();
    }

    public int getTotalMembersCount() {
        return dashboardService.getTotalMembersCount();
    }
}

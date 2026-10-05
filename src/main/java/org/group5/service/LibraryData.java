package org.group5.service;

import org.group5.model.Book;
import org.group5.model.BorrowRecord;
import org.group5.model.Member;

import java.util.ArrayList;
import java.util.List;

public class LibraryData {

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

}

package org.group5.service;

import org.group5.model.Book;
import org.group5.model.Member;
import java.util.List;

public class DashboardService {

    private List<Book> books;
    private List<Member> members;

    public DashboardService(
            BookService bookService,
            MemberService memberService) {

        this.books = bookService.getBooks();
        this.members = memberService.getMembers();
    }

    public int getTotalBooksCount() {
        return books.size();
    }

    public int getAvailableBooksCount() {
        int count = 0;
        for (Book b : books) {
            if (b.isAvailable()) count++;
        }
        return count;
    }

    public int getBorrowedBooksCount() {
        int count = 0;
        for (Book b : books) {
            if (!b.isAvailable()) count++;
        }
        return count;
    }

    public int getTotalMembersCount() {
        return members.size();
    }
}

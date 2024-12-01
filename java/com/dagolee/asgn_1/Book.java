package com.dagolee.asgn_1;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String availability = "TRUE";
    private String borrowerName = null;

    public Book(String title, String author, String ISBN, String availability, String borrowerName) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.availability = availability;
        this.borrowerName = borrowerName;
    }


    public boolean borrowBook(String name) {
        if ("AVAILABLE".equals(this.availability)) {
            this.availability = "UNAVAILABLE";
            this.borrowerName = name;
            return true;
        } else {
            return false;
        }
    }

    public boolean returnBook() {
        if (availability.equals("UNAVAILABLE")) {
            availability = "AVAILABLE";
            borrowerName = ".";
            return true;
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getAvailability() {
        return availability;
    }

    public String getBorrowerName() {
        return borrowerName;
    }
}

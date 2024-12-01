package com.dagolee.asgn_1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Library {
    private final ArrayList<Book> bookList = new ArrayList<>();

    // Constructor to load csv file content into Book ArrayList
    public Library() {
        try(Scanner scanner = new Scanner(new File("src/main/resources/BookData.csv"))){
            // While file has content, read new line into Book ArrayList
            while(scanner.hasNextLine()){
                String record = scanner.nextLine();
                String[] bookAttributes = record.split(",");
                // Initialise Book object with 5 book attributes read from file record and add to ArrayList
                bookList.add(new Book(bookAttributes[0], bookAttributes[1], bookAttributes[2], bookAttributes[3], bookAttributes[4]));
            }
        } catch(IOException _){}
    }

    public void saveToFile(){
        try(FileWriter fileOutput = new FileWriter("src/main/resources/BookData.csv")){
            for(Book book : bookList){
                String record = book.getTitle() + "," + book.getAuthor() + "," + book.getISBN() + "," + book.getAvailability() + "," + book.getBorrowerName() + "\n";
                fileOutput.write(record);
            }
        } catch(IOException _){}
    }

    public void addBook(String title, String author, String isbn, String availability, String borrowerName) {
        bookList.add(new Book(title, author, isbn, availability, borrowerName));
    }

    public ArrayList<Book> searchBookByTitle(String titleQuery) {
        ArrayList<Book> matchingBooks = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(titleQuery.toLowerCase()) && book.getAvailability().equals("AVAILABLE")) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }

    public ArrayList<Book> searchBorrowedBooks(String query) {
        ArrayList<Book> filteredBooks = new ArrayList<>();
        query = query.toLowerCase();
        for (Book book : bookList) {
            // Check if the query matches the title or ISBN and the book is not available
            if ((book.getTitle().toLowerCase().contains(query) || book.getISBN().toLowerCase().equals(query))
                    && book.getAvailability().equals("UNAVAILABLE")) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }
}

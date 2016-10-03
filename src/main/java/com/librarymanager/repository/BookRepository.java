package com.librarymanager.repository;

import com.librarymanager.entity.Book;

import java.util.List;

/**
 * Created by Vladislav on 10/3/2016.
 */
public interface BookRepository {

    public void addBook(Book book);
    public List<Book> removeBook(int id, String bookName);
    public List<Book> getBooksWithName(String bookName);
    public List<Book> getAllBooks();
    public void editBook(Book oldBook, Book newBook);

}

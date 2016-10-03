package com.librarymanager;

import com.librarymanager.entity.Book;
import com.librarymanager.repository.BookRepository;
import com.librarymanager.repository.BookRepositoryImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.List;

/**
 * Created by Vladislav on 10/3/2016.
 */
public class ConsoleListener extends Thread {
    private BookRepository bookRepository;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    @Override
    public void run() {
        while (!isInterrupted()) {
            System.out.println("Write number of the command: ");
            System.out.println("1 - add book.");
            System.out.println("2 - edit book. ");
            System.out.println("3 - remove book. ");
            System.out.println("4 - print all books. ");
            System.out.println("5 - exit. ");
            System.out.println("-----------------------------------------------");
            try {
                byte number = Byte.parseByte(reader.readLine());
                switch(number) {
                    case 1 : addBook();
                        break;
                    case 2 : editBook();
                        break;
                    case 3 : removeBook();
                        break;
                    case 4 : printAllBooks();
                        break;
                    case 5 : System.exit(0);
                }
            }
            catch (Exception e) {
                System.out.println("Unknown command!");
                System.out.println("-----------------------------------------------");
            }
        }
    }

    private void addBook() {
        System.out.println("Please write book and author in such format: [author]-[name], without [] and \"\"");
        try {
            String line = reader.readLine();
            String author = line.split("-")[0];
            String name = line.split("-")[1];
            bookRepository.addBook(new Book(0, name, author));
        }
        catch (Exception e) {
            System.out.println("Unknown command!");
            System.out.println("-----------------------------------------------");
        }
    }

    private void editBook() {
        System.out.println("Please write name of the book without \"\"");
        try {
            String name = reader.readLine();
            List<Book> books = bookRepository.getBooksWithName(name);
            if (books.isEmpty()) {
                System.out.println("This book is not found!");
                System.out.println("-----------------------------------------------");
                return;
            }
            if (books.size() == 1) {
                System.out.println("Please write book and author in such format: [author]-[name], without [] and \"\"");
                    String line = reader.readLine();
                    String newAuthor = line.split("-")[0];
                    String newName = line.split("-")[1];
                    bookRepository.editBook(books.get(0), new Book(0, newName, newAuthor));
                System.out.println("Book was succesfully edited.");
                System.out.println("-----------------------------------------------");
            }
            else {
                System.out.println("You have a few books with such name: ");
                books.stream().forEach((s) -> System.out.println(s.toString()));
                System.out.println("Print the id of book which you want to edit");
                int id = Integer.parseInt(reader.readLine());
                System.out.println("Please write book and author in such format: [author]-[name], without [] and \"\"");
                String line = reader.readLine();
                String newAuthor = line.split("-")[0];
                String newName = line.split("-")[1];
                bookRepository.editBook(books.get(id), new Book(0, newName, newAuthor));
                System.out.println("Book was succesfully edited.");
                System.out.println("-----------------------------------------------");
            }
        }
        catch (Exception e) {
            System.out.println("Unknown command!");
            System.out.println("-----------------------------------------------");
        }
    }

    private void removeBook() {
        System.out.println("Please write name of the book without \"\"");
        try {
            String name = reader.readLine();
            List<Book> books = bookRepository.getBooksWithName(name);
            if (books.isEmpty()) {
                System.out.println("This book is not found!");
                System.out.println("-----------------------------------------------");
                return;
            }
            if (books.size() == 1) {
                bookRepository.removeBook(books.get(0).getId(), books.get(0).getName());
                System.out.println("This book was succesfully deleted!");
                System.out.println("-----------------------------------------------");
            }
            else {
                System.out.println("You have a few books with such name: ");
                books.stream().forEach((s) -> System.out.println(s.toString()));
                System.out.println("Print the id of book which you want to delete.");
                int id = Integer.parseInt(reader.readLine());
                bookRepository.removeBook(id, name);
                System.out.println("This book was succesfully deleted!");
                System.out.println("-----------------------------------------------");
            }
        }
        catch (Exception e) {
            System.out.println("Unknown command!");
            System.out.println("-----------------------------------------------");
        }
    }

    private void printAllBooks() {
        bookRepository.getAllBooks().stream().forEach((s) -> System.out.println(s.toString()));
        System.out.println("-----------------------------------------------");
    }

    public ConsoleListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepositoryImpl();
        ConsoleListener consoleListener = new ConsoleListener(bookRepository);
        consoleListener.start();
    }
}

package com.librarymanager.repository;

import com.librarymanager.entity.Book;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Vladislav on 10/3/2016.
 */
public class BookRepositoryImpl implements BookRepository {
    private static String url;
    private static String userName;
    private static String password;

    private String ADD_BOOK = "INSERT INTO public.books(name, author) VALUES (?, ?);";
    private String SELECT_BOOKS_WITH_NAME = "SELECT * FROM public.books WHERE name=?;";
    private String REMOVE_BOOK_WITH_NAME = "DELETE FROM public.books WHERE name=?;";
    private String REMOVE_BOOK_WITH_NAME_AND_ID = "DELETE FROM public.books WHERE name=? AND id=?;";
    private String GET_BOOK_WITH_NAME = "SELECT * FROM public.books WHERE name=?;";
    private String GET_ALL_BOOKS = "SELECT * FROM public.books;";
    private String UPDATE_BOOK = "UPDATE public.books SET name=?, author=? WHERE id=?;";



    public void addBook(Book book) {
        try(Connection dbConnection = getDBConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(ADD_BOOK)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthorName());
            preparedStatement.execute();
        }
        catch (SQLException e) { System.err.println(e.getMessage()); }
    }

    public List<Book> removeBook(int id, String bookName) {
        List<Book> books = new ArrayList<>();
        try(Connection dbConnection = getDBConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_BOOKS_WITH_NAME)) {
            if (id != 0) {
                PreparedStatement deleteStatement = dbConnection.prepareStatement(REMOVE_BOOK_WITH_NAME_AND_ID);
                deleteStatement.setString(1, bookName);
                deleteStatement.setInt(2, id);
                deleteStatement.execute();
                deleteStatement.close();
                return books;
            }


            preparedStatement.setString(1, bookName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
            if (books.size() == 1) {
                PreparedStatement removeStatement = dbConnection.prepareStatement(REMOVE_BOOK_WITH_NAME);
                removeStatement.setString(1, bookName);
                removeStatement.execute();
                removeStatement.close();
            }
        }
        catch (SQLException e) { System.err.println(e.getMessage()); }
        return books;
    }

    public List<Book> getBooksWithName(String bookName) {
        List<Book> books = new ArrayList<>();
        try(Connection dbConnection = getDBConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_BOOKS_WITH_NAME)) {
            preparedStatement.setString(1, bookName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        }
        catch (SQLException e) { System.err.println(e.getMessage()); }
        return books;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try(Connection dbConnection = getDBConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_ALL_BOOKS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        }
        catch (SQLException e) { System.err.println(e.getMessage()); }
        return books;
    }

    @Override
    public void editBook(Book oldBook, Book newBook) {
        try(Connection dbConnection = getDBConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_BOOK)) {
            preparedStatement.setString(1, newBook.getName());
            preparedStatement.setString(2, newBook.getAuthorName());
            preparedStatement.setInt(3, oldBook.getId());
            preparedStatement.execute();
        }
        catch (SQLException e) { System.err.println(e.getMessage()); }
    }

    public BookRepositoryImpl() {
        Properties property = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream input = classLoader.getResourceAsStream("db/db.properties")) {
            property.load(input);
            url = property.getProperty("db.url");
            userName = property.getProperty("db.username");
            password = property.getProperty("db.password");
        }
        catch (IOException e) { System.err.println(e.getMessage()); }

    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(org.postgresql.Driver.class.getName());
        } catch (ClassNotFoundException e)  { System.err.println(e.getMessage()); }
        try {
            dbConnection = DriverManager.getConnection(url, userName, password);
            return dbConnection;
        } catch (SQLException e)  { System.err.println(e.getMessage()); }
        return dbConnection;
    }
}

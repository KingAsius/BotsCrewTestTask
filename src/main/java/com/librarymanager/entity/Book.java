package com.librarymanager.entity;

/**
 * Created by Vladislav on 10/3/2016.
 */
public class Book {
    private int id;
    private String name;
    private String authorName;

    public Book(int id, String name, String authorName) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getAuthorName() + " - \"" + this.getName() + "\"" ;
    }
}

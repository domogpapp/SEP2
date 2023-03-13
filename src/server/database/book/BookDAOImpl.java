package server.database.book;


import server.database.DatabaseConnection;
import shared.Author;
import shared.Book;
import shared.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

    private static BookDAOImpl instance;

    public static synchronized BookDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new BookDAOImpl();
        }
        return instance;
    }

    private BookDAOImpl () {


    }

    @Override
    public Book create(String isbn, String title, String coverType, Author author, int yearOfPublishing, ArrayList<Genre> genres) throws SQLException {
        try(Connection connection = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO Book(isbn, title, publication_year, cover_type, author_id) VALUES (?, ?, ?, ?,?);");
            statement.setString(1, isbn);
            statement.setString(2, title);
            statement.setInt(3, yearOfPublishing);
            statement.setString(4, coverType);
            statement.setInt(5,author.getId());
            statement.executeUpdate();
            return new Book(isbn, title, yearOfPublishing, coverType, author, genres);
        }
    }

    @Override
    public Book readByISBN(String isbn) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Book JOIN Author ON author_id = id WHERE isbn = ?");
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                int publicationYear = resultSet.getInt("publication_year");
                String coverType = resultSet.getString("cover_type");
                Author author = new Author(resultSet.getString("first_name"), resultSet.getString("last_name"), authorId);
                Book book = new Book(isbn,title,publicationYear,coverType,author, BookGenreDAOImpl.getInstance().getGenresForBook(isbn));
                return book;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getAllBooks() throws SQLException {
        return null;
    }

    @Override
    public List<Book> readByTitle(String searchString) throws SQLException {
        return null;
    }

    @Override
    public List<Book> readByGenre(String genre) throws SQLException {
        return null;
    }

    @Override
    public List<Book> readByAuthor(Author author) throws SQLException {
        return null;
    }

    @Override
    public void update(Book book) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Book SET title = ?, publication_year = ?, author_id = ?, cover_type = ? WHERE isbn = ?");
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getYearOfPublish());
            statement.setInt(3, book.getAuthor().getId());
            statement.setString(4, book.getCoverType());
            statement.setString(5, book.getIsbn());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Book book) throws SQLException {

    }
}

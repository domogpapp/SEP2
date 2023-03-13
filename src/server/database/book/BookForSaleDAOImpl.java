package server.database.book;

import server.database.DatabaseConnection;
import shared.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookForSaleDAOImpl implements BookForSaleDAO {

    private static BookForSaleDAOImpl instance;

    private BookForSaleDAOImpl() {


    }

    public static synchronized BookForSaleDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new BookForSaleDAOImpl();
        }
        return instance;
    }

    @Override
    public BookForSale create(String condition, double price, Book book, User user) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO bookforsale  (isbn,seller_id, price, condition) values (?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(4,condition);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return new BookForSale(keys.getInt(1), condition, price, book, user);
            }
            else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<BookForSale> getBooksSoldBy(String id) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from bookforsale where seller_id =? and price != -1;");
            preparedStatement.setString(1, id);
            return returnBooksUtilMethod(preparedStatement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String condition, double price, String isbn, String username) {
        try(Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement =connection.prepareStatement("UPDATE bookforsale SET price = ?, condition =? where isbn =? and seller_id =?;");
            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, condition);
            preparedStatement.setString(3, isbn);
            preparedStatement.setString(4, username);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookForSale delete(int id) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){
            BookForSale bookToBeDeleted = getBookById(id);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM bookforsale WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return bookToBeDeleted;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // this method is used when a book gets sold
    // price -1 will indicate that the book is no longer available for sale
    @Override
    public void changePrice(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE bookforsale SET price = -1 where id =?");
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<BookForSale> getAllBooks() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * from bookforsale where price != -1");

            return returnBooksUtilMethod(preparedStatement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override public List<BookForSale> getBooksByTitle(String title)
    {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM book JOIN bookforsale on book.isbn = bookforsale.isbn WHERE lower(title) LIKE lower(?) and price != -1;");
            preparedStatement.setString(1, "%" + title +  "%" );
            return returnBooksUtilMethod(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public List<BookForSale> getBooksByGenre(String genre)
    {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bookforsale JOIN book ON bookforsale.isbn = book.isbn JOIN bookgenre b ON book.isbn = b.isbn WHERE b.genre_name = ? and price != -1;");
            preparedStatement.setString(1, genre);
            return returnBooksUtilMethod(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public List<BookForSale> getBooksByAuthor(String authorFName, String authorLName)
    {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bookforsale JOIN book ON bookforsale.isbn = book.isbn JOIN author a ON book.author_id = a.id WHERE  first_name = ? AND last_name = ? and price != -1;");
            preparedStatement.setString(1, authorFName );
            preparedStatement.setString(2, authorLName );
           return returnBooksUtilMethod(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public BookForSale getBookById(int id) throws SQLException {

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from bookforsale where id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new BookForSale(resultSet.getInt("id"), resultSet.getString("condition"), resultSet.getDouble("price"), resultSet.getString("isbn"),
                        resultSet.getString("seller_id"));
            } return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public List<BookForSale> returnBooksUtilMethod(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BookForSale> booksForSale = new ArrayList<>();

            while (resultSet.next()) {
                booksForSale.add(new BookForSale(resultSet.getInt("id"), resultSet.getString("condition"), resultSet.getDouble("price"), resultSet.getString("isbn"),
                        resultSet.getString("seller_id")));
            }
            return booksForSale;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }





}

package server.database.book;
import server.database.DatabaseConnection;
import shared.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AuthorDAOImpl implements AuthorDAO{

    private static AuthorDAOImpl instance;

    public static synchronized AuthorDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new AuthorDAOImpl();
        }
        return instance;
    }

    @Override
    public Author create(String firstName, String lastName) throws SQLException {
        try(Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO author(first_name, last_name) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1,firstName);
            statement.setString(2, lastName);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                return new Author(firstName,lastName, keys.getInt(1));
            } else {
                throw new SQLException("No keys generated");
            }
        }
    }

    public ArrayList<Author> getAllAuthors() throws SQLException {

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM author");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Author> allAuthors = new ArrayList<>();
            while (resultSet.next()) {
                allAuthors.add(new Author(resultSet.getString(2), resultSet.getString(3), resultSet.getInt(1)));
            }
            return allAuthors;
        }
    }

    public Author getAuthorById(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name, last_name from author where id = ?;");
            preparedStatement.setInt(1,id );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                Author  author = new Author(firstName,lastName, id);
                return author;
            } else return null;
        }
    }

}

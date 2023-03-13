package client.model;

import shared.Author;
import shared.BookForSale;
import shared.Buyer;
import shared.Genre;
import util.Subject;

import java.util.ArrayList;
import java.util.List;

public interface BuyerModelManager extends Subject {

    List<BookForSale> getBooks();
    List<BookForSale> searchBooksByTitle(String title);
    ArrayList<Genre> getAllGenres();
    List<BookForSale> searchBooksByGenre(String genre);
    List<BookForSale> searchBooksByAuthor(String authorFName, String authorLName);
    ArrayList<Author> getAllAuthors();
    void addToShoppingCart(BookForSale bookForSale) throws Exception;
    void removeFromShoppingCart(BookForSale bookForSale);
    ArrayList<BookForSale> getShoppingCart();
    double calculatePrice();
    double getPrice();
    // also works
    void purchase();
    void checkBooks() throws Exception;
    void createOrder(Buyer buyer);
}

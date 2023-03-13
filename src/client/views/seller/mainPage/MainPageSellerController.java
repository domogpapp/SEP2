package client.views.seller.mainPage;


import client.core.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import shared.BookForSale;

public class MainPageSellerController {

    private MainPageSellerViewModel mainPageViewModel;
    private ViewHandler viewHandler;
    @FXML
    private TextField textFieldSearch;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelFullName;
    @FXML private  ListView listViewBooks;

    public void init(ViewHandler viewHandler, MainPageSellerViewModel mainPageSellerViewModel) {
        mainPageViewModel = mainPageSellerViewModel;
        this.viewHandler = viewHandler;
        labelFullName.textProperty().bindBidirectional(mainPageViewModel.getFullNameProperty());
        labelUsername.textProperty().bindBidirectional(mainPageViewModel.getUsernameProperty());
        mainPageViewModel.updateLabels();
        this.mainPageViewModel.loadBooksForSale();
        listViewBooks.setItems(this.mainPageViewModel.getBooksSoldBySeller());
//        listViewBooks.getItems().setAll(mainPageViewModel.getBooksForSale().getItems());
        textFieldSearch.textProperty().bindBidirectional(mainPageViewModel.getTitleProperty());

    }



    @FXML
    public void onSignOut(ActionEvent actionEvent) {
        viewHandler.openSign();
    }

    @FXML
    public void onButtonAddBook(ActionEvent actionEvent) {
        viewHandler.openAddBook();
    }

    @FXML
    public void onSearchByTitle(ActionEvent actionEvent) {
        listViewBooks.setItems(mainPageViewModel.searchBooksByTitle());
        textFieldSearch.clear();
    }

//    public ListView getListViewBooks() {
//        return listViewBooks;
//    }
    @FXML
    public void onButtonModify(ActionEvent actionEvent) {
        if (listViewBooks.getSelectionModel().getSelectedItem() != null) {
            sendBookToDetailsView();
            ViewHandler.getInstance().openBookDetailsSeller();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You have not selected any book :(");
            alert.show();
        }
    }

    public void onSeeAll() {
        listViewBooks.getItems().setAll(mainPageViewModel.getBooksForSale().getItems());
    }

    public BookForSale sendBookToDetailsView() {
        return (BookForSale) listViewBooks.getSelectionModel().getSelectedItem();
    }




}

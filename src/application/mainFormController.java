package application;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class mainFormController implements Initializable {
    
    @FXML
    private Button add_btn;

    @FXML
    private Button clear_btn;

    @FXML
    private Button customers_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button delete_btn;

    @FXML
    private Button import_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private TableColumn<ProductData, Double> inventory_col_Price;

    @FXML
    private TableColumn<ProductData, String> inventory_col_Status;

    @FXML
    private TableColumn<ProductData, Integer> inventory_col_Stock;

    @FXML
    private TableColumn<ProductData, String> inventory_col_Type;

    @FXML
    private TableColumn<ProductData, String> inventory_col_date;

    @FXML
    private TableColumn<ProductData, String> inventory_col_productID;

    @FXML
    private TableColumn<ProductData, String> inventory_col_productName;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private TableView<ProductData> inventory_tableView;

    @FXML
    private Button logoutBtn;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private Button menu_btn;

    @FXML
    private ComboBox<String> text_Status;

    @FXML
    private TextField text_Stock;

    @FXML
    private ComboBox<String> text_Type;

    @FXML
    private TextField text_price;

    @FXML
    private TextField text_productID;

    @FXML
    private TextField text_productName;

    @FXML
    private Button update_btn;
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
 private Image image ;
 
 // add btn
 public void inventoryAddBtn() {
	    if (text_productID.getText().isEmpty() ||
	    		text_productName.getText().isEmpty() ||
	    		text_Type.getSelectionModel().getSelectedItem() == null ||
	    				text_Stock.getText().isEmpty() ||
	    				text_price.getText().isEmpty() ||
	    				text_Status.getSelectionModel().getSelectedItem() == null || 
	    				data.path == null) {
	   
	        
	        alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error Message");
	        alert.setHeaderText(null);
	        alert.setContentText("Please fill all blank fields");
	        alert.showAndWait();
	    } else {
	        // Check if the product ID already exists
	        String checkProdIDQuery = "SELECT prod_id FROM product WHERE prod_id = '" + text_productID.getText() + "'";
	        Connection connect = database.connectDB();
	        try {
	            Statement statement = connect.createStatement();
	            result = statement.executeQuery(checkProdIDQuery);

	            if (result.next()) {
	                // Product ID exists, show an alert
	                 alert = new Alert(AlertType.ERROR);
	                alert.setTitle("Error Message");
	                alert.setHeaderText(null);
	                alert.setContentText("Product ID already exists.");
	                alert.showAndWait();
	            } else {
	                String insertData = "INSERT INTO product "
	                        + " (prod_id, prod_name, type, stock, price, status, image, date) "
	                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	                    PreparedStatement prepare = connect.prepareStatement(insertData);
	                    prepare.setString(1, text_productID.getText());
	                    prepare.setString(2, text_productName.getText());
	                    prepare.setString(3, (String) text_Type.getSelectionModel().getSelectedItem());
	                    prepare.setString(4, text_Stock.getText());
	                    prepare.setString(5, text_price.getText());
	                    prepare.setString(6, (String) text_Status.getSelectionModel().getSelectedItem());
	                    String path = data.path;
	                    path = path.replace("\\", "\\\\");
	                    prepare.setString(7, path);

	                    // TO GET CURRENT DATE
	                    Date date = new Date();
	                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	                    prepare.setString(8, String.valueOf(sqlDate));
	                    prepare.executeUpdate();

	                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                    alert.setTitle("Information");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Successfully Added!");
	                    alert.showAndWait();
	                    
	                    inventoryShowData();
	                    inventoryClearBtn();
	                    inventoryShowData();
	                }} catch (Exception e) {
	                    e.printStackTrace();
	                }
	    }}
 
 public void inventoryUpdateBtn() {
	    if (text_productID.getText().isEmpty() ||
	    		text_productName.getText().isEmpty() ||
	    		text_Type.getSelectionModel().getSelectedItem() == null ||
	    				text_Stock.getText().isEmpty() ||
	    				text_price.getText().isEmpty() ||
	    				text_Status.getSelectionModel().getSelectedItem() == null || 
	    				data.path == null || data.id == 0) {
	   
	        
	        alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error Message");
	        alert.setHeaderText(null);
	        alert.setContentText("Please fill all blank fields");
	        alert.showAndWait();
	    } else {
	    	    String path = data.path;
	    	    path = path.replace("\\", "\\\\"); // Escaping the backslashes
	    	    String updateData = "UPDATE product SET "
	    	            + "prod_id = '" + text_productID.getText() + "', "
	    	            + "prod_name = '" + text_productName.getText() + "', "
	    	            + "type = '" + text_Type.getSelectionModel().getSelectedItem() + "', "
	    	            + "stock = '" + text_Stock.getText() + "', "
	    	            + "price = '" + text_price.getText() + "', "
	    	            + "status = '" + text_Status.getSelectionModel().getSelectedItem() + "', "
	    	            + "image = '" + path + "', "
	    	            + "date = '" + data.date + "' WHERE id = " + data.id;

	    	    connect = database.connectDB();
	    	    try {
	    	        Alert alert = new Alert(AlertType.CONFIRMATION);
	    	        alert.setTitle("Error Message");
	    	        alert.setHeaderText(null);
	    	        alert.setContentText("Are you sure you want to UPDATE Product ID: " + text_productID.getText() + "?");
	    	        Optional<ButtonType> option = alert.showAndWait();
	    	        if (option.get().equals(ButtonType.OK)) {
	    	            prepare = connect.prepareStatement(updateData);
	    	            prepare.executeUpdate();
	    	            alert = new Alert(AlertType.INFORMATION);
	    	            alert.setTitle("Error Message");
	    	            alert.setHeaderText(null);
	    	            alert.setContentText("Successfully Updated!");
	    	            alert.showAndWait();
	    	            inventoryShowData();
	    	            inventoryClearBtn();
	    	        }
	    	        else {
	    	        	  alert = new Alert(AlertType.ERROR);
		    	            alert.setTitle("Error Message");
		    	            alert.setHeaderText(null);
		    	            alert.setContentText("Cancelled");
		    	            alert.showAndWait();
	    	        }
	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	    	    }}
	    	
	    	
	    }
 
 
 public void inventoryClearBtn() {
	 text_productID.setText("");
	 text_productName.setText("");
	 text_Type.getSelectionModel().clearSelection();
	 text_Stock.setText("");
	 text_price.setText("");
	 text_Status.getSelectionModel().clearSelection();
	    data.path = "";
	    data.id = 0 ;
	    inventory_imageView.setImage(null);
	}
 // import button
 public void inventoryImportBtn() {
	    FileChooser openFile = new FileChooser();
	    openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*.png", "*.jpg"));
	    File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
	    if (file != null) {
	        data.path = file.getAbsolutePath();
	         image = new Image(file.toURI().toString(), 172, 174, false, true);
	        inventory_imageView.setImage(image);
	    }
}
    private ObservableList<ProductData> inventoryListData;

    public ObservableList<ProductData> inventoryDataList() {
        ObservableList<ProductData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = database.connectDB(); 

        try (PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                ProductData prodData = new ProductData(
                        result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date")
                );
                listData.add(prodData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listData;
    }

    private String[] typeList = {"Meals", "Drinks"};

    public void inventoryTypeList() {
        List<String> typeL = new ArrayList<>();
        for (String data : typeList) {
            typeL.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(typeL);
        text_Type.setItems(listData);
    }
    
    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_Stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    
    
    public void inventorySelectData() {
        ProductData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();
        if (num < 0) return;
        text_productID.setText(prodData.getProductId());
        text_productName.setText(prodData.getProductName());
        text_Stock.setText(String.valueOf(prodData.getStock()));
        text_price.setText(String.valueOf(prodData.getPrice()));
        
        data.path = prodData.getImage();
        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getStock());
        data.id = prodData.getId() ;
        Image image = new Image( path, 172, 174, false, true);
        inventory_imageView.setImage(image);
    }
    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {
        List<String> statusL = new ArrayList<>();
        for (String data : statusList) {
            statusL.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(statusL);
        text_Status.setItems(listData);
    } 
    
    public void logout() {
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.close();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
                Stage loginStage = new Stage();
                loginStage.setTitle("Cafe Shop Management System");
                loginStage.setScene(new Scene(root));
                loginStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
    }
}


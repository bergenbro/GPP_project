/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPP_project.controller;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import GPP_project.model.*;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Erik
 */
public class ReservationController{
    private ArrayList<Reservation> reservations;
    private ArrayList<Button> resButtons = new ArrayList();
    private ArrayList<Reservation> ALLReservations;
    private ArrayList<Customer> ALLCustomers;
    private ArrayList<Screening> ALLScreenings;
    private Statement statement;
    
    //Initialises Containers and textfields from Reservationsside.fxml
    
    private VBox buttonPane;
    private TextField searchBar;
    private Button searchButton;
    private Text nameField;
    private Text titleField;
    private Text theaterField;
    private Text seatField;
    
    public ReservationController(ArrayList<Customer> ALLCustomers,ArrayList<Reservation> ALLReservations,ArrayList<Screening> ALLScreenings, 
            Statement statement) throws Exception{
        this.ALLCustomers=ALLCustomers;
        this.ALLReservations=ALLReservations;
        this.ALLScreenings=ALLScreenings;
        this.statement=statement;
        reservations = new ArrayList<Reservation>();
        for(Reservation res: ALLReservations){
            reservations.add(res);
        }
        
    } 
    
    public void FXMLLoader(VBox buttonPane, TextField searchBar, Button searchButton, 
            Text nameField, Text titleField, Text theaterField, Text seatField){
        
        this.buttonPane = buttonPane;
        this.searchBar = searchBar;
        this.searchButton = searchButton;
        this.nameField = nameField;
        this.titleField = titleField;
        this.theaterField = theaterField;
        this.seatField = seatField;
        
        Image SearchIcon = new Image("GPP_project/resources/images/Searchicon.png");
        searchButton.setGraphic(new ImageView(SearchIcon));
            
        makeButtons();
    
    }
    
    public void makeButtons() {
        
        //Generates an ArrayList of buttons based on a list of Reservations
        //To be shown in the reservation overview.
        for(Button btn: resButtons){
            buttonPane.getChildren().remove(btn);
        }
        resButtons.clear();
        for(int i=1;i<reservations.size();i++){
            Reservation res=reservations.get(i);
            Button btn = new Button();
                btn.setText(res.getName());
                btn.setPrefSize(331, 65);
                btn.setOnAction(new EventHandler<ActionEvent>() {
            
                    /*Gives each button the ability to overwrite their 
                    reservation's information onto the detailed information field.*/
                    @Override
                    public void handle(ActionEvent event) {
                        nameField.setText(res.getName());
                        titleField.setText(res.getScreening().getMovieTitle());
                        theaterField.setText("Theater: "+res.getScreening().getTheaterNumber());
                        seatField.setText(res.printSeats());
                    }
                });
            resButtons.add(btn);
        }
        //Adds the buttons from the list to the VBox in the view. 
        for(Button btn: resButtons){
            buttonPane.getChildren().add(btn);
        }
        
    }  
    public void search() throws Exception{
        Reservation JD=reservations.get(0);
        reservations.clear();
        reservations.add(JD);
        String query = "SELECT * FROM Customers WHERE Name LIKE '"+searchBar.getCharacters().toString()+"%'";
        ResultSet rs = statement.executeQuery(query);
        ArrayList<Integer> IDs = new ArrayList<>();
        while(rs.next()){
            int CustomerID = rs.getInt("CustomerID");
            IDs.add(CustomerID);
        }
        rs.close();
        for(int i: IDs){
            query = "SELECT * FROM Reservations WHERE CustomerID = '"+i+"'";
            rs = statement.executeQuery(query);
            if(rs.next()){
                int scrnID = rs.getInt("ScreeningID");
                //Reservation res = new Reservation(ALLScreenings.get(scrnID),ALLCustomers.get(i));
                reservations.add(ALLReservations.get(rs.getInt("ReservationID")));
            }
            rs.close();
        }
        makeButtons();
    }
    
}

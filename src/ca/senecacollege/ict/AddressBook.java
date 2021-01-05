/* Alexis Arellano Saturday October 31/2020 */

package ca.senecacollege.ict;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddressBook extends Application{

	ArrayList<String> fName = new ArrayList<String>();
	ArrayList<String> lName = new ArrayList<String>();
	ArrayList<String> city = new ArrayList<String>();
	ArrayList<String> province = new ArrayList<String>();
	ArrayList<String> postalCode = new ArrayList<String>();
	int contactNum;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		read();
		primaryStage.setTitle("Arellano - Address Book");
		
		
		// First Name
		Label label_fn = new Label("First Name: ");
		TextField field_fn = new TextField();
		field_fn.setPrefWidth(500);
		HBox fnHbox = new HBox(50);
		fnHbox.setAlignment(Pos.CENTER);
		fnHbox.getChildren().addAll(label_fn, field_fn);
		
		// Last Name
		Label label_ln = new Label("Last Name: ");
		TextField field_ln = new TextField();
		field_ln.setPrefWidth(500);
		HBox lnHbox = new HBox(50);
		lnHbox.setAlignment(Pos.CENTER);
		lnHbox.getChildren().addAll(label_ln, field_ln);
		
		// City
		
		Label label_city = new Label("City: ");
		TextField field_city = new TextField();
		
		// Province
		Label label_province = new Label("Province: ");
		ChoiceBox<String> field_province = new ChoiceBox();
		field_province.getItems().addAll("Alberta","British Columbia","Ontario","Quebec","Nova Scotia","New Brunswick","Manitoba","Prince Edward Island","Saskatchewan","Newfoundland & Laborador","North West Territories","Yukon Territory","Nunavut");

		
		
		// Postal Code
		Label label_postal = new Label("Postal Code: ");
		TextField field_postal = new TextField();
		
		// CITY-PROVINCE-POSTAL LAYOUT
		HBox cppHbox = new HBox(10);
		cppHbox.setAlignment(Pos.CENTER);
		cppHbox.getChildren().addAll(label_city,field_city,label_province,field_province,label_postal,field_postal);
		
		
		
		// Form Layout
		VBox formVbox = new VBox(30);
		formVbox.setAlignment(Pos.CENTER);
		formVbox.getChildren().addAll(fnHbox,lnHbox,cppHbox);
		
		// ADD BUTTON
		Button btnAdd = new Button("Add");
		
		btnAdd.setOnAction(e -> add(field_fn.getText(),field_ln.getText(),field_city.getText(),field_province.getValue(),field_postal.getText()));
		
		// FIRST BUTTON
		Button btnFirst = new Button("First");
		
		btnFirst.setOnAction(e -> {
			contactNum = 0;
			field_fn.setText(fName.get(0));
			field_ln.setText(lName.get(0));
			field_city.setText(city.get(0));
			field_province.setValue(province.get(0));
			field_postal.setText(postalCode.get(0));
		});
		
		// NEXT BUTTON
		Button btnNext = new Button("Next");
		btnNext.setOnAction(e->{
			int last = (fName.size() - 1);
			if (contactNum >= last) {
				contactNum = 0;
			}
			else {
				contactNum++;
			}
			
			field_fn.setText(fName.get(contactNum));
			field_ln.setText(lName.get(contactNum));
			field_city.setText(city.get(contactNum));
			field_province.setValue(province.get(contactNum));
			field_postal.setText(postalCode.get(contactNum));
		});
		
		// PREVIOUS BUTTON
		Button btnPrevious = new Button("Previous");
		btnPrevious.setOnAction(e->{
			if (contactNum == 0) {
				contactNum = fName.size()-1;
			}
			else {
				contactNum--;
			}
			
			field_fn.setText(fName.get(contactNum));
			field_ln.setText(lName.get(contactNum));
			field_city.setText(city.get(contactNum));
			field_province.setValue(province.get(contactNum));
			field_postal.setText(postalCode.get(contactNum));
		});
		
		// LAST BUTTON
		Button btnLast = new Button("Last");
		btnLast.setOnAction(e->{
			contactNum = fName.size()-1;
			
			field_fn.setText(fName.get(contactNum));
			field_ln.setText(lName.get(contactNum));
			field_city.setText(city.get(contactNum));
			field_province.setValue(province.get(contactNum));
			field_postal.setText(postalCode.get(contactNum));
		});
		
		// UPDATE BUTTON
		Button btnUpdate = new Button("Update");
		btnUpdate.setOnAction(e->{
			fName.set(contactNum, field_fn.getText());
			lName.set(contactNum, field_ln.getText());
			city.set(contactNum, field_city.getText());
			province.set(contactNum, field_province.getValue());
		    postalCode.set(contactNum, field_postal.getText());
		    write();
		    System.out.println("Contact info for: " + fName.get(contactNum) + " " + lName.get(contactNum) + " Updated.");
		});
		
		// Button Layout
		
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(btnAdd, btnFirst, btnNext, btnPrevious, btnLast, btnUpdate);
		
		BorderPane bpane = new BorderPane();
		bpane.setPadding(new Insets(10,10,10,10));
		bpane.setCenter(formVbox);
		bpane.setBottom(hbox);
		
		
		Scene scene = new Scene (bpane, 800,300);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void add(String firstName, String lastName, String cty, String prov, String postal) {
		fName.add(firstName);
		lName.add(lastName);
		city.add(cty);
		province.add(prov);
		postalCode.add(postal);
		System.out.println("Contact info for: " + firstName + " " + lastName + " added.");
		contactNum++;
		
		write();
	}
	
	public void read() {
		String line;
		contactNum = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile("AddressBook.txt","rw");
			StringBuffer buffer = new StringBuffer();
			raf.seek(0);
			while(raf.getFilePointer() < raf.length()) {
				buffer.append(raf.readLine()+System.lineSeparator());
			}
			line = buffer.toString();
			String[] records = line.split("\n");
			
			if (raf.length() !=0 ) {
				for (int i=0;i<records.length;i++) {
					String [] contacts = records[i].split(";");
					
					fName.add(contacts[0]);
					lName.add(contacts[1]);
					city.add(contacts[2]);
					province.add(contacts[3]);
					postalCode.add(contacts[4]);
					contactNum++;
				}
			}

			raf.close();
		} 
		catch(IOException e) {
			System.err.println(e.toString());
		}
	}
	
	public void write() {
		
		try {
			RandomAccessFile raf = new RandomAccessFile("AddressBook.txt","rw");
			String[] str = new String [fName.size()];
			
			for (int i=0; i<fName.size(); i++) {
				str[i] = fName.get(i) + ";" + lName.get(i) + ";" + city.get(i) + ";" + province.get(i) + ";";
			}
			
			for (int j = 0; j < fName.size(); j++) {
				raf.write(str[j].getBytes());
				raf.write(postalCode.get(j).getBytes());
				if(str[j].length() != 0) {
					raf.writeBytes(";\n");
				}
				
			}
			raf.close();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}

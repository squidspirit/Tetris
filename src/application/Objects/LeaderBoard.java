package application.objects;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LeaderBoard extends VBox {

    final private int MAX_ITEM = 10;
    
    private ArrayList<String> list;
    private Label[] labels = new Label[MAX_ITEM];
    private int index;

    public LeaderBoard(ArrayList<String> list) {
        this.list = list;
        this.index = 0;

        super.setAlignment(Pos.TOP_CENTER);
        super.setSpacing(10);
        
        for (int i = 0; i < MAX_ITEM; i ++) {
            labels[i] = new Label();
            labels[i].setFont(new Font(24));
            labels[i].setTextFill(Color.LIGHTGRAY);
            labels[i].getStylesheets().add("/resources/scenes/FontStyle.css");
            labels[i].setStyle("-fx-font-family: Zpix;");
            super.getChildren().add(labels[i]);
        }

        this.update();
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        this.update();
    }



    private void update() {
        for (Label label : labels)
            label.setText("");

        if (list.isEmpty()) {
            labels[4].setText("CONNECTION FAILED.");
            return;
        }

        for (int i = index; i < MAX_ITEM && i < list.size(); i ++) {
            labels[i].setText(String.format("%03d %s", i + 1, list.get(i)));
        }
    }
}

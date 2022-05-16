package application.objects;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LeaderBoard extends VBox {

    final private int MAX_ITEM = 13;
    
    private ArrayList<String> list;
    private Label[] labels = new Label[MAX_ITEM];
    private int firstIndex;

    public LeaderBoard(ArrayList<String> list) {
        this.list = list;
        this.firstIndex = 0;

        super.setAlignment(Pos.TOP_CENTER);
        super.setSpacing(10);
        
        for (int i = 0; i < MAX_ITEM; i ++) {
            labels[i] = new Label();
            labels[i].setFont(new Font(24));
            labels[i].setTextFill(Color.LIGHTGRAY);
            labels[i].getStylesheets().add("/resources/fonts/FontStyle.css");
            labels[i].setStyle("-fx-font-family: Kongtext;");
            super.getChildren().add(labels[i]);
        }

        this.update();
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        this.update();
    }

    public boolean scrollUp() {
        if (firstIndex > 0) {
            firstIndex --;
            this.update();
            return true;
        }
        return false;
    }

    public boolean scrollDown() {
        if (firstIndex < list.size() - MAX_ITEM) {
            firstIndex ++;
            this.update();
            return true;
        }
        return false;
    }



    private void update() {
        for (Label label : labels)
            label.setText("");

        if (list.isEmpty()) {
            labels[4].setText("CONNECTION FAILED.");
            return;
        }

        for (int i = 0; i < MAX_ITEM && i + firstIndex < list.size(); i ++) {
            labels[i].setText(String.format("%03d %s", i + firstIndex + 1, list.get(i + firstIndex)));
        }
    }
}

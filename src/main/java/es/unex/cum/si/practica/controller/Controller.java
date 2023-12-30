package es.unex.cum.si.practica.controller;

import es.unex.cum.si.practica.model.genotype.Class;
import es.unex.cum.si.practica.model.util.Data;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
    private GridPane gridpane;

    @FXML
    private TextArea time0 = new TextArea();

    @FXML
    private TextArea time1 = new TextArea();

    @FXML
    private TextArea time10 = new TextArea();

    @FXML
    private TextArea time11 = new TextArea();

    @FXML
    private TextArea time12  = new TextArea();

    @FXML
    private TextArea time13 = new TextArea();

    @FXML
    private TextArea time14  = new TextArea();

    @FXML
    private TextArea time15 = new TextArea();

    @FXML
    private TextArea time16 = new TextArea();

    @FXML
    private TextArea time17 = new TextArea();

    @FXML
    private TextArea time18 = new TextArea();

    @FXML
    private TextArea time19 = new TextArea();

    @FXML
    private TextArea time2 = new TextArea();

    @FXML
    private TextArea time20 = new TextArea();

    @FXML
    private TextArea time21 = new TextArea();

    @FXML
    private TextArea time22 = new TextArea();

    @FXML
    private TextArea time23 = new TextArea();

    @FXML
    private TextArea time24 = new TextArea();

    @FXML
    private TextArea time25 = new TextArea();

    @FXML
    private TextArea time26 = new TextArea();

    @FXML
    private TextArea time27 = new TextArea();

    @FXML
    private TextArea time28 = new TextArea();

    @FXML
    private TextArea time29 = new TextArea();

    @FXML
    private TextArea time3 = new TextArea();

    @FXML
    private TextArea time4 = new TextArea();

    @FXML
    private TextArea time5 = new TextArea();

    @FXML
    private TextArea time6 = new TextArea();

    @FXML
    private TextArea time7 = new TextArea();

    @FXML
    private TextArea time8 = new TextArea();

    @FXML
    private TextArea time9 = new TextArea();

    public void show(Class aClass) {
        String text = "";
        text += "Subject: " +
                Data.getInstance().getSubject(aClass.subjectId()).denomination() + "\n";
        text += "Room: " +
                Data.getInstance().getRoomSlot(aClass.roomId()).denomination() + "\n";
        text += "Time: " +
                aClass.timeId() + "\n";
        switch (aClass.timeId()) {
            case 0:
                time0.setText(text);
                break;
            case 1:
                time1.setText(text);
                break;
            case 2:
                time2.setText(text);
                break;
            case 3:
                time3.setText(text);
                break;
            case 4:
                time4.setText(text);
                break;
            case 5:
                time5.setText(text);
                break;
            case 6:
                time6.setText(text);
                break;
            case 7:
                time7.setText(text);
                break;
            case 8:
                time8.setText(text);
                break;
            case 9:
                time9.setText(text);
                break;
            case 10:
                time10.setText(text);
                break;
            case 11:
                time11.setText(text);
                break;
            case 12:
                time12.setText(text);
                break;
            case 13:
                time13.setText(text);
                break;
            case 14:
                time14.setText(text);
                break;
            case 15:
                time15.setText(text);
                break;
            case 16:
                time16.setText(text);
                break;
            case 17:
                time17.setText(text);
                break;
            case 18:
                time18.setText(text);
                break;
            case 19:
                time19.setText(text);
                break;
            case 20:
                time20.setText(text);
                break;
            case 21:
                time21.setText(text);
                break;
            case 22:
                time22.setText(text);
                break;
            case 23:
                time23.setText(text);
                break;
            case 24:
                time24.setText(text);
                break;
            case 25:
                time25.setText(text);
                break;
            case 26:
                time26.setText(text);
                break;
            case 27:
                time27.setText(text);
                break;
            case 28:
                time28.setText(text);
                break;
            case 29:
                time29.setText(text);
                break;
            default:
                break;
        }
    }
}

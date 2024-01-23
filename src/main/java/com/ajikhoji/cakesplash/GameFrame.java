package com.ajikhoji.cakesplash;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameFrame extends Application {

    private static Pane pane_base = null, pane_content = null;
    private static Stage stage_game_window = null;

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        pane_base = new Pane();
        pane_content = new Pane();
        pane_content.setBackground(Value.bg_pane_content);
        pane_content.setPrefSize(Value.DBL_PANE_CONTENT_WIDTH,Value.DBL_PANE_CONTENT_HEIGHT);
        pane_content.setMinSize(Value.DBL_PANE_CONTENT_WIDTH,Value.DBL_PANE_CONTENT_HEIGHT);
        pane_content.setMaxSize(Value.DBL_PANE_CONTENT_WIDTH,Value.DBL_PANE_CONTENT_HEIGHT);
        stage.setScene(new Scene(pane_base,Value.DBL_SCREEN_WIDTH,Value.DBL_SCREEN_HEIGHT));
        pane_base.getChildren().add(pane_content);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        this.stage_game_window = stage;
        buildGUI();
        pane_base.getChildren().add(pane_content);
        //MainMenu.buildPane();
        //MainMenu.setAsContentPane();
        //LevelZero.buildPane();
        //LevelZero.setAsContentPane();
        loadLevel1();
        stage.show();
    }

    public static void loadLevel1() {
        LevelOne lo = new LevelOne();
        lo.buildPane();
        setContentPane(lo.getContentPane());
    }

    public static void loadLevel2() {
        LevelTwo lt = new LevelTwo();
        lt.buildPane();
        setContentPane(lt.getContentPane());
    }

    public static void setContentPane(Pane pane) {
        pane_content.getChildren().clear();
        pane_content.getChildren().add(pane);
    }

    private static Line getBorderLine(final double START_X, final double START_Y, final double END_X, final double END_Y, final double STROKE_WIDTH, final Color CLR) {
        Line l = new Line(START_X,START_Y,END_X,END_Y);
        l.setFill(CLR);
        l.setStroke(CLR);
        l.setStrokeWidth(STROKE_WIDTH);
        l.setOnMousePressed((MouseEvent event) -> {
            Value.dbl_screen_loc_x = event.getX();
            Value.dbl_screen_loc_y = event.getY();
        });
        l.setOnMouseDragged((MouseEvent event) -> {
            if(event.getScreenX() > 20.0D && event.getScreenX() < java.awt.Toolkit.getDefaultToolkit().getScreenSize().width - 20.0D) {
                stage_game_window.setX(event.getScreenX() - Value.dbl_screen_loc_x);
            }
            if(event.getScreenY() > 20.0D && event.getScreenY() < java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 20.0D) {
                stage_game_window.setY(event.getScreenY() - Value.dbl_screen_loc_y);
            }
        });
        return l;
    }
    private static void buildGUI() {
        pane_base.getChildren().clear();
        Pane pane_title = new Pane();
        pane_title.setMaxSize(Value.DBL_PANE_CONTENT_WIDTH, Value.DBL_PANE_TITLE_HEIGHT);
        pane_title.setMinSize(Value.DBL_PANE_CONTENT_WIDTH, Value.DBL_PANE_TITLE_HEIGHT);
        pane_title.setPrefSize(Value.DBL_PANE_CONTENT_WIDTH, Value.DBL_PANE_TITLE_HEIGHT);
        pane_title.setBackground(Value.bg_pane_title);
        pane_title.setLayoutX(Value.DBL_STROKE_WIDTH);
        pane_title.setLayoutY(Value.DBL_STROKE_WIDTH);
        pane_content.setLayoutX(Value.DBL_STROKE_WIDTH);
        pane_content.setLayoutY(Value.DBL_STROKE_WIDTH+Value.DBL_PANE_TITLE_HEIGHT);
        Text t_title = new Text("Brick Blast");
        t_title.setLayoutX(44.0D);
        t_title.setLayoutY(Value.DBL_PANE_TITLE_HEIGHT-11.0D);
        t_title.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 28));
        t_title.setFill(Color.web("#feddef"));
        t_title.hoverProperty().addListener((ov, newValue, oldValue) -> {
            if(newValue) {
                t_title.setFill(Color.web("#feddef"));
            } else {
                t_title.setFill(Color.web("#ef0936"));
            }
        });
        pane_title.getChildren().addAll(t_title);
        Line seperator = getBorderLine(Value.DBL_STROKE_WIDTH,Value.DBL_PANE_TITLE_HEIGHT+Value.DBL_STROKE_WIDTH,Value.DBL_SCREEN_WIDTH,Value.DBL_PANE_TITLE_HEIGHT+Value.DBL_STROKE_WIDTH,Value.DBL_STROKE_WIDTH,Color.web("#F0BF18"));
        Line l_left = getBorderLine((Value.DBL_STROKE_WIDTH/2.0D),0.0D,(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_SCREEN_HEIGHT,Value.DBL_STROKE_WIDTH,Color.web("#45AF09"));
        Line l_top = getBorderLine(Value.DBL_STROKE_WIDTH,(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_SCREEN_WIDTH-Value.DBL_STROKE_WIDTH,(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_STROKE_WIDTH,Color.web("#5670CD"));
        Line l_right = getBorderLine(Value.DBL_SCREEN_WIDTH-(Value.DBL_STROKE_WIDTH/2.0D),0.0D,Value.DBL_SCREEN_WIDTH-(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_SCREEN_HEIGHT,Value.DBL_STROKE_WIDTH,Color.web("#A7109F"));
        Line l_bottom = getBorderLine(Value.DBL_STROKE_WIDTH,Value.DBL_SCREEN_HEIGHT-(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_SCREEN_WIDTH-(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_SCREEN_HEIGHT-(Value.DBL_STROKE_WIDTH/2.0D),Value.DBL_STROKE_WIDTH,Color.web("#34E78A"));
        pane_base.getChildren().addAll(pane_title,seperator,l_left,l_top,l_right,l_bottom);
    }

    public static void main(String[] args) {
        launch();
    }

}


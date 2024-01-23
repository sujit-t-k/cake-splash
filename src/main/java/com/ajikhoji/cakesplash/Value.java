package com.ajikhoji.cakesplash;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Value {
    public final static Background bg_pane_content = new Background(new BackgroundFill(Color.web("#8D178F"), CornerRadii.EMPTY, Insets.EMPTY));
    public final static Background bg_pane_main_menu = new Background(new BackgroundFill(Color.web("#072933"), CornerRadii.EMPTY, Insets.EMPTY));
    public final static Background bg_pane_mode_and_level_selector = new Background(new BackgroundFill(Color.web("#4F0B3C"), CornerRadii.EMPTY, Insets.EMPTY));
    public final static Background bg_pane_title = new Background(new BackgroundFill(Color.web("#472D3C"), CornerRadii.EMPTY, Insets.EMPTY));
    public final static Background bg_pane_left = new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY));
    public final static double DBL_STROKE_WIDTH = 4.0D;//303033
    public final static double DBL_PANE_CONTENT_WIDTH = 1166.00D, DBL_PANE_CONTENT_HEIGHT = 1166.00D/1.56D, DBL_PANE_TITLE_HEIGHT = 00.00D;//40.00D;
    public final static double DBL_SCREEN_WIDTH = DBL_PANE_CONTENT_WIDTH + (2*DBL_STROKE_WIDTH), DBL_SCREEN_HEIGHT = DBL_PANE_CONTENT_HEIGHT + (2*DBL_STROKE_WIDTH) + DBL_PANE_TITLE_HEIGHT;
    public static double dbl_screen_loc_x=0.0D,dbl_screen_loc_y=0.0D;
    public final static double DBL_PANE_LEFT_WIDTH = 0.23*DBL_PANE_CONTENT_WIDTH;
}

package com.ajikhoji.cakesplash;

import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LevelTwo {

    private static Pane pane_content = null, pane_game_grid = null, pane_block = null, pane_left = null;
    private static StackPane pane_game_area = null;
    private final static short[][] short_array_banned_location = new short[][]{{0,0},{0,8},{6,0},{6,8},{2,4},{4,4},{3,3},{3,5}};
    private static Block[] b = new Block[LevelProperties.INT_NO_OF_TILES];
    private static boolean bln_mouse_action_allowed = false;
    private NumericalLabel nlblScore;
    private static Image imgbg = null;
    private static Image imgScoreBoard = null;
    private SFXPlayer sound = new SFXPlayer();
    private final boolean isSoundEnabled = true;
    private Text txtMoves = new Text("MOVES : 0");
    private int intMoves = 0;

    public void buildPane() {
        loadResources();
        pane_left = new Pane();
        pane_left.setBackground(Value.bg_pane_left);
        pane_left.setMaxSize(Value.DBL_PANE_LEFT_WIDTH, Value.DBL_PANE_CONTENT_HEIGHT);
        pane_left.setMinSize(Value.DBL_PANE_LEFT_WIDTH, Value.DBL_PANE_CONTENT_HEIGHT);
        pane_left.setPrefSize(Value.DBL_PANE_LEFT_WIDTH, Value.DBL_PANE_CONTENT_HEIGHT);
        Text t_level = new Text("LEVEL 2");
        t_level.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 36));
        t_level.setX(65.0D);
        t_level.setY(200.0D);
        t_level.setMouseTransparent(true);
        t_level.setFill(Color.web("#FFC90E"));
        Glow g = new Glow();
        g.setLevel(0.5D);
        t_level.setEffect(g);
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(g.levelProperty(), 0.2D)),
                new KeyFrame(Duration.millis(3000), new KeyValue(g.levelProperty(), 0.5D)),
                new KeyFrame(Duration.millis(3040), new KeyValue(g.levelProperty(), 0.48D))
        );
        t.setCycleCount(Timeline.INDEFINITE);
        t.setAutoReverse(true);
        t.playFromStart();
        Rectangle rect_exit = new Rectangle(100.0D, 48.0D);
        rect_exit.setFill(Color.web("#CC0000"));
        rect_exit.setX(12.0D);
        rect_exit.setY(Value.DBL_PANE_CONTENT_HEIGHT - 64.0D);
        rect_exit.setOnMouseClicked(e -> {
            System.exit(0);
        });
        Text txt_exit = new Text("EXIT");
        txt_exit.setFont(Font.font("Century Gothic", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 36));
        txt_exit.setX(28.0D);
        txt_exit.setY(Value.DBL_PANE_CONTENT_HEIGHT - 25.0D);
        txt_exit.setMouseTransparent(true);
        txt_exit.setFill(Color.web("#FFC90E"));
        nlblScore = new NumericalLabel();
        nlblScore.setValue(0L);
        nlblScore.setFill(Color.ANTIQUEWHITE);//Color.web("#FFF200")
        nlblScore.setFont(Font.font("Algerian", FontWeight.NORMAL, FontPosture.REGULAR, 40));
        nlblScore.setLayoutX(60.0D);
        nlblScore.setLayoutY(LevelProperties.DBL_GAME_AREA_HEIGHT / 3 + 80.0D);
        ImageView imgViewScoreBoardUI = new ImageView(imgScoreBoard);
        imgViewScoreBoardUI.setX(25.0D);
        imgViewScoreBoardUI.setY(LevelProperties.DBL_GAME_AREA_HEIGHT / 3);
        txtMoves.setLayoutX(70.0D);
        txtMoves.setLayoutY(LevelProperties.DBL_GAME_AREA_HEIGHT / 2 + 70.0D);
        txtMoves.setFill(Color.YELLOW);//Color.web("#FFF200")
        txtMoves.setFont(Font.font("Agency FB", FontWeight.NORMAL, FontPosture.REGULAR, 40));
        Text txt_target = new Text("TARGET: 3600");
        txt_target.setFont(Font.font("Agency FB", FontWeight.BOLD, FontPosture.REGULAR, 36));
        txt_target.setX(50.0D);
        txt_target.setY(Value.DBL_PANE_CONTENT_HEIGHT * 5 / 7);
        txt_target.setFill(Color.web("#44F530"));
        pane_left.getChildren().addAll(rect_exit, txt_exit, t_level, imgViewScoreBoardUI, nlblScore, txtMoves, txt_target);
        rect_exit.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                rect_exit.setFill(Color.web("#CC9209"));
                txt_exit.setFill(Color.web("#fedfed"));
            } else {
                rect_exit.setFill(Color.web("#CC0000"));
                txt_exit.setFill(Color.web("#FFC90E"));
            }
        });
        rect_exit.setArcWidth(20.0D);
        rect_exit.setArcHeight(20.0D);
        pane_content = new Pane();
        pane_content.setBackground(Value.bg_pane_content);
        pane_content.setPrefSize(Value.DBL_PANE_CONTENT_WIDTH, Value.DBL_PANE_CONTENT_HEIGHT);
        pane_content.setMinSize(Value.DBL_PANE_CONTENT_WIDTH, Value.DBL_PANE_CONTENT_HEIGHT);
        pane_content.setMaxSize(Value.DBL_PANE_CONTENT_WIDTH, Value.DBL_PANE_CONTENT_HEIGHT);
        ImageView imgViewbg = new ImageView(imgbg);
        imgViewbg.setLayoutX(Value.DBL_PANE_LEFT_WIDTH + 5.0D);
        imgViewbg.setLayoutY(30.0D + 5.0D);
        pane_content.getChildren().addAll(pane_left, imgViewbg);
        buildGameArea();
        assignNewBlocks();
        putBlocksInBoard();
    }

    public Pane getContentPane() {
        return pane_content;
    }

    private void updateLabel() {
        txtMoves.setText("MOVES : " + ++intMoves);
    }

    private boolean isTargetAchieved() {
        return nlblScore.getCurrentValue() >= 3600L;
    }

    private void levelCompleted() {
        double d = pane_game_area.getLayoutY();
        Timeline tl = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(pane_game_area.layoutYProperty(), d)),
                new KeyFrame(Duration.millis(500.0D), new KeyValue(pane_game_area.layoutYProperty(), d + 60.00D, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(1500.0D), new KeyValue(pane_game_area.layoutYProperty(), -1000.0D, Interpolator.EASE_IN)));
        tl.playFromStart();
        tl.setOnFinished(e -> {
            Text t_won = new Text("LEVEL COMPLETED!!");
            t_won.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 60));
            t_won.setX(395.0D);
            t_won.setY(350.0D);
            t_won.setMouseTransparent(true);
            t_won.setFill(Color.web("#FFC90E"));
            t_won.setStroke(Color.RED);
            t_won.setStrokeWidth(4.0D);
            Glow g = new Glow();
            g.setLevel(-0.5D);
            t_won.setEffect(g);
            Timeline tl_won_scale = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(t_won.scaleXProperty(), 1.0D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(t_won.scaleYProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(700D), new KeyValue(t_won.scaleXProperty(), 1.4D)),
                    new KeyFrame(Duration.millis(700D), new KeyValue(t_won.scaleYProperty(), 1.4D)),
                    new KeyFrame(Duration.millis(1100D), new KeyValue(t_won.scaleXProperty(), 0.7D)),
                    new KeyFrame(Duration.millis(1100D), new KeyValue(t_won.scaleYProperty(), 0.7D)));
            Timeline t_glow = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(g.levelProperty(), -0.5D)),
                    new KeyFrame(Duration.millis(500D), new KeyValue(g.levelProperty(), 0.4D)),
                    new KeyFrame(Duration.millis(900D), new KeyValue(g.levelProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(1100D), new KeyValue(g.levelProperty(), 1.0D)));
            if (isSoundEnabled) {
                sound.sfx_start.seek(Duration.ZERO);
                sound.sfx_start.play();
                sound.sfx_start.setOnEndOfMedia(() -> {
                    sound.sfx_level_completed.play();
                });
            }
            t_glow.setAutoReverse(true);
            t_glow.setCycleCount(Timeline.INDEFINITE);
            tl_won_scale.setAutoReverse(true);
            tl_won_scale.setCycleCount(3);
            tl_won_scale.playFromStart();
            tl_won_scale.setOnFinished(eh -> {
                t_glow.playFromStart();
            });
            pane_content.getChildren().add(t_won);
        });
        Rectangle rect_next = new Rectangle(110.0D, 48.0D);
        rect_next.setFill(Color.web("#B5E61D"));
        rect_next.setX(142.0D);
        rect_next.setY(Value.DBL_PANE_CONTENT_HEIGHT - 64.0D);
        rect_next.setOnMouseClicked(e -> {
            Timeline tl_fade_out = new Timeline();
            tl_fade_out.getKeyFrames().addAll(new KeyFrame(Duration.millis(800.0D), new KeyValue(pane_content.opacityProperty(), 0.7D)),
                    new KeyFrame(Duration.millis(1200.0D), new KeyValue(pane_content.opacityProperty(), 0.3D)),
                    new KeyFrame(Duration.millis(1600.0D), new KeyValue(pane_content.opacityProperty(), 0.0D)));
            tl_fade_out.setOnFinished(eh->{
                //GameFrame.loadLevel3();
                if(sound.music_theme_track_1.getStatus() == MediaPlayer.Status.PLAYING) {
                    sound.music_theme_track_1.stop();
                }
            });
            tl_fade_out.playFromStart();
        });
        Text txt_next = new Text("NEXT");
        txt_next.setFont(Font.font("Century Gothic", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 36));
        txt_next.setX(154.0D);
        txt_next.setY(Value.DBL_PANE_CONTENT_HEIGHT - 25.0D);
        txt_next.setMouseTransparent(true);
        txt_next.setFill(Color.web("#0935E6"));
        rect_next.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                rect_next.setFill(Color.web("#CC9209"));
                txt_next.setFill(Color.web("#fedfed"));
            } else {
                rect_next.setFill(Color.web("#B5E61D"));
                txt_next.setFill(Color.web("#0935E6"));
            }
        });
        rect_next.setArcWidth(20.0D);
        rect_next.setArcHeight(20.0D);
        //pane_left.getChildren().addAll(rect_next, txt_next);
    }

    private void putBlocksInBoard() {
        pane_content.setOpacity(0.1D);
        Timeline tl_fade_in = new Timeline();
        tl_fade_in.getKeyFrames().addAll(new KeyFrame(Duration.millis(800.0D), new KeyValue(pane_content.opacityProperty(), 0.4D)),
                new KeyFrame(Duration.millis(1200.0D), new KeyValue(pane_content.opacityProperty(), 0.6D)),
                new KeyFrame(Duration.millis(1600.0D), new KeyValue(pane_content.opacityProperty(), 1.0D)));
        Timeline tl = new Timeline();
        final short ANIM_TYPE = 3;
        for (short ID = 0; ID < LevelProperties.INT_NO_OF_TILES; ID++) {
            LevelProperties.imgs[ID] = new ImageView(getBlock(b[ID].bv));
            if (ANIM_TYPE == 0) {
                tl.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].xProperty(), -LevelProperties.DBL_IMG_HEIGHT)),
                        new KeyFrame(Duration.millis(750.0D), new KeyValue(LevelProperties.imgs[ID].xProperty(), b[ID].DBL_IMG_X_LOC)),
                        new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].yProperty(), -LevelProperties.DBL_IMG_HEIGHT)),
                        new KeyFrame(Duration.millis(750.0D), new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC)));
                LevelProperties.imgs[ID].setX(-LevelProperties.DBL_IMG_HEIGHT);
                LevelProperties.imgs[ID].setY(-LevelProperties.DBL_IMG_HEIGHT);
            } else if (ANIM_TYPE == 1) {
                tl.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].yProperty(), -LevelProperties.DBL_IMG_HEIGHT)),
                        new KeyFrame(Duration.millis(750.0D), new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC)));
                LevelProperties.imgs[ID].setX(b[ID].DBL_IMG_X_LOC);
                LevelProperties.imgs[ID].setY(-LevelProperties.DBL_IMG_HEIGHT);
            } else if (ANIM_TYPE == 2) {
                tl.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].yProperty(), -LevelProperties.DBL_IMG_HEIGHT)),
                        new KeyFrame(Duration.millis((b[ID].INT_ROW_LOC + 1) * 300.0D), new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC)));
                LevelProperties.imgs[ID].setX(b[ID].DBL_IMG_X_LOC);
                LevelProperties.imgs[ID].setY(-LevelProperties.DBL_IMG_HEIGHT);
            } else if (ANIM_TYPE == 3) {
                tl.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].yProperty(), -((LevelProperties.INT_NO_OF_ROWS - b[ID].INT_ROW_LOC) * LevelProperties.DBL_IMG_HEIGHT))),
                        new KeyFrame(Duration.millis((LevelProperties.INT_NO_OF_ROWS) * 150.0D), new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC)));
                LevelProperties.imgs[ID].setX(b[ID].DBL_IMG_X_LOC);
                LevelProperties.imgs[ID].setY(-LevelProperties.DBL_IMG_HEIGHT);
            }
            pane_block.getChildren().add(LevelProperties.imgs[ID]);
            LevelProperties.imgs[ID].setVisible(false);
        }
        tl.setDelay(Duration.millis(1000D));
        tl_fade_in.setOnFinished(e -> {
            for (short ID = 0; ID < LevelProperties.INT_NO_OF_TILES; ID++) {
                LevelProperties.imgs[ID].setVisible(true);
            }
            tl.playFromStart();
        });
        tl_fade_in.playFromStart();
        tl.setOnFinished(e -> {
            bln_mouse_action_allowed = true;
            if (isSoundEnabled) {
                sound.sfx_start.play();
                sound.sfx_start.setOnEndOfMedia(() -> {
                    sound.music_theme_track_1.setCycleCount(MediaPlayer.INDEFINITE);
                    sound.music_theme_track_1.seek(Duration.ZERO);
                    sound.music_theme_track_1.play();
                });
            }
        });
    }

    private void assignNewBlocks() {
        boolean random_block_chain_fixed = false;
        ArrayList<Short> arr = new ArrayList<Short>();
        for (short sh = 0; sh < LevelProperties.INT_NO_OF_TILES; sh++) {
            arr.add(sh);
        }
        Collections.shuffle(arr);
        while (!random_block_chain_fixed) {//to ensure that there is atleast one chain possible to make.
            final short SH_SELECTED_ID = arr.get(0);
            if (b[SH_SELECTED_ID].bv == BlockVariant.NOT_AVAILABLE) {
                ArrayList<Short> arr_adjancent_sides_id = new ArrayList<Short>();
                if (b[SH_SELECTED_ID].BLN_ADJ_LEFT_AVAILABLE && b[b[SH_SELECTED_ID].LEFT_ID].bv == BlockVariant.NOT_AVAILABLE) {
                    arr_adjancent_sides_id.add(b[SH_SELECTED_ID].LEFT_ID);
                }
                if (b[SH_SELECTED_ID].BLN_ADJ_RIGHT_AVAILABLE && b[b[SH_SELECTED_ID].RIGHT_ID].bv == BlockVariant.NOT_AVAILABLE) {
                    arr_adjancent_sides_id.add(b[SH_SELECTED_ID].RIGHT_ID);
                }
                if (b[SH_SELECTED_ID].BLN_ADJ_TOP_AVAILABLE && b[b[SH_SELECTED_ID].TOP_ID].bv == BlockVariant.NOT_AVAILABLE) {
                    arr_adjancent_sides_id.add(b[SH_SELECTED_ID].TOP_ID);
                }
                if (b[SH_SELECTED_ID].BLN_ADJ_BOTTOM_AVAILABLE && b[b[SH_SELECTED_ID].BOTTOM_ID].bv == BlockVariant.NOT_AVAILABLE) {
                    arr_adjancent_sides_id.add(b[SH_SELECTED_ID].BOTTOM_ID);
                }
                if (arr_adjancent_sides_id.size() > 0) {
                    Collections.shuffle(arr_adjancent_sides_id);
                    final BlockVariant BV = getBlockVariantName(getRandomBlockVariantID());
                    b[SH_SELECTED_ID].bv = BV;
                    b[arr_adjancent_sides_id.get(0)].bv = BV;
                    random_block_chain_fixed = true;
                }
            }
            short removed_id = arr.remove(0);
        }
        for (short ID = 0; ID < LevelProperties.INT_NO_OF_TILES; ID++) {//to assign random block variant across the board(in empty cells).
            if (b[ID].bv == BlockVariant.NOT_AVAILABLE) {
                b[ID].bv = getBlockVariantName(getRandomBlockVariantID());
            }
        }
        /*b[28].bv = BlockVariant.NOT_AVAILABLE;
        b[19].bv = BlockVariant.NOT_AVAILABLE;
        b[10].bv = BlockVariant.NOT_AVAILABLE;
        b[20].bv = BlockVariant.NOT_AVAILABLE;
        b[12].bv = BlockVariant.NOT_AVAILABLE;*/
    }

    public class NumericalLabel extends Text {

        private long VALUE = 0L, OLD_VALUE = 0L;
        Timeline t = new Timeline();

        public final void setValue(final long Value) {
            OLD_VALUE = this.VALUE;
            this.VALUE = Value;
            if (this.t.getStatus() == Timeline.Status.RUNNING) {
                this.t.stop();
            }
            this.setText(String.valueOf(VALUE));
        }

        public final void setValue(final long Value, final long milliseconds) {
            if (milliseconds == 0) {
                setValue(Value);
                return;
            } else if (milliseconds < 0) {
                System.err.println("Duration \'milliseconds\' should be always positive.");
                return;
            }
            if (this.t.getStatus() == Timeline.Status.RUNNING) {
                this.t.stop();
                this.setText(String.valueOf(VALUE));
            }
            this.OLD_VALUE = this.VALUE;
            this.VALUE = Value;
            this.t.getKeyFrames().clear();
            long DIFFERENCE = this.VALUE - this.OLD_VALUE;
            for (double duration_ms = 0; duration_ms < milliseconds; duration_ms += 20) {
                this.t.getKeyFrames().add(new KeyFrame(Duration.millis(duration_ms), new KeyValue(this.textProperty(), String.valueOf((int) (OLD_VALUE + (DIFFERENCE * (duration_ms / milliseconds)))))));
            }
            this.t.getKeyFrames().add(new KeyFrame(Duration.millis(milliseconds), new KeyValue(this.textProperty(), String.valueOf((int) (VALUE)))));
            this.t.play();
        }

        public long getCurrentValue() {
            return this.VALUE;
        }

        public long getPreviousValue() {
            return this.OLD_VALUE;
        }
    }

    private Rectangle createRectangle(final double WIDTH, final double HEIGHT, final double DBL_STROKE_WIDTH, final double DBL_ARC_RADIUS, final Color bg, final Color border) {
        Rectangle rect = new Rectangle(WIDTH, HEIGHT);
        rect.setFill(bg);
        rect.setStrokeWidth(DBL_STROKE_WIDTH);
        rect.setStroke(border);
        rect.setArcWidth(DBL_ARC_RADIUS);
        rect.setArcHeight(DBL_ARC_RADIUS);
        return rect;
    }

    private void validateMove(final short ID) {
        bln_mouse_action_allowed = false;
        ArrayList<Short> clear_IDS = new ArrayList<Short>();
        clear_IDS.add(ID);
        final BlockVariant match = b[ID].bv;
        for (int index = 0; index < clear_IDS.size(); index++) {
            if (b[clear_IDS.get(index)].BLN_ADJ_LEFT_AVAILABLE && b[b[clear_IDS.get(index)].LEFT_ID].bv == match && !clear_IDS.contains(b[clear_IDS.get(index)].LEFT_ID)) {
                clear_IDS.add(b[clear_IDS.get(index)].LEFT_ID);
            }
            if (b[clear_IDS.get(index)].BLN_ADJ_RIGHT_AVAILABLE && b[b[clear_IDS.get(index)].RIGHT_ID].bv == match && !clear_IDS.contains(b[clear_IDS.get(index)].RIGHT_ID)) {
                clear_IDS.add(b[clear_IDS.get(index)].RIGHT_ID);
            }
            if (b[clear_IDS.get(index)].BLN_ADJ_TOP_AVAILABLE && b[b[clear_IDS.get(index)].TOP_ID].bv == match && !clear_IDS.contains(b[clear_IDS.get(index)].TOP_ID)) {
                clear_IDS.add(b[clear_IDS.get(index)].TOP_ID);
            }
            if (b[clear_IDS.get(index)].BLN_ADJ_BOTTOM_AVAILABLE && b[b[clear_IDS.get(index)].BOTTOM_ID].bv == match && !clear_IDS.contains(b[clear_IDS.get(index)].BOTTOM_ID)) {
                clear_IDS.add(b[clear_IDS.get(index)].BOTTOM_ID);
            }
            b[clear_IDS.get(index)].bv = BlockVariant.NOT_AVAILABLE;
        }
        if (clear_IDS.size() < 2) {
            if (isSoundEnabled) {
                sound.sfx_invalid_move.seek(Duration.ZERO);
                sound.sfx_invalid_move.play();
            }
            b[ID].bv = match;
            System.out.println("Invalid Move");
            bln_mouse_action_allowed = true;
        } else {
            if (isSoundEnabled) {
                sound.sfx_valid_move.seek(Duration.ZERO);
                sound.sfx_valid_move.play();
            }
            updateLabel();
            Timeline tls = new Timeline();
            ImageView[] temp = new ImageView[clear_IDS.size()];
            for (int i = 0; i < clear_IDS.size(); i++) {
                temp[i] = new ImageView(LevelProperties.imgs[clear_IDS.get(i)].getImage());
                LevelProperties.imgs[clear_IDS.get(i)].setImage(null);
                temp[i].setX(b[clear_IDS.get(i)].DBL_IMG_X_LOC);
                temp[i].setY(b[clear_IDS.get(i)].DBL_IMG_Y_LOC);
                pane_block.getChildren().add(temp[i]);
                tls.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(temp[i].scaleXProperty(), 1.0D)),
                        new KeyFrame(Duration.ZERO, new KeyValue(temp[i].scaleYProperty(), 1.0D)),
                        new KeyFrame(Duration.millis(300.0D), new KeyValue(temp[i].scaleXProperty(), 0.24D)),
                        new KeyFrame(Duration.millis(300.0D), new KeyValue(temp[i].scaleYProperty(), 0.24D)));
            }
            tls.setOnFinished(e -> {
                for (ImageView imgViewTemp : temp) {
                    pane_block.getChildren().remove(imgViewTemp);
                }
                moveBlocks();
            });
            int increment = clear_IDS.size() * (20 + clear_IDS.size());
            Text txt_increment = new Text("+" + increment);
            txt_increment.setFont(Font.font("Agency FB", FontWeight.BOLD, FontPosture.REGULAR, 32));
            txt_increment.setX(170.0D);
            txt_increment.setY(Value.DBL_PANE_CONTENT_HEIGHT / 2);
            txt_increment.setMouseTransparent(true);
            double ran = Math.random();
            if (ran < 0.3) {
                txt_increment.setFill(Color.web("#92E044"));
            } else if (ran < 0.67) {
                txt_increment.setFill(Color.web("#FFF502"));
            } else {
                txt_increment.setFill(Color.web("#2BEFFF"));
            }
            Timeline tl_increment = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(txt_increment.scaleXProperty(), 0.8D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(txt_increment.scaleYProperty(), 0.8D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(txt_increment.opacityProperty(), 0.8D)),
                    new KeyFrame(Duration.millis(180.0D), new KeyValue(txt_increment.scaleXProperty(), 1.2D)),
                    new KeyFrame(Duration.millis(180.0D), new KeyValue(txt_increment.scaleYProperty(), 1.2D)),
                    new KeyFrame(Duration.millis(180.0D), new KeyValue(txt_increment.opacityProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(530.0D), new KeyValue(txt_increment.scaleXProperty(), 0.7D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(530.0D), new KeyValue(txt_increment.scaleYProperty(), 0.7D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(530.0D), new KeyValue(txt_increment.opacityProperty(), 0.8D, Interpolator.EASE_BOTH)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txt_increment.opacityProperty(), 0.2D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txt_increment.scaleXProperty(), 0.5D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txt_increment.scaleYProperty(), 0.5D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txt_increment.yProperty(), Value.DBL_PANE_CONTENT_HEIGHT / 2 - 80.0D))
            );
            tl_increment.setOnFinished(e -> {
                pane_left.getChildren().remove(txt_increment);
            });
            pane_left.getChildren().add(txt_increment);
            tl_increment.playFromStart();
            nlblScore.setValue(nlblScore.getCurrentValue() + increment, 500L);
            tls.playFromStart();
        }
    }

    private final short getRandomBlockVariantID() {
        return (short) Math.floor(Math.random() * LevelProperties.SHORT_BLOCK_VARIENT_COUNT);
    }

    private final BlockVariant getBlockVariantName(final short sh_random_number) {
        switch (sh_random_number) {
            case 0:
                return BlockVariant.TRIANGULAR_CAKE_PINK;
            case 1:
                return BlockVariant.RECTANGULAR_CAKE_ORANGE;
            case 2:
                return BlockVariant.TRIANGULAR_CAKE_CHERRY;
            case 3:
                return BlockVariant.CUP_CAKE_LIGHT_BLUE_BROWN;
            case 4:
                return BlockVariant.ROUND_CAKE_GREEN;
            default:
                System.err.println("Unexpected ID requested.");
        }
        return getBlockVariantName(getRandomBlockVariantID());
    }

    private final Image getBlock(final BlockVariant BV) {
        switch (BV) {
            case TRIANGULAR_CAKE_PINK:
                return LevelProperties.img[0];
            case RECTANGULAR_CAKE_ORANGE:
                return LevelProperties.img[1];
            case TRIANGULAR_CAKE_CHERRY:
                return LevelProperties.img[2];
            case CUP_CAKE_LIGHT_BLUE_BROWN:
                return LevelProperties.img[3];
            case ROUND_CAKE_GREEN:
                return LevelProperties.img[4];
            case NOT_AVAILABLE:
                System.err.println("WARNING : NOT_AVAILABLE(null block) expected.");
                return null;
            default:
                System.err.println("Unexpected Block Variant expected : " + BV);
        }
        System.exit(0);
        return null;
    }

    private Timeline tl_move = new Timeline();

    private void moveBlocks() {
        final double DBL_PLAY_SLIDE_SPEED_MILLIS = 250.0D;
        for (short ID = (short) (LevelProperties.INT_NO_OF_TILES - 1); ID > -1; ID--) {
            if (b[ID].bv == BlockVariant.NOT_AVAILABLE) {
                if (b[ID].INT_ADJUSTANT_TOP_ID == LevelProperties.INT_NOT_AVAILABLE) {
                    //create new ones
                    b[ID].bv = getBlockVariantName(getRandomBlockVariantID());
                    tl_move.getKeyFrames().addAll(new KeyFrame(Duration.millis(1), new KeyValue(LevelProperties.imgs[ID].imageProperty(), getBlock(b[ID].bv))),
                            new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].xProperty(), b[ID].DBL_IMG_X_LOC)),
                            new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC - (LevelProperties.DBL_CELL_HEIGHT + LevelProperties.DBL_EXTRA_Y_PADDING))),
                            new KeyFrame(Duration.millis(DBL_PLAY_SLIDE_SPEED_MILLIS), new KeyValue(LevelProperties.imgs[ID].xProperty(), b[ID].DBL_IMG_X_LOC)),
                            new KeyFrame(Duration.millis(DBL_PLAY_SLIDE_SPEED_MILLIS), new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC)));
                } else if (b[b[ID].INT_ADJUSTANT_TOP_ID].bv != BlockVariant.NOT_AVAILABLE) {
                    b[ID].bv = b[b[ID].INT_ADJUSTANT_TOP_ID].bv;
                    b[b[ID].INT_ADJUSTANT_TOP_ID].bv = BlockVariant.NOT_AVAILABLE;
                    LevelProperties.imgs[b[ID].INT_ADJUSTANT_TOP_ID].setImage(null);
                    tl_move.getKeyFrames().addAll(new KeyFrame(Duration.millis(1), new KeyValue(LevelProperties.imgs[ID].imageProperty(), getBlock(b[ID].bv))),
                            new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].xProperty(), b[b[ID].INT_ADJUSTANT_TOP_ID].DBL_IMG_X_LOC)),
                            new KeyFrame(Duration.ZERO, new KeyValue(LevelProperties.imgs[ID].yProperty(), b[b[ID].INT_ADJUSTANT_TOP_ID].DBL_IMG_Y_LOC)),
                            new KeyFrame(Duration.millis(DBL_PLAY_SLIDE_SPEED_MILLIS), new KeyValue(LevelProperties.imgs[ID].xProperty(), b[ID].DBL_IMG_X_LOC)),
                            new KeyFrame(Duration.millis(DBL_PLAY_SLIDE_SPEED_MILLIS), new KeyValue(LevelProperties.imgs[ID].yProperty(), b[ID].DBL_IMG_Y_LOC)));
                }
            }
        }
        if (tl_move.getKeyFrames().size() > 0) {
            tl_move.setOnFinished(e -> {
                tl_move.getKeyFrames().clear();
                moveBlocks();
            });
            tl_move.playFromStart();
        } else {
            if (isTargetAchieved()) {
                levelCompleted();
            }
            bln_mouse_action_allowed = true;
        }
    }

    private void autoCreateGrid() {
        short[][] temp_id = new short[LevelProperties.INT_NO_OF_ROWS][LevelProperties.INT_NO_OF_COLUMNS];
        short ID = 0;
        for (short[] banned_loc : short_array_banned_location) {
            temp_id[banned_loc[0]][banned_loc[1]] = LevelProperties.SHORT_TILE_NOT_AVAILABLE;
        }
        for (short row = 0; row < LevelProperties.INT_NO_OF_ROWS; row++) {
            for (short col = 0; col < LevelProperties.INT_NO_OF_COLUMNS; col++) {
                if (temp_id[row][col] != LevelProperties.SHORT_TILE_NOT_AVAILABLE) {
                    temp_id[row][col] = ID;
                    ID++;
                }
            }
        }
        for (short row = 0; row < LevelProperties.INT_NO_OF_ROWS; row++) {
            for (short col = 0; col < LevelProperties.INT_NO_OF_COLUMNS; col++) {
                if (temp_id[row][col] != LevelProperties.SHORT_TILE_NOT_AVAILABLE) {
                    short top_id = LevelProperties.SHORT_TILE_NOT_AVAILABLE, bottom_id = LevelProperties.SHORT_TILE_NOT_AVAILABLE,
                            left_id = LevelProperties.SHORT_TILE_NOT_AVAILABLE, right_id = LevelProperties.SHORT_TILE_NOT_AVAILABLE;
                    int R = row, C = col - 1;
                    boolean bln_found = false;
                    while (!bln_found && C > -1) {//to search for left id with respect to ID
                        if (temp_id[R][C] != LevelProperties.SHORT_TILE_NOT_AVAILABLE) {
                            left_id = temp_id[R][C];
                            bln_found = true;
                        }
                        C--;
                    }
                    C = col + 1;
                    bln_found = false;
                    while (!bln_found && C < LevelProperties.INT_NO_OF_COLUMNS) {//to search for right id with respect to ID
                        if (temp_id[R][C] != LevelProperties.SHORT_TILE_NOT_AVAILABLE) {
                            right_id = temp_id[R][C];
                            bln_found = true;
                        }
                        C++;
                    }
                    C = col;
                    R = row - 1;
                    bln_found = false;
                    while (!bln_found && R > -1) {//to search for top id with respect to ID
                        if (temp_id[R][C] != LevelProperties.SHORT_TILE_NOT_AVAILABLE) {
                            top_id = temp_id[R][C];
                            bln_found = true;
                        }
                        R--;
                    }
                    R = row + 1;
                    bln_found = false;
                    while (!bln_found && R < LevelProperties.INT_NO_OF_ROWS) {//to search for bottom id with respect to ID
                        if (temp_id[R][C] != LevelProperties.SHORT_TILE_NOT_AVAILABLE) {
                            bottom_id = temp_id[R][C];
                            bln_found = true;
                        }
                        R++;
                    }
                    boolean top_available = (row > 0 && temp_id[row - 1][col] != LevelProperties.SHORT_TILE_NOT_AVAILABLE),
                            top_left_available = (row > 0 && col > 0 && temp_id[row - 1][col - 1] != LevelProperties.SHORT_TILE_NOT_AVAILABLE),
                            top_right_available = (row > 0 && (col + 1) < LevelProperties.INT_NO_OF_COLUMNS && temp_id[row - 1][col + 1] != LevelProperties.SHORT_TILE_NOT_AVAILABLE);
                    short arr_size = 0;
                    if (top_available) {
                        arr_size++;
                    }
                    if (top_left_available) {
                        arr_size++;
                    }
                    if (top_right_available) {
                        arr_size++;
                    }
                    int neighbouring_top_id = LevelProperties.INT_NOT_AVAILABLE;
                    if (top_available) {
                        neighbouring_top_id = temp_id[row - 1][col];
                    } else if (top_left_available) {
                        neighbouring_top_id = temp_id[row - 1][col - 1];
                    } else if (top_right_available) {
                        neighbouring_top_id = temp_id[row - 1][col + 1];
                    }
                    final String tile_clr_normal = "#EF88BE", tile_border_clr_normal = "#688511", tile_clr_hover = "#FFA10E", tile_border_clr_hover = "#EA3680";
                    final boolean bln_adjancent_left_avaialable = (col > 0 && temp_id[row][col - 1] != LevelProperties.SHORT_TILE_NOT_AVAILABLE),
                            bln_adjancent_right_avaialable = ((col + 1) < LevelProperties.INT_NO_OF_COLUMNS && temp_id[row][col + 1] != LevelProperties.SHORT_TILE_NOT_AVAILABLE),
                            bln_adjancent_top_avaialable = (row > 0 && temp_id[row - 1][col] != LevelProperties.SHORT_TILE_NOT_AVAILABLE),
                            bln_adjancent_bottom_avaialable = ((row + 1) < LevelProperties.INT_NO_OF_ROWS && temp_id[row + 1][col] != LevelProperties.SHORT_TILE_NOT_AVAILABLE);
                    b[temp_id[row][col]] = new Block(row, col, ((col) * (LevelProperties.DBL_X_SPACING + LevelProperties.DBL_CELL_WIDTH)) + LevelProperties.DBL_X_SPACING,
                            ((row) * (LevelProperties.DBL_Y_SPACING + LevelProperties.DBL_CELL_HEIGHT)) + LevelProperties.DBL_Y_SPACING, temp_id[row][col], left_id, right_id, top_id, bottom_id,
                            bln_adjancent_left_avaialable, bln_adjancent_right_avaialable, bln_adjancent_top_avaialable, bln_adjancent_bottom_avaialable, neighbouring_top_id);
                    Rectangle rect = createRectangle(LevelProperties.DBL_CELL_WIDTH, LevelProperties.DBL_CELL_HEIGHT, 1.2D, 0.0D, Color.web(tile_clr_normal), Color.web(tile_border_clr_normal));
                    rect.setLayoutX(((col) * (LevelProperties.DBL_X_SPACING + LevelProperties.DBL_CELL_WIDTH)) + LevelProperties.DBL_X_SPACING);
                    rect.setLayoutY(((row) * (LevelProperties.DBL_Y_SPACING + LevelProperties.DBL_CELL_HEIGHT)) + LevelProperties.DBL_Y_SPACING);
                    rect.setId(String.valueOf(temp_id[row][col]));

                    final short CID = temp_id[row][col];
                    rect.setOnMouseClicked(EH -> {
                        if (bln_mouse_action_allowed) {
                            validateMove(CID);
                        }
                    });
                    Glow g = new Glow();
                    g.setLevel(0.0D);
                    rect.setEffect(g);
                    rect.hoverProperty().addListener((ov, oldValue, newValue) -> {
                        if (newValue) {
                            rect.setStroke(Color.web(tile_border_clr_hover));//#75163F
                            rect.setFill(Color.web(tile_clr_hover));//#EA3680
                            g.setLevel(1.0D);
                        } else {
                            rect.setFill(Color.web(tile_clr_normal));//#79749E
                            rect.setStroke(Color.web(tile_border_clr_normal));//#46435C
                            g.setLevel(0.0D);
                        }
                    });
                    /*rect.setOnMouseClicked(e-> {
                        System.out.println("ID = " + CID + ", Left ID = " + b[CID].LEFT_ID + ", Right ID = " + b[CID].RIGHT_ID + ", Top ID = " + b[CID].TOP_ID + ", Bottom ID = " + b[CID].BOTTOM_ID);
                        System.out.print("ADJ ID's -> (Left,Right,Top,Bottom) : (" + (bln_adjancent_left_avaialable ? b[CID].LEFT_ID : "N/A") + ", " + (bln_adjancent_right_avaialable ? b[CID].RIGHT_ID : "N/A") +
                                ", " + (bln_adjancent_top_avaialable ? b[CID].TOP_ID : "N/A") + ", " + (bln_adjancent_bottom_avaialable ? b[CID].BOTTOM_ID : "N/A") + ")\nTops ID ->");
                        if(tops_id.length == 0 ) System.out.print(" Not available");
                        for(int i : tops_id) {
                            System.out.print(" " + i);
                        }
                        System.out.println("\n-------------------------------------------");
                    });*/
                    Tooltip tp = new Tooltip(String.valueOf("ID = " + temp_id[row][col]) + "\n (R,C) = (" + row + "," + col + ")");
                    tp.setStyle("-fx-background-color: #ff9999; -fx-text-fill: #003300;");
                    Tooltip.install(rect, tp);
                    pane_game_grid.getChildren().add(rect);
                }
            }
        }
    }

    private void buildGameArea() {
        pane_game_area = new StackPane();
        pane_game_area.setMaxSize(LevelProperties.DBL_GAME_AREA_WIDTH, LevelProperties.DBL_GAME_AREA_HEIGHT);
        pane_game_area.setMinSize(LevelProperties.DBL_GAME_AREA_WIDTH, LevelProperties.DBL_GAME_AREA_HEIGHT);
        pane_game_area.setPrefSize(LevelProperties.DBL_GAME_AREA_WIDTH, LevelProperties.DBL_GAME_AREA_HEIGHT);
        pane_game_grid = new Pane();
        pane_game_grid.setMaxSize(LevelProperties.DBL_GRID_WIDTH, LevelProperties.DBL_GRID_HEIGHT);
        pane_game_grid.setMinSize(LevelProperties.DBL_GRID_WIDTH, LevelProperties.DBL_GRID_HEIGHT);
        pane_game_grid.setPrefSize(LevelProperties.DBL_GRID_WIDTH, LevelProperties.DBL_GRID_HEIGHT);
        //pane_game_grid.setBackground(Value.bg_pane_mode_and_level_selector);
        pane_block = new Pane();
        pane_block.setMaxSize(LevelProperties.DBL_GRID_WIDTH, LevelProperties.DBL_GRID_HEIGHT);
        pane_block.setMinSize(LevelProperties.DBL_GRID_WIDTH, LevelProperties.DBL_GRID_HEIGHT);
        pane_block.setPrefSize(LevelProperties.DBL_GRID_WIDTH, LevelProperties.DBL_GRID_HEIGHT);
        pane_block.setMouseTransparent(true);
        Rectangle R = createRectangle(LevelProperties.DBL_GRID_WIDTH + LevelProperties.DBL_EXTRA_X_PADDING, LevelProperties.DBL_GRID_HEIGHT + LevelProperties.DBL_EXTRA_Y_PADDING, 8.0D, 20.0D, Color.web("#E3D4F0"), Color.web("#B521E3"));//,#948A9C
        R.setOpacity(0.8D);
        pane_game_grid.setOpacity(0.8D);
        pane_game_area.getChildren().addAll(R, pane_game_grid, pane_block);
        pane_game_area.setLayoutX(Value.DBL_PANE_LEFT_WIDTH);
        pane_content.getChildren().add(pane_game_area);
        autoCreateGrid();
    }

    private final static void loadResources() {
        LevelProperties.img = new Image[LevelProperties.SHORT_BLOCK_VARIENT_COUNT];
        LevelProperties.img[0] = new Image(LevelTwo.class.getResource("images/cakes/triangular_cake_pink.png").toString(), LevelProperties.DBL_IMG_WIDTH, LevelProperties.DBL_IMG_HEIGHT, true, true);
        LevelProperties.img[1] = new Image(LevelTwo.class.getResource("images/cakes/rectangular_cake_orange.png").toString(), LevelProperties.DBL_IMG_WIDTH, LevelProperties.DBL_IMG_HEIGHT, true, true);
        LevelProperties.img[2] = new Image(LevelTwo.class.getResource("images/cakes/triangular_cake_cherry_brown.png").toString(), LevelProperties.DBL_IMG_WIDTH, LevelProperties.DBL_IMG_HEIGHT, true, true);
        LevelProperties.img[3] = new Image(LevelTwo.class.getResource("images/cakes/cup_cake_light_blue_brown.png").toString(), LevelProperties.DBL_IMG_WIDTH, LevelProperties.DBL_IMG_HEIGHT, true, true);
        LevelProperties.img[4] = new Image(LevelTwo.class.getResource("images/cakes/round_cake_green.png").toString(), LevelProperties.DBL_IMG_WIDTH, LevelProperties.DBL_IMG_HEIGHT, true, true);
        if (Math.random() > 0.5D) {
            imgbg = new Image(LevelTwo.class.getResource("images/bg/farm1.png").toString(), LevelProperties.DBL_GAME_AREA_WIDTH - 10.0D, LevelProperties.DBL_GAME_AREA_HEIGHT + 170.0D, true, true);
        } else if (Math.random() > 0.5D) {
            imgbg = new Image(LevelTwo.class.getResource("images/bg/castle.png").toString(), LevelProperties.DBL_GAME_AREA_WIDTH - 10.0D, LevelProperties.DBL_GAME_AREA_HEIGHT + 170.0D, true, true);
        } else {
            imgbg = new Image(LevelTwo.class.getResource("images/bg/farm2.png").toString(), LevelProperties.DBL_GAME_AREA_WIDTH - 10.0D, LevelProperties.DBL_GAME_AREA_HEIGHT + 170.0D, true, true);
        }
        //if(Math.random() < 0.5D) {
        imgScoreBoard = new Image(LevelTwo.class.getResource("images/ui/score_board_1.png").toString(), 250.0D, 130.0D, true, true);
        //}
    }

    private class LevelProperties {

        private final static int INT_NOT_AVAILABLE = -908;
        public final static double DBL_CELL_WIDTH = 60.0D, DBL_CELL_HEIGHT = 60.0D, DBL_X_SPACING = 7.0D, DBL_Y_SPACING = 7.0D, DBL_EXTRA_X_PADDING = 16.0D, DBL_EXTRA_Y_PADDING = 16.0D;
        public final static int INT_NO_OF_ROWS = 7, INT_NO_OF_COLUMNS = 9;
        public final static double DBL_GRID_WIDTH = (INT_NO_OF_COLUMNS + 1) * (DBL_X_SPACING) + (INT_NO_OF_COLUMNS) * (DBL_CELL_WIDTH),
                DBL_GRID_HEIGHT = (INT_NO_OF_ROWS + 1) * (DBL_Y_SPACING) + (INT_NO_OF_ROWS) * (DBL_CELL_HEIGHT);
        public final static int INT_NO_OF_TILES = INT_NO_OF_ROWS * INT_NO_OF_COLUMNS - short_array_banned_location.length;
        public final static double DBL_GAME_AREA_WIDTH = Value.DBL_PANE_CONTENT_WIDTH - Value.DBL_PANE_LEFT_WIDTH,
                DBL_GAME_AREA_HEIGHT = Value.DBL_PANE_CONTENT_HEIGHT;
        public final static short SHORT_TILE_NOT_AVAILABLE = -99;
        public final static double DBL_IMG_WIDTH = 40.0D, DBL_IMG_HEIGHT = 40.0D;
        public final static short SHORT_BLOCK_VARIENT_COUNT = 5;
        public static Image[] img;
        public static ImageView[] imgs = new ImageView[LevelProperties.INT_NO_OF_TILES];
    }

    public class SFXPlayer {

        public final MediaPlayer sfx_start, sfx_valid_move, sfx_invalid_move, music_theme_track_1, sfx_level_completed;

        public SFXPlayer() {
            Media sfx_m1 = new Media(getClass().getResource("audio/soundfx/start.mp3").toString());
            sfx_start = new MediaPlayer(sfx_m1);
            Media sfx_m2 = new Media(getClass().getResource("audio/soundfx/valid_click.mp3").toString());
            sfx_valid_move = new MediaPlayer(sfx_m2);
            Media sfx_m3 = new Media(getClass().getResource("audio/soundfx/invalid_move.mp3").toString());
            sfx_invalid_move = new MediaPlayer(sfx_m3);
            Media sfx_m4 = new Media(getClass().getResource("audio/music/theme_track_1.mp3").toString());
            music_theme_track_1 = new MediaPlayer(sfx_m4);
            Media sfx_m5 = new Media(getClass().getResource("audio/soundfx/level_completed.mp3").toString());
            sfx_level_completed = new MediaPlayer(sfx_m5);
        }

    }

    private enum BlockVariant {
        TRIANGULAR_CAKE_PINK, TRIANGULAR_CAKE_CHERRY, CUP_CAKE_LIGHT_BLUE_BROWN, ROUND_CAKE_GREEN, RECTANGULAR_CAKE_ORANGE, NOT_AVAILABLE
    }

    private class Block {

        public final int INT_ROW_LOC, INT_COL_LOC;
        public final short ID, LEFT_ID, RIGHT_ID, TOP_ID, BOTTOM_ID;
        public final int INT_ADJUSTANT_TOP_ID;
        public final double DBL_TILE_X_LOC, DBL_TILE_Y_LOC, DBL_IMG_X_LOC, DBL_IMG_Y_LOC;
        public final boolean BLN_ADJ_LEFT_AVAILABLE, BLN_ADJ_RIGHT_AVAILABLE, BLN_ADJ_TOP_AVAILABLE, BLN_ADJ_BOTTOM_AVAILABLE;
        public BlockVariant bv = BlockVariant.NOT_AVAILABLE;

        public Block(final int R, final int C, final double DBL_X_LOC, final double DBL_Y_LOC, final short ID, final short LEFT_ID, final short RIGHT_ID,
                     final short TOP_ID, final short BOTTOM_ID, final boolean ADJ_LEFT, final boolean ADJ_RIGHT, final boolean ADJ_TOP, final boolean ADJ_BOTTOM, final int TOP) {
            this.INT_ROW_LOC = R;
            this.INT_COL_LOC = C;
            this.ID = ID;
            this.LEFT_ID = LEFT_ID;
            this.RIGHT_ID = RIGHT_ID;
            this.TOP_ID = TOP_ID;
            this.BOTTOM_ID = BOTTOM_ID;
            this.INT_ADJUSTANT_TOP_ID = TOP;
            this.DBL_TILE_X_LOC = DBL_X_LOC;
            this.DBL_TILE_Y_LOC = DBL_Y_LOC;
            this.BLN_ADJ_LEFT_AVAILABLE = ADJ_LEFT;
            this.BLN_ADJ_RIGHT_AVAILABLE = ADJ_RIGHT;
            this.BLN_ADJ_TOP_AVAILABLE = ADJ_TOP;
            this.BLN_ADJ_BOTTOM_AVAILABLE = ADJ_BOTTOM;
            this.DBL_IMG_X_LOC = this.DBL_TILE_X_LOC + (LevelProperties.DBL_CELL_WIDTH - LevelProperties.DBL_IMG_WIDTH) / 2.0D;
            this.DBL_IMG_Y_LOC = this.DBL_TILE_Y_LOC + (LevelProperties.DBL_CELL_HEIGHT - LevelProperties.DBL_IMG_HEIGHT) / 2.0D;
        }
    }
}


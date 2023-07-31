package com.example.jfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private Label songname;

    @FXML
    private Button play,pause,reset,previous,next;

    @FXML
    private ComboBox<String> speed;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProgressbar;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;
    private ArrayList<File> songs;

    private int songNumber;
    private int[] speeds={25,50,75,100,125,150,175,200};

    private Timer timer;
    private TimerTask task;
    private Boolean running;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();

        directory = new File("music");
        files=directory.listFiles();

        if(files!=null){
            for (File file:files) {

                songs.add(file);
            }
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songname.setText(songs.get(songNumber).getName());

        for (int i=0;i<speeds.length;i++){

            speed.getItems().add(Integer.toString(speeds[i])+"%");

        }
        speed.setOnAction(this::changeSpeed);
/*
      volumeSlider.valueProperty().addListener(new  ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
              mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
          }
      });
*/
    }

    public void playMedia(){
        beginTimer();
        mediaPlayer.play();
        changeSpeed(null);

    }

    public void pauseMedia(){
        cancelTimer();
        mediaPlayer.pause();
    }
    @FXML
    public void resetMedia(){

        mediaPlayer.seek(Duration.seconds(0));
    }
    @FXML

    public void previousMedia(){
        if (songNumber>0){
            songNumber--;
            mediaPlayer.stop();

            if (running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songname.setText(songs.get(songNumber).getName());
            playMedia();
        }
        else {
            songNumber=songs.size()-1;

            mediaPlayer.stop();

            if (running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songname.setText(songs.get(songNumber).getName());
            playMedia();
        }
    }
    @FXML
    public void nextMedia(){
        if (songNumber<songs.size()-1){
            songNumber++;
            mediaPlayer.stop();

            if (running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songname.setText(songs.get(songNumber).getName());
            playMedia();
        }
        else {
            songNumber=0;

            mediaPlayer.stop();

            if (running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songname.setText(songs.get(songNumber).getName());
            playMedia();
        }
    }
    @FXML
    public void changeSpeed(ActionEvent event) {
        if (speed.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            //mediaPlayer.setRate(Integer.parseInt(speed.getValue())*0.01);
            mediaPlayer.setRate(Integer.parseInt(speed.getValue().substring(0, speed.getValue().length() - 1)) * 0.01);
        }
    }
    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running=true;
                double current =mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current/end);
                songProgressbar.setProgress(current/end);

                if (current/end==1){
                    running=false;
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task,0,1000);
    }
    public void cancelTimer(){
        running=false;
        timer.cancel();
    }
}

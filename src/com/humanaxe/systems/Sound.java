package com.humanaxe.systems;

import java.applet.Applet;
import java.applet.AudioClip;
import javax.swing.JOptionPane;

public class Sound {

    private AudioClip clip;

    public static final Sound death = new Sound("/sfx/death.wav");
    public static final Sound chomp = new Sound("/sfx/chomp.wav");
    public static final Sound beginning = new Sound("/sfx/beginning.wav");
    public static final Sound eatghost = new Sound("/sfx/eatghost.wav");
    public static final Sound intermission = new Sound("/sfx/intermission.wav");

    private Sound(String name) {
        try {
            clip = Applet.newAudioClip(Sound.class.getResource(name));
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.\nMessage: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void play() {
        try {
            new Thread() {
                @Override
                public void run() {
                    clip.play();
                }
            }.start();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.\nMessage: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loop() {
        try {
            new Thread() {
                @Override
                public void run() {
                    clip.loop();
                }
            }.start();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.\nMessage: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package Util;

// Adicionamos a biblioteca para adicionar musicas
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private static Clip backgroundClip;
    // Metodo é apenas para a musica de fundo, tive de criar dois metodos um para o background e outro
    // Para os soundeffects
    public static void playBackground(String filePath) {
        try {
            // Se já houver música a tocar, para antes de começar a nova
            stopBackground();

            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioInput);

                FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);

                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMusic(String filePath) {
        try {
            File musicPath = new File(filePath);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);

                // Reduzir um pouco o volume da musica
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo serve para parar a musica mm que o jogo falhe
    public static void stopBackground() {
        if (backgroundClip != null ) {
            if (backgroundClip.isRunning()) {
                backgroundClip.stop();
            }
            backgroundClip.close();
            backgroundClip = null;
        }
    }
    // Efeito sonoro
    public void playOnce() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start(); // Toca apenas uma vez
        }
    }
}


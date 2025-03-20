package com.dumbmuttstudios.main;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private Clip clip;
	
	public static final Sound musicBackground = new Sound("/sound/music/test.wav");
	public static final Sound sfxHurt = new Sound("/sound/sfx/hurt.wav");

	private Sound(String name) {
		try {
			// TODO:
			// Atualmente, o jogo dá uma travadinha quando está carregando um som pela
			// primeira vez, talvez compense dar uma olhada em como pré-carregar os sons
			// antes do jogo começar.
			clip = AudioSystem.getClip();
			InputStream audioSrc = getClass().getResourceAsStream(name);
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
			clip.open(inputStream);
			
		} catch(Throwable e) {}
	}
	
	public void play() {
		new Thread() {
			public void run() {
				try {
					if(clip != null) {
						if(!clip.isActive()) {
							clip.setFramePosition(0);
						}
						clip.start();
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			};
		}.start();
	}
	
	
	public void loop() {
		new Thread() {
			public void run() {
				try {
					if(clip != null) {
						clip.loop(1);
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			};
		}.start();
	}
	 

	
}

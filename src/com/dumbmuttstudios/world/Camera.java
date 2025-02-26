package com.dumbmuttstudios.world;

public class Camera {
	public static int x = 0, y = 0;
	
	public static int clamp(int coordenadaAtual, int coordenadaMin, int coordenadaMax) {
		if(coordenadaAtual < coordenadaMin) {
			coordenadaAtual = coordenadaMin;
		}
		
		if(coordenadaAtual > coordenadaMax) {
			coordenadaAtual = coordenadaMax;
		}
		
		return coordenadaAtual;
	}
}

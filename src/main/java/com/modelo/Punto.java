package com.modelo;

import com.funciones.Funciones;

public class Punto {
	
    private double positionX;
    private double positionY;
    
	public Punto(double positionX, double positionY) {
		super();
		this.positionX = Funciones.aproximar1Decimal(positionX);
		this.positionY = Funciones.aproximar1Decimal(positionY);
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	@Override
	public String toString() {
		return "Punto [positionX=" + positionX + ", positionY=" + positionY + "]";
	}    
	
	
    

}

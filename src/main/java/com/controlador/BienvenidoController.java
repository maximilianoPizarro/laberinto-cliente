/*
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.controlador;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.funciones.IntValue;
import com.funciones.LongValue;
import com.modelo.Laberinto;
import com.negocio.Cliente;
import com.negocio.Semaforo;
import com.test.Sprite;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BienvenidoController implements Initializable {

	@FXML
	private Text titulo;

	@FXML
	private Text estado;

	@FXML
	private ListView<String> list = new ListView<String>();

	@FXML
	private Pane juego;

	@FXML
	private Label copyright;

	@FXML
	private Label iduser;

	@FXML
	private ImageView personaje;

	@FXML
	public Canvas canvas= new Canvas( 512, 512 );



	public void setUsername(String dato) {
		String parts[] = dato.split("=");
		this.titulo.setText(parts[0]);
		this.iduser.setText(dato.substring(dato.lastIndexOf('=') + 1));

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) { // TODO 
		run();
	}

	public void run() {
		canvas.setFocusTraversable(true);
		juego.getChildren().add(canvas);
		
		
		ArrayList<String> input = new ArrayList<String>();

		juego.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				if (!input.contains(code))
					input.add(code);
				
			}
		});

		juego.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				input.remove(code);
				
			}
		});

		GraphicsContext gc = canvas.getGraphicsContext2D();

		Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
		gc.setFont(theFont);
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);

		ArrayList<Sprite> caminos = new ArrayList<Sprite>();
		ArrayList<Sprite> ladrillos = new ArrayList<Sprite>();
		ArrayList<Sprite> guardias = new ArrayList<Sprite>();
		ArrayList<Sprite> oros = new ArrayList<Sprite>();
		ArrayList<Sprite> llaves = new ArrayList<Sprite>();

		Laberinto l = new Laberinto();
		try {
			l.rellenarLaberinto();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		l.dibujar();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				char elemento = l.getCeldas()[j][i];
				Sprite elementoSprite = new Sprite();
				switch (elemento) {
				case 'P':
					elementoSprite.setImage("/views/ladrillo.png");
					elementoSprite.setPosition(j * 50, i * 50);
					ladrillos.add(elementoSprite);
					break;
				case 'C':
					elementoSprite.setImage("/views/cesped.png");
					elementoSprite.setPosition(j * 50, i * 50);
					caminos.add(elementoSprite);
					break;
				case 'G':
					elementoSprite.setImage("/views/guardia.png");
					elementoSprite.setPosition(j * 50, i * 50);
					guardias.add(elementoSprite);
					break;
				case 'O':
					elementoSprite.setImage("/views/oro.png");
					elementoSprite.setPosition(j * 50, i * 50);
					oros.add(elementoSprite);
					break;
				case 'L':
					elementoSprite.setImage("/views/llave.png");
					elementoSprite.setPosition(j * 50, i * 50);
					llaves.add(elementoSprite);
					break;
				}

			}
		}

		Sprite briefcase = new Sprite();
		briefcase.setImage("/views/personaje.png");
		briefcase.setPosition(0, 0);

		LongValue lastNanoTime = new LongValue(System.nanoTime());

		IntValue score = new IntValue(0);

		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				// calculate time since last update.
				double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
				lastNanoTime.value = currentNanoTime;

				// game logic

				// analizo posiciones cercanas?
				
				briefcase.setVelocity(0, 0);
				if (input.contains("LEFT"))
					briefcase.addVelocity(-50, 0);
				if (input.contains("RIGHT"))
					briefcase.addVelocity(50, 0);
				if (input.contains("UP"))
					briefcase.addVelocity(0, -50);
				if (input.contains("DOWN"))
					briefcase.addVelocity(0, 50);
				
				briefcase.update(elapsedTime);
				if(!input.isEmpty())
					new Thread(new Semaforo(briefcase.toString())).start();

				// collision detection con "PARED"
				Iterator<Sprite> ladrillosIter = ladrillos.iterator();
				while (ladrillosIter.hasNext()) {
					Sprite moneybag = ladrillosIter.next();
					if (briefcase.intersects(moneybag)) {
						briefcase.setVelocity(0, 0);
						if (input.contains("LEFT"))
							briefcase.addVelocity(50, 0);
						if (input.contains("RIGHT"))
							briefcase.addVelocity(-50, 0);
						if (input.contains("UP"))
							briefcase.addVelocity(0, 50);
						if (input.contains("DOWN"))
							briefcase.addVelocity(0, -50);

						briefcase.update(elapsedTime);
					}
				}
				// Coleccion con Guardia
				Iterator<Sprite> guardiasIter = guardias.iterator();
				while (guardiasIter.hasNext()) {
					Sprite guardia = guardiasIter.next();
					if (briefcase.intersects(guardia)) {
						guardiasIter.remove();
						Sprite camino = new Sprite();
						camino.setImage("views/cesped.png");
						camino.setPosition(guardia.getX(), guardia.getY());
						caminos.add(camino);
						score.value--;

					}
				}
				// Colision con oros
				Iterator<Sprite> orosIter = oros.iterator();
				while (orosIter.hasNext()) {
					Sprite oro = orosIter.next();
					if (briefcase.intersects(oro)) {
						Sprite camino = new Sprite();
						camino.setImage("/views/cesped.png");
						camino.setPosition(oro.getX(), oro.getY());
						caminos.add(camino);
						//score.value--;
						orosIter.remove();
						score.value++;
					}
				}

				// render

				gc.clearRect(0, 0, 512, 512);

				for (Sprite elementoLaberinto : caminos)
					elementoLaberinto.render(gc);
				for (Sprite elementoLaberinto : ladrillos)
					elementoLaberinto.render(gc);
				for (Sprite elementoLaberinto : guardias) {
					elementoLaberinto.render(gc);
				}
				for (Sprite elementoLaberinto : llaves)
					elementoLaberinto.render(gc);
				for (Sprite elementoLaberinto : oros)
					elementoLaberinto.render(gc);

				briefcase.render(gc);

				String pointsText = "ORO: $" + (100 * score.value);
				gc.fillText(pointsText, 360, 36);
				gc.strokeText(pointsText, 360, 36);

			}
		}.start();
		
		
		//theScene.show();

	}

	/*
	 * @FXML public void handleOnKeyPressed(KeyEvent event) { if
	 * (event.getCode() == KeyCode.LEFT)
	 * personaje.setTranslateX(personaje.getTranslateX() - 1); if
	 * (event.getCode() == KeyCode.RIGHT)
	 * personaje.setTranslateX(personaje.getTranslateX() + 1); if
	 * (event.getCode() == KeyCode.UP)
	 * personaje.setTranslateY(personaje.getTranslateY() - 1); if
	 * (event.getCode() == KeyCode.DOWN)
	 * personaje.setTranslateY(personaje.getTranslateY() + 1);
	 * 
	 * new Thread(new Semaforo(personaje.getTranslateX() + ":" +
	 * personaje.getTranslateY())).start();
	 * 
	 * //System.out.println(personaje.getTranslateX() + ":" +
	 * personaje.getTranslateY()); //System.out.println(personaje); }
	 */

	@FXML
	protected void salir(ActionEvent event) throws IOException {
		Cliente cliente = Cliente.getInstance();

		try {
			cliente.desconectar();
			cliente.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/index.fxml"));
		juego.getChildren().remove(canvas);
		Parent rt = loader.load();
		juego.getScene().setRoot(rt);
		AppController controller = loader.<AppController>getController();

	}

	@FXML
	protected void acercaDe(ActionEvent event) {
		copyright.setVisible(true);
		copyright.setText("Universidad Nacional de Lanús, Redes y Comunicaciónes 2018");
		copyright.setAlignment(Pos.CENTER);
	}

}

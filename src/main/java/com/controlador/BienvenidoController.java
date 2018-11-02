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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.funciones.IntValue;
import com.funciones.LongValue;
import com.google.gson.Gson;
import com.modelo.Laberinto;
import com.modelo.Punto;
import com.modelo.Sprite;
import com.modelo.User;
import com.negocio.Cliente;
import com.negocio.Semaforo;
import com.modelo.Punto;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import	javafx.stage.Popup;



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
	
	public boolean encontroLlave = false;


	public void setUsername(String dato) {
		String parts[] = dato.split("=");
		this.titulo.setText(parts[0]);
		this.iduser.setText(dato.substring(dato.lastIndexOf('=') + 1));

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) { // TODO 
		run();
	}

	@SuppressWarnings("restriction")
	public void run(){
		Laberinto l = new Laberinto();
		canvas.setFocusTraversable(true);
		juego.getChildren().add(canvas);
		Cliente cliente = Cliente.getInstance();
		
		try {
			System.out.println(cliente.recibirDato());
			System.out.println("recibo!");
			
		} catch (IOException e2) {
			System.out.println("error al recibir el laberinto");
		}
		
		
		
		//PEDIR dato a partir del punto
		if(!cliente.estaCerrada()) {
			try {
				Punto p = new Punto(0,0);
				cliente.enviarDato(new Gson().toJson(new Punto(0,0)));
				System.out.println("ENVIO EL 0,0!");
				//System.out.println(cliente.recibirDato());
				l = new Gson().fromJson(cliente.recibirDato(),Laberinto.class);
				
				System.out.println("Recibo laberinto --> "); 
				l.dibujar() ;
				
			//if (cliente.recibirDato().compareTo("")!=0 && !cliente.estaCerrada()) {
				//RECIBO LA MATRIZ CORRESPONDIENTE AL PUNTO
				//}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		

    // For example
		iniciarMusica();
		
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
		int distanciaVisible = 100;
		ArrayList<Sprite> caminos = new ArrayList<Sprite>();
		ArrayList<Sprite> ladrillos = new ArrayList<Sprite>();
		ArrayList<Sprite> guardias = new ArrayList<Sprite>();
		ArrayList<Sprite> oros = new ArrayList<Sprite>();
		ArrayList<Sprite> llaves = new ArrayList<Sprite>();
		ArrayList<Sprite> metas = new ArrayList<Sprite>();
		Punto posicionAnterior = new Punto(0,0);

		
		//Laberinto l = new Laberinto();
	//	try {
		//	l.rellenarLaberinto();
		//} catch (URISyntaxException e1) {
//			e1.printStackTrace();
	//	}
		//l.dibujar();

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
				case 'M':
					elementoSprite.setImage("/views/meta.png");
					elementoSprite.setPosition(j * 50, i * 50);
					metas.add(elementoSprite);
					break;
				case 'X':  //COMO NO HAy Q BORRAR LO PISADO NO HAGO NADA
					break;
				}

			}
		}
		        Sprite briefcase = new Sprite();
		        briefcase.setImage("views/personajecaminante.png");
		        briefcase.setPosition(0, 0);
		        briefcase.setVisible();
		                
		        LongValue lastNanoTime = new LongValue( System.nanoTime() );

		        IntValue score = new IntValue(0);
		   		        
		        new AnimationTimer()
		        {
		            public void handle(long currentNanoTime)
		            {	
		                // calculate time since last update.
		                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
		                lastNanoTime.value = currentNanoTime;
		                
		                // game logic
		                
		                //analizo posiciones cercanas?
		                
		                briefcase.setVelocity(0,0);
		                if (input.contains("LEFT")) {
		                    briefcase.addVelocity(-50,0);
		                }
		                if (input.contains("RIGHT"))
		                    briefcase.addVelocity(50,0);
		                if (input.contains("UP"))
		                    briefcase.addVelocity(0,-50);
		                if (input.contains("DOWN"))
		                    briefcase.addVelocity(0,50);
		                    
		                briefcase.update(elapsedTime);
//envio coordenadas al servidor		               

						try {
							if ((((int)posicionAnterior.getPositionX()/ 50) - ((int)briefcase.getX() / 50) != 0) || (((int)posicionAnterior.getPositionY()/ 50) - ((int)briefcase.getY() / 50) != 0)) {
								//System.out.println("comparo X: " + (posicionAnterior.getPositionX())+ " "  + (int)(briefcase.getX() / 50));
								//System.out.println("comparo Y: " + (posicionAnterior.getPositionY())+ " "  + (briefcase.getY() / 50));
								posicionAnterior.setPositionX((int)briefcase.getX()/1);
								posicionAnterior.setPositionY((int)briefcase.getY()/1);
								cliente.enviarDato(new Gson().toJson(new Punto(briefcase.getX(),briefcase.getY())));
							//System.out.println(cliente.recibirDato());
								Laberinto l = new Gson().fromJson(cliente.recibirDato(),Laberinto.class);
								System.out.println("Recibo laberinto --> "); 
								l.dibujar() ;
								for (int i = 0; i < 10; i++) {
									for (int j = 0; j < 10; j++) {
										char elemento = l.getCeldas()[j][i];
										Sprite elementoSprite = new Sprite();
										switch (elemento) {
										case 'P':
											elementoSprite.setImage("/views/ladrillo.png");
											elementoSprite.setPosition(j * 50, i * 50);
											//System.out.println("ladrillos --> " + ladrillos);
											if (!ladrillos.contains(elementoSprite)) {
												ladrillos.add(elementoSprite);
											}
											break;
										case 'C':
											elementoSprite.setImage("/views/cesped.png");
											elementoSprite.setPosition(j * 50, i * 50);
											//System.out.println("caminos --> " +caminos);
											if (!caminos.contains(elementoSprite)) {
												caminos.add(elementoSprite);
											}
											break;
										case 'G':
											elementoSprite.setImage("/views/guardia.png");
											elementoSprite.setPosition(j * 50, i * 50);
											//System.out.println("guardias --> " +guardias);
											if (!guardias.contains(elementoSprite)) {
												guardias.add(elementoSprite);
											}
											break;
										case 'O':
											elementoSprite.setImage("/views/oro.png");
											elementoSprite.setPosition(j * 50, i * 50);
											//System.out.println("oros --> " +oros);
											if (!oros.contains(elementoSprite)) {
												oros.add(elementoSprite);
											}
											break;
										case 'L':
											elementoSprite.setImage("/views/llave.png");
											elementoSprite.setPosition(j * 50, i * 50);
											//System.out.println("llaves --> " +llaves);
											if (!llaves.contains(elementoSprite)) {
												llaves.add(elementoSprite);
											}
											break;
										case 'M':
											elementoSprite.setImage("/views/meta.png");
											elementoSprite.setPosition(j * 50, i * 50);
											if (!metas.contains(elementoSprite)) {
												metas.add(elementoSprite);
											}
											break;
										case 'X':  //COMO NO HAy Q BORRAR LO PISADO NO HAGO NADA
											break;
										}
									}		
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

						
						
					//if (cliente.recibirDato().compareTo("")!=0 && !cliente.estaCerrada()) {
						//RECIBO LA MATRIZ CORRESPONDIENTE AL PUNTO
						//}
       	
		                // collision detection con "PARED"
		                Iterator<Sprite> ladrillosIter = ladrillos.iterator();
		                while ( ladrillosIter.hasNext() )
		                {
		                    Sprite moneybag = ladrillosIter.next();
		                    if ( briefcase.intersects(moneybag) )
		                    {
		                    	briefcase.setVelocity(0,0);
		                        if (input.contains("LEFT"))
		                            briefcase.addVelocity(50,0);
		                        if (input.contains("RIGHT"))
		                            briefcase.addVelocity(-50,0);
		                        if (input.contains("UP"))
		                            briefcase.addVelocity(0,50);
		                        if (input.contains("DOWN"))
		                            briefcase.addVelocity(0,-50);
		                            
		                        briefcase.update(elapsedTime);
		                    }
		                }
		                //Colision con Guardia
		                Iterator<Sprite> guardiasIter = guardias.iterator();
		                while ( guardiasIter.hasNext() )
		                {
		                    Sprite guardia = guardiasIter.next();
		                    if ( briefcase.intersects(guardia) && guardia.isVisible())
		                    {
		                    	//guardiasIter.remove();
		                    	guardia.setInvisible();	
		                    	Sprite camino = new Sprite();
		                    	camino.setImage("views/cesped.png");
		                    	camino.setPosition(guardia.getX(),guardia.getY());
		    					caminos.add( camino );
		    					score.value--;
		                        
		                    }
		                }
		              //Colision con oros
		                Iterator<Sprite> orosIter = oros.iterator();
		                while ( orosIter.hasNext() )
		                {
		                    Sprite oro = orosIter.next();
		                    if ( briefcase.intersects(oro) && oro.isVisible())
		                    {
		                    	Sprite camino = new Sprite();
		                    	camino.setImage("views/cesped.png");
		                    	camino.setPosition(oro.getX(),oro.getY());
		    					caminos.add( camino );
		    				//	score.value--;
		                        //orosIter.remove();
		    					oro.setInvisible();	
		                        score.value++;
		                    }
		                }
		              //colision con llaves
		                Iterator<Sprite> llavesIter = llaves.iterator();
		                while ( llavesIter.hasNext() )
		                {
		                    Sprite llave = llavesIter.next();
		                    if ( briefcase.intersects(llave) && llave.isVisible())
		                    {
		                    	Sprite camino = new Sprite();
		                    	camino.setImage("views/cesped.png");
		                    	camino.setPosition(llave.getX(),llave.getY());
		    					caminos.add( camino );
		    					llave.setInvisible();
		    					encontroLlave = true;
			                }
	                    }
		                //colision con metas
		                Iterator<Sprite> metasIter = metas.iterator();
		                while ( metasIter.hasNext() )
		                {
		                    Sprite meta = metasIter.next();
		                    if ( briefcase.intersects(meta) && meta.isVisible())
		                    {
				                if (encontroLlave) {
				                	//ACA EN REALIDAD GANA
				                	Winner go = new Winner(canvas);
				                }
		                    }
		                }

		                
		                //renderizado
		                
		                gc.clearRect(0, 0, 512,512);
		                
		                for (Sprite elementoLaberinto : caminos ) {
		                    if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
		                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {
		                    	elementoLaberinto.setVisible();	
		                    }
		                    elementoLaberinto.render( gc );
		                }
		                for (Sprite elementoLaberinto : ladrillos ) {
		                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
		                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {	
		                		elementoLaberinto.setVisible();	
		                    }
		                    elementoLaberinto.render( gc );
		                }
		                for (Sprite elementoLaberinto : guardias ) {
//		                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
//		                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {
//		                		elementoLaberinto.setVisible();	
//		                    }
		                	if (elementoLaberinto.isVisible()) {
		                		elementoLaberinto.render( gc );
		                	}
		                }
		                for (Sprite elementoLaberinto : llaves ) {
//		                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
//		                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {
//		                		elementoLaberinto.setVisible();	
//		                    }
		                	if (elementoLaberinto.isVisible()) {
		                		elementoLaberinto.render( gc );
		                	}
		                }
		                for (Sprite elementoLaberinto : oros ) {
//		                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
//		                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {		
//		                		elementoLaberinto.setVisible();	
//		                    }
		                	if (elementoLaberinto.isVisible()) {
		                		elementoLaberinto.render( gc );
		                	}
		                }
		                for (Sprite elementoLaberinto : metas ) {
		                	if (elementoLaberinto.isVisible()) {
		                		elementoLaberinto.render( gc );
		                	}
		                }
		                
		                briefcase.render( gc );
		                
		                String pointsText = "ORO: $" + (100 * score.value);
		                gc.fillText( pointsText, 360, 36 );
		                gc.strokeText( pointsText, 360, 36 );
		                if (score.value < 0) {
		                	GameOver go = new GameOver(canvas);
   		                }

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
	
	public void iniciarMusica(){
		try {
			String musicFile = "file"+getClass().getResource("/views/soundtrack.mp3").toURI().toString().substring(7);
			Media sound = new Media(musicFile.replace("\\", "/"));		
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
		} catch (URISyntaxException e2) {
			e2.printStackTrace();
		} 
	}
	
	protected void salir() throws IOException {
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
}

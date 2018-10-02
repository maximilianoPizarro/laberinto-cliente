package com.test;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import com.funciones.LongValue;
import com.modelo.Laberinto;
import com.modelo.Sprite;
import com.funciones.IntValue;

import java.util.Scanner;

// Collect the Money Bags!
public class Example5 extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) throws URISyntaxException 
    {
    	int distanciaVisible = 100;
        theStage.setTitle( "Laberinto" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );

        ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    if ( !input.contains(code) )
                        input.add( code );
                }
            });

        theScene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    input.remove( code );
                }
            });

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setFill( Color.GREEN );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);
        
        ArrayList<Sprite> caminos = new ArrayList<Sprite>();
        ArrayList<Sprite> ladrillos = new ArrayList<Sprite>();
        ArrayList<Sprite> guardias = new ArrayList<Sprite>();
        ArrayList<Sprite> oros = new ArrayList<Sprite>();
        ArrayList<Sprite> llaves = new ArrayList<Sprite>();
        
        Laberinto l = new Laberinto();
		l.rellenarLaberinto();
		l.dibujar();
        
		for ( int i = 0 ; i < 10 ; i++) {
			for ( int j = 0 ; j < 10 ; j++) {
				char elemento = l.getCeldas()[j][i];
				Sprite elementoSprite = new Sprite();
				switch (elemento) {
				case 'P':
					elementoSprite.setImage("views/ladrillo.png");
					elementoSprite.setPosition(j*50,i*50);
					ladrillos.add( elementoSprite );
					break;
				case 'C':
					elementoSprite.setImage("views/cesped.png");
					elementoSprite.setPosition(j*50,i*50);
					caminos.add( elementoSprite );
					break;
				case 'G':
					elementoSprite.setImage("views/guardia.png");
					elementoSprite.setPosition(j*50,i*50);
					guardias.add( elementoSprite );
					break;
				case 'O':
					elementoSprite.setImage("views/oro.png");
					elementoSprite.setPosition(j*50,i*50);
					oros.add( elementoSprite );
					break;
				case 'L':
					elementoSprite.setImage("views/llave.png");
					elementoSprite.setPosition(j*50,i*50);
					llaves.add( elementoSprite );
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
                //Coleccion con Guardia
                Iterator<Sprite> guardiasIter = guardias.iterator();
                while ( guardiasIter.hasNext() )
                {
                    Sprite guardia = guardiasIter.next();
                    if ( briefcase.intersects(guardia) )
                    {
                    	guardiasIter.remove();
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
                    if ( briefcase.intersects(oro) )
                    {
                    	Sprite camino = new Sprite();
                    	camino.setImage("views/cesped.png");
                    	camino.setPosition(oro.getX(),oro.getY());
    					caminos.add( camino );
    					score.value--;
                        orosIter.remove();
                        score.value++;
                    }
                }
                
                // render
                
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
                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {
                		elementoLaberinto.setVisible();	
                    }
                    elementoLaberinto.render( gc );
                }
                for (Sprite elementoLaberinto : llaves ) {
                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {
                		elementoLaberinto.setVisible();	
                    }
                    elementoLaberinto.render( gc );
                }
                for (Sprite elementoLaberinto : oros ) {
                	if ((Math.abs(briefcase.getX() - elementoLaberinto.getX())) < distanciaVisible 
                    		&& (Math.abs(briefcase.getY() - elementoLaberinto.getY())) < distanciaVisible) {		
                		elementoLaberinto.setVisible();	
                    }
                    elementoLaberinto.render( gc );
                }
                
                briefcase.render( gc );
                
                String pointsText = "ORO: $" + (100 * score.value);
                gc.fillText( pointsText, 360, 36 );
                gc.strokeText( pointsText, 360, 36 );

            }
        }.start();

        theStage.show();
    }
}
package com.test;

import javafx.application.Application;
import javafx.stage.Stage;
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
import java.util.ArrayList;
import java.util.Iterator;
import com.funciones.LongValue;
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
    public void start(Stage theStage) 
    {
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
        
        ArrayList<Sprite> moneybagList = new ArrayList<Sprite>();
        ArrayList<Sprite> cespedbagList = new ArrayList<Sprite>();
        ArrayList<Sprite> guardiabagList = new ArrayList<Sprite>();
        
        File f = new File("C:/Users/nicolas/Documents/Facultad/redes/Proyecto/laberinto-cliente/src/main/resources/views/pared.config");
        try  (Scanner entrada = new Scanner(f)) {

            while (entrada.hasNextInt()) { //mientras queden enteros por leer
                Sprite moneybag = new Sprite();
               moneybag.setImage("views/ladrillo.png");
               double px = entrada.nextInt(); //se lee un entero del archivo
               double py = entrada.nextInt(); //se lee un entero del archivo
               moneybag.setPosition(px,py);
               moneybagList.add( moneybag );
               
            }
        } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        f = new File("C:/Users/nicolas/Documents/Facultad/redes/Proyecto/laberinto-cliente/src/main/resources/views/guardia.config");
		try (Scanner entrada = new Scanner(f)) {
			while (entrada.hasNextInt()) { // mientras queden enteros por leer
				Sprite guardia = new Sprite();
				guardia.setImage("views/guardia.png");
				double px = entrada.nextInt(); // se lee un entero del archivo
				double py = entrada.nextInt(); // se lee un entero del archivo
				   guardia.setPosition(px,py);
               cespedbagList.add( guardia );
            }
        } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		f = new File("C:/Users/nicolas/Documents/Facultad/redes/Proyecto/laberinto-cliente/src/main/resources/views/cesped.config");
		try (Scanner entrada = new Scanner(f)) {
			while (entrada.hasNextInt()) { // mientras queden enteros por leer
				Sprite cesped = new Sprite();
				cesped.setImage("views/cesped.png");
				double px = entrada.nextInt(); // se lee un entero del archivo
				double py = entrada.nextInt(); // se lee un entero del archivo
				   cesped.setPosition(px,py);
               cespedbagList.add( cesped );
            }
        } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Sprite briefcase = new Sprite();
        briefcase.setImage("views/personaje.png");
        briefcase.setPosition(0, 0);
        
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
                
                //analizo posiciones cercanas
                               
                briefcase.setVelocity(0,0);
                if (input.contains("LEFT"))
                    briefcase.addVelocity(-50,0);
                if (input.contains("RIGHT"))
                    briefcase.addVelocity(50,0);
                if (input.contains("UP"))
                    briefcase.addVelocity(0,-50);
                if (input.contains("DOWN"))
                    briefcase.addVelocity(0,50);
                    
                briefcase.update(elapsedTime);
                
                // collision detection con "PARED"
                
                Iterator<Sprite> moneybagIter = moneybagList.iterator();
                while ( moneybagIter.hasNext() )
                {
                    Sprite moneybag = moneybagIter.next();
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
                
                // render
                
                gc.clearRect(0, 0, 512,512);
                
                for (Sprite moneybag : moneybagList )
                    moneybag.render( gc );
                
                for (Sprite cespedbag : cespedbagList )
                    cespedbag.render( gc );
                
                for (Sprite guardiabag : guardiabagList )
                    guardiabag.render( gc );
                
                briefcase.render( gc );

            }
        }.start();

        theStage.show();
    }
}
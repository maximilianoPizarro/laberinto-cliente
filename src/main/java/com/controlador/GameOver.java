package com.controlador;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 *
 * @author Shekkar Raee
 */
public class GameOver {
	private Popup pp;
	public GameOver(BienvenidoController controller) {
		this.showGameOverPopup(controller);
	}
	
	/**
	 * shows game-over pop-up.
	 * @param controller 
	 */
	@SuppressWarnings("restriction")
	private void showGameOverPopup(BienvenidoController controller) {
		@SuppressWarnings("deprecation")
		BorderPane content =BorderPaneBuilder.create()
			 .minWidth(230).minHeight(130)
			 .bottom(getBottomBox(controller))
			 .center(getCenterBox())
			 .style(              "-fx-background-color:linear-gradient(darkslategrey, wheat, white);"
				  + "-fx-background-radius:7;"
				  + "-fx-border-radius:7")
			 .build();
		pp = new Popup();
		pp.setAutoHide(true);
		pp.getContent().add(content);
		pp.show(controller.canvas.getScene().getWindow());
	}
	
	/**
	 * 
	 * @return center_box
	 */
	private HBox getCenterBox() {
		@SuppressWarnings("deprecation")
		HBox center = HBoxBuilder.create()
			 .children(LabelBuilder.create().text("Game Over!!")
				.textFill(Color.BLACK)
				 .font(Font.font("System", FontWeight.MEDIUM, FontPosture.REGULAR, 30))
				 .build())
			 .alignment(Pos.CENTER)
			 .build();
		return center;
	}
	
	/**
	 * 
	 * @param canvas
	 * @return bottom_box
	 */
	private HBox getBottomBox(BienvenidoController controller) {
		@SuppressWarnings("deprecation")
		HBox bottom = HBoxBuilder.create()
			 .alignment(Pos.CENTER)
			 .spacing(10)
			 .padding(new Insets(5,5,5,5))
			 .children(/*getRestartButton(controller), */getQuitButton(controller))
			 .build();
		return bottom;
	}
	
	/**
	 * 
	 * @param controller
	 * @return restart-button
	 */
	/*
	public Button getRestartButton(BienvenidoController controller) {
		@SuppressWarnings("deprecation")
		Button restart = ButtonBuilder.create().text("Restart")
				.minWidth(50).minHeight(24)
				.font(Font.font("System", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20))
				.style("-fx-base:darkslategrey;"
				           + "-fx-background-radius:7;"
					 + "-fx-border-radius:7;")
				.onAction(e -> {

//						controller.salir();
						System.out.println("SALIR"); 
						pp.hide();
						try {
							controller.salir();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						// TODO Auto-generated catch block

				})
				.build();
		return restart;
	}
	*/
	/**
	 * 
	 * @param controller
	 * @return exit-button
	 */
	public Button getQuitButton(BienvenidoController controller) {
		@SuppressWarnings("deprecation")
		Button quit = ButtonBuilder.create().text("Exit")
				.minWidth(50).minHeight(24)
				.font(Font.font("System", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20))
				.style("-fx-base:darkslategrey;"
				           + "-fx-background-radius:7;"
					 + "-fx-border-radius:7;")
				.onAction(e -> Platform.exit())
				.build();
		return quit;
	}
}
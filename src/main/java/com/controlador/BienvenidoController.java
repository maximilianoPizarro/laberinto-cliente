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
import java.net.URL;
import java.util.ResourceBundle;

import com.negocio.Cliente;
import com.negocio.Semaforo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BienvenidoController implements Initializable{

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

	public void setUsername(String dato) {
		String parts[] = dato.split("=");
		this.titulo.setText(parts[0]);
		this.iduser.setText(dato.substring(dato.lastIndexOf('=') + 1));

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) { // TODO }
		// Facade facade= new Facade();
		Image i= new Image("/views/personaje.png");
		personaje.setImage(i);
		personaje.setFocusTraversable(true);
		

	}

	@FXML
	public void handleOnKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.LEFT)
			personaje.setTranslateX(personaje.getTranslateX() - 1);
		if (event.getCode() == KeyCode.RIGHT)
			personaje.setTranslateX(personaje.getTranslateX() + 1);
		if (event.getCode() == KeyCode.UP)
			personaje.setTranslateY(personaje.getTranslateY() - 1);
		if (event.getCode() == KeyCode.DOWN)
			personaje.setTranslateY(personaje.getTranslateY() + 1);
		
		new Thread(new Semaforo(personaje.getTranslateX() + ":" + personaje.getTranslateY())).start();

		//System.out.println(personaje.getTranslateX() + ":" + personaje.getTranslateY());
		//System.out.println(personaje);
	}
	

	@FXML
	protected void salir(ActionEvent event) throws IOException {
		 Cliente cliente=Cliente.getInstance();

         try {        	 	
				cliente.desconectar();
				cliente.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
         
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/index.fxml"));
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


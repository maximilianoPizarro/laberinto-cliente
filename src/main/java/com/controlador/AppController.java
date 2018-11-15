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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.modelo.User;
import com.negocio.Cliente;
import com.negocio.Facade;
import com.negocio.UserABM;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import org.apache.commons.codec.digest.DigestUtils;

public class AppController extends Thread implements Initializable {

	@FXML
	private Text actiontarget;

	@FXML
	private Text titulo;

	@FXML
	private Label usuarioLabel;

	@FXML
	private Text username;

	@FXML
	private TextField usuarioText;

	@FXML
	private PasswordField passwordText;

	@FXML
	private Label errorLogin;

	@FXML
	private Label copyright;

	@FXML
	private ListView<String> consolaList = new ListView<String>();

	private ArrayList<String> lista = new ArrayList<String>();

	@FXML
	private TextField consolaText;

	@FXML
	private ListView<String> list = new ListView<String>();

	public Pane getRoot() {
		return root;
	}

	public void setRoot(Pane root) {
		this.root = root;
	}

	@FXML
	private Pane root;

	@FXML
	private AnchorPane container;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {

		// Thread thread = new AppController();
		// thread.start();

		Stage stage = (Stage) root.getScene().getWindow();
		// stage.close();

		// crearInicio();
		// Login

		Facade sistema = new Facade();
		UserABM usuario = sistema.getUserABM();

		try {
			// User user = usuario.existeUsuario(usuarioText.getText(),
			// passwordText.getText());

			User user = new User();
			user.setSsoId(usuarioText.getText());
			//
			// ENCRIPTO PASSWORD
			user.setPassword(DigestUtils.md5Hex(passwordText.getText()));
			System.out.println("password encriptado: ------------>>>>>>" + DigestUtils.md5Hex(passwordText.getText()));

			Cliente cliente = Cliente.getInstance();
			if (!cliente.estaCerrada())
				cliente.conectar(user);
			// System.out.println(cliente.recibirDato());

			if (cliente.recibirDato().compareTo("autenticado") == 0 && !cliente.estaCerrada()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/bienvenido.fxml"));
				Parent rt = loader.load();
				BienvenidoController controller = loader.<BienvenidoController>getController();
				controller.setUsername("Bienvenido: " + user.getSsoId());
				root.getScene().setRoot(rt);

			} else {
				cliente.clear();
				cliente.desconectar();
				copyright.setVisible(false);
				errorLogin.setVisible(true);
				errorLogin.setAlignment(Pos.CENTER_LEFT);
				errorLogin.setText("Usuario o contraseña incorrectos");
			}
		} catch (Exception e1) {
			copyright.setVisible(false);
			errorLogin.setVisible(true);
			errorLogin.setAlignment(Pos.CENTER_LEFT);
			// errorLogin.setText("Usuario o contraseña incorrectos.");
			errorLogin.setText(e1.getMessage());
		}

	}

	@FXML
	protected void consola(ActionEvent event) {
		Stage stage = (Stage) root.getScene().getWindow();

		Cliente cliente = Cliente.getInstance();
		
		System.out.println(cliente.estaConectado());
		
		// {"ssoId":"admin","password":"21232f297a57a5a743894a0e4a801fc3"}
		if (consolaText.getText().compareTo("") != 0) {
			if (!cliente.estaConectado()) {
				//System.out.println(consolaText.getText());
				cliente.conectar(consolaText.getText());
				consolaText.clear();
				try {
					lista.add(cliente.recibirDato());
					if (mensajesDeError("ERROR 503: USUARIO Y/O CONTRASEÑA INCORRECTO/S")||mensajesDeError("ERROR 501: PROTOCOLO USUARIO INCORRECTO")|| mensajesDeError("ERROR 504: PROTOCOLO INVALIDO")
							 /*|| mensajesDeError("ERROR 500: CADENA MAL FORMADA")*/) {
						
						System.out.println("ERROR");
						cliente.desconectar();
						cliente.clear();
						consolaText.clear();
					} 
					else { //SINO HAY MENSAJE DE ERROR, autentica --> recibo matriz inicial 
						
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	
			} else {
				try { // {"positionX":0.0,"positionY":0.0}
					//if(!mensajesDeError("ERROR AL TRANSFORMAR STRING")){
					lista.add("Ingrese comando para enviar {\"positionX\":0.0,\"positionY\":0.0}");
					consolaList.setItems(FXCollections.observableArrayList(lista)); 
					consolaList.refresh();					
					cliente.enviarDato(consolaText.getText());

					lista.add(cliente.recibirDato());
					if (mensajesDeError("ERROR 503: USUARIO Y/O CONTRASEÑA INCORRECTO/S")
							|| mensajesDeError("ERROR 501: PROTOCOLO USUARIO INCORRECTO")
							|| mensajesDeError("ERROR 504: PROTOCOLO INVALIDO")
					 || mensajesDeError("ERROR 505: ERROR FORMATO PUNTO" )) {

						System.out.println("ERROR");
						cliente.desconectar();
						cliente.clear();
						consolaText.clear();
					} else { // SINO HAY MENSAJE DE ERROR, autentica --> recibo matriz inicial
						consolaList.setItems(FXCollections.observableArrayList(lista));
						consolaList.refresh();
						consolaText.clear();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// PrintToTextArea.create(consola);
				consolaList.setItems(FXCollections.observableArrayList(lista));

			}
		});

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) { // TODO }

		lista.add("Ingrese usuario {\"ssoId\":\"tuNombre\",\"password\":\"tuClave\"}");
		consolaList.setItems(FXCollections.observableArrayList(lista));

	}
	
	
	public boolean mensajesDeError(String mensaje){
		boolean respuesta=false;
		if(lista.get(lista.size() - 1).compareTo(mensaje)==0) respuesta=true;
		
		return respuesta;
	}
	

	@Override
	public void run() {
		errorLogin.setText("");
	}

	@FXML
	protected void salir(ActionEvent event) throws IOException {
		Cliente cliente = Cliente.getInstance();
		if (cliente != null) {// cliente.desconectar();
			cliente.clear();
		}

		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
		System.exit(1);

	}

	protected void salir() throws IOException {
		Cliente.getInstance().desconectar();
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
		System.exit(1);

	}

	@FXML
	protected void acercaDe(ActionEvent event) {
		copyright.setVisible(true);
		errorLogin.setVisible(true);
		errorLogin.setText("UNLa 1.0");
		errorLogin.setAlignment(Pos.CENTER);
		copyright.setText("Universidad Nacional de Lanús, Redes y Comunicaciónes 2018");
		copyright.setAlignment(Pos.CENTER);
	}

}

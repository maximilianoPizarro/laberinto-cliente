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

import javax.swing.JOptionPane;

import com.modelo.User;
import com.negocio.Cliente;
import com.negocio.Facade;
import com.negocio.UserABM;

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
	private ListView<String> list = new ListView<String>();

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
			user.setPassword(passwordText.getText());
			user.setFirstName("usuarioNombre");
			user.setLastName("usuarioApellido");
			user.setId(1);
			Cliente cliente = Cliente.getInstance();
			if(!cliente.estaCerrada())
				cliente.conectar(user);
			//System.out.println(cliente.recibirDato());		
			
			if (cliente.recibirDato().compareTo("autenticado")==0 && !cliente.estaCerrada()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/bienvenido.fxml"));
				Parent rt = loader.load();
				BienvenidoController controller = loader.<BienvenidoController>getController();
				controller.setUsername("Bienvenido: " + user.getFirstName() + " " + user.getLastName() + "=" + user.getId());
				root.getScene().setRoot(rt);

			}else{
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

	@Override
	public void initialize(URL url, ResourceBundle rb) { // TODO }

	}

	@Override
	public void run() {
		errorLogin.setText("");
	}

	@FXML
	protected void salir(ActionEvent event) throws IOException {
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

package com.test;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.EnhancedThrowableRenderer;

import com.google.gson.Gson;
import com.modelo.Punto;
import com.modelo.User;
import com.negocio.*;

public class LlamadasInvalidas {

	public static void main(String[] args) throws Exception {
		
		Cliente cliente = Cliente.getInstance();	
		String entradaTeclado = "";
		
		Scanner entradaLogin = new Scanner (System.in); //Creación de un objeto Scanner
		System.out.println("Ingrese usuario {\"ssoId\":\"tuNombre\",\"password\":\"tuClave\"}");
        entradaTeclado = entradaLogin.nextLine (); //Invocamos un método sobre un objeto Scanner
        
        cliente.conectar(entradaTeclado);
        System.out.println(cliente.recibirDato());
        //{"ssoId":"admin","password":"admin"}
		
		try {
			boolean salir = false;
			do {
				
		        Scanner entradaJuego = new Scanner (System.in); //Creación de un objeto Scanner
		        
		        System.out.println("Ingrese comando para enviar {\"positionX\":0.0,\"positionY\":0.0}");	        
		        entradaTeclado = entradaJuego.nextLine (); //Invocamos un método sobre un objeto Scanner
		        System.out.println ("Enviare \"" + entradaTeclado +"\"");
		        cliente.enviarDato(entradaTeclado);
		        System.out.println("conectado");
		        System.out.println(cliente.recibirDato());
		        
		        if (entradaTeclado.compareTo("salir") == 0) salir=true;
		        
			}while (salir != true);
	        
			
		} catch (IOException e) {
			System.err.println("no se pudo conectar con el servidor");
		}
		
		
	}

}

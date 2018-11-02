package com.test;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.EnhancedThrowableRenderer;

import com.google.gson.Gson;
import com.modelo.Punto;
import com.negocio.*;

public class LlamadasInvalidas {

	public static void main(String[] args) throws Exception {
		
		Cliente cliente = Cliente.getInstance();	
		try {
			boolean salir = false;
			do {
				cliente.conectar();
				String entradaTeclado = "";
		        Scanner entradaEscaner = new Scanner (System.in); //Creación de un objeto Scanner
		        
		        System.out.println("Ingrese comando para enviar");	        
		        entradaTeclado = entradaEscaner.nextLine (); //Invocamos un método sobre un objeto Scanner
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

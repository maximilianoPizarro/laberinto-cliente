package com.test;


import java.io.IOException;


import com.negocio.Facade;
import com.negocio.UserABM;
import com.rest.UserRest;

public class TestRestUser {
	
	public static void main(String[] args) throws Exception {
		Facade facade=new Facade();
		
		UserABM sistema= facade.getUserABM();
		System.out.println(sistema.existeUsuario("mateodecu", "123"));
		System.out.println(sistema.traerUser(1));
		System.out.println(sistema.existeUsuario("abish", "123").esAdmin());

		
	}

}

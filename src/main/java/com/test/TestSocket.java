package com.test;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestSocket {
	
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
  
  /*      URL yahoo = new URL("http://localhost:8080/RDP/");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
     */
    //	excutePost("http://www.google.com.ar","asd");
        String servidor = "localhost";
        int puerto = 8080;             // puerto de daytime

        try {
          // Se abre un socket conectado al servidor y al
          // puerto estándar de echo
          Socket socket = new Socket( servidor,puerto );
          System.out.println( "Socket Abierto." );

          // Se consigue el canal de entrada
          BufferedReader entrada = new BufferedReader(
            new InputStreamReader( socket.getInputStream() ) );

          System.out.println( "Hora actual en localhost:" );
          System.out.println( "\t"+entrada.readLine() );
          System.out.println( "Hora actual con la clase date:" );
          System.out.println( "\t" + new Date() );

          // Se cierra el canal de entrada
          entrada.close();

          // Se cierra el socket
          socket.close();
        } catch( UnknownHostException e ) {
          System.out.println( e );
          System.out.println(
            "Debes estar conectado para que esto funcione bien." );
        } catch( IOException e ) {
          System.out.println( e );
          }
    
        
}

    public static String excutePost(String targetURL, String urlParameters)
    {
      URL url;
      HttpURLConnection connection = null;  
      try {
        //Create connection
        url = new URL(targetURL);
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", 
             "application/x-www-form-urlencoded");
  			
        connection.setRequestProperty("Content-Length", "" + 
                 Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");  
  			
        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
        wr.writeBytes (urlParameters);
        wr.flush ();
        wr.close ();

        //Get Response	
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer(); 
        while((line = rd.readLine()) != null) {
          response.append(line);
          response.append('\r');
        }
        rd.close();
        return response.toString();

      } catch (Exception e) {

        e.printStackTrace();
        return null;

      } finally {

        if(connection != null) {
          connection.disconnect(); 
        }
      }
    }

    
}

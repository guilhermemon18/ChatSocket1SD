package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private Distribuidor distribuidor;
	private Registrador registrador;
	private ServerSocket serverSocket;
	
	
	//Constr�i o server para rodar.
	/*
	 * public Server() throws IOException { super(); distribuidor = new
	 * Distribuidor();
	 * 
	 * serverSocket = new ServerSocket(10000);
	 * 
	 * //O registrador ele j� tem o serversocket l� nele,ent�o d� para acessar por
	 * l�. registrador = new Registrador(distribuidor, serverSocket); Thread pilha =
	 * new Thread(registrador); pilha.start();
	 * System.out.println("Criou o server!");
	 * System.out.println("Servidor ouvindo a porta 10000"); }
	 */

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public Registrador getRegistrador() {
		return registrador;
	}


	public static void main(String[] args) throws IOException {
		//Distribuidor distribuidor = new Distribuidor();

		ServerSocket serverSocket = new ServerSocket(10000);

		//O registrador ele j� tem o serversocket l� nele,ent�o d� para acessar por l�.
		Registrador registrador = new Registrador(null, serverSocket);
		Thread pilha = new Thread(registrador);
		pilha.start();
		System.out.println("Criou o server!");
		System.out.println("Servidor ouvindo a porta 10000");
	}

	

}

package server;

import cliente.InterfazCliente;

public class Chat {
	
	public String name;
	public InterfazCliente client;
	
	//constructor
	public Chat(String name, InterfazCliente client){
		this.name = name;
		this.client = client;
	}

	
	//getters and setters
	public String getName(){
		return name;
	}
	public InterfazCliente getClient(){
		return client;
	}

}

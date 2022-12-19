package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import server.Pacote.MessageType;

/*
Obs:
Na aplicação servidora, deve existir um objeto da classe DISTRIBUIDOR que tem como
tarefa receber as mensagens dos receptores e repassá-las para os emissores.
*/
public class Distribuidor{
	private Collection<Emissor> emissores = new ArrayList<Emissor>();
	private List<User> users;
	
	// Distribuidor
	public Distribuidor(List<User> users) {
		super();
		this.users = users;
	}

	// Lista de usuarios
	public List<User> getUsers() {
		return users;
	}

	// Adiciona um emissor
	public void adicionaEmissor(Emissor emissor) {
		this.emissores.add(emissor);
	}
	
	// Remove emissor
	private void removeEmissor(Emissor emissor) {
		this.emissores.remove(emissor);
	}
	
	// Distribui mensagem
	public void distribuiMensagem(Object mensagem) throws IOException {
		Pacote msg = (Pacote) mensagem;
		
		if(msg.getTipo().equals(MessageType.DISCONNET)) {
			System.out.println("Entrou desconectar um cliente!");
			removeEmissor(new Emissor(null,msg.getIdOrigem()));
			this.users.remove(new User(msg.getIdOrigem(),null));
			List<User> teste = new LinkedList<User>(this.users);
			//teste.add(new User(1,"Guilherme"));
			//teste.add(new User(2,"Camila"));
			
			this.distribuiMensagem(new Pacote(teste));		
		}
		
		for (Emissor emissor : this.emissores) {
			emissor.envia(mensagem );
		}
	}	
}

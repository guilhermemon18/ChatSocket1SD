package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import server.Pacote.MessageType;

/*Na aplicação servidora, deve existir um objeto da classe DISTRIBUIDOR que tem como
tarefa receber as mensagens dos receptores e repassá-las para os emissores.*/
public class Distribuidor{


	private Collection<Emissor> emissores = new ArrayList<Emissor>();
	private List<User> users;
	
	public Distribuidor(List<User> users) {
		super();
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void adicionaEmissor(Emissor emissor) {
		this.emissores.add(emissor);
	}
	
	public void removeEmissor(Emissor emissor) {
		this.emissores.remove(emissor);
	}
	
	public void distribuiMensagem(Object mensagem) throws IOException {
		//System.out.println(mensagem);
		
		
		Pacote msg = (Pacote) mensagem;

		if(msg.getTipo().equals(MessageType.GETUSERS)) {
			System.out.println("Entrou enviar lista de usu[arios conectados no distribuidor!");
			System.out.println("Número do ID de destino!" + msg.getIdDestino());
			List<User> l = (List<User>) msg.getMessage();
			System.out.println("LIsta de usuários!");
			for (User user : l) {
				System.out.println(user);
			}
		}
		
		for (Emissor emissor : this.emissores) {
			emissor.envia(mensagem );
		}
	}

	
}

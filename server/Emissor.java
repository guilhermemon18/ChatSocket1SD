package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import server.Pacote.MessageType;

/*
Para cada usuário cadastrado no chat da K19 deve ser criado um objeto da classe EMISSOR.
A tarefa de um objeto da classe EMISSOR é enviar as mensagens do chat para o usuário
correspondente.*/
public class Emissor{

	private ObjectOutputStream saida;

	public Emissor(ObjectOutputStream saida) {
		this.saida = saida;
	}

	public void envia(Object mensagem) throws IOException {
		
		
		Pacote msg = (Pacote) mensagem;

		if(msg.getTipo().equals(MessageType.GETUSERS)) {
			System.out.println("Entrou enviar lista de usu[arios conectados no emissor!");
			System.out.println("Número do ID de destino!" + msg.getIdDestino());
			List<User> l = (List<User>) msg.getMessage();
			System.out.println("LIsta de usuários!");
			for (User user : l) {
				System.out.println(user);
			}
		}
		
		
		this.saida.writeObject(mensagem);
		//System.out.println("Imprimindo mensagem no emissor: " + mensagem);
	}

	@Override
	public String toString() {
		return "Emissor [saida=" + saida + "]";
	}
}

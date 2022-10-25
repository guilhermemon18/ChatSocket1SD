package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import server.Pacote;
import server.Pacote.MessageType;
import server.User;
import view.Chat;

public class ReceptorDeMensagem implements Runnable {

	private  ObjectInputStream entrada;
	private Chat telaChat;

	public ReceptorDeMensagem(ObjectInputStream entrada, Chat telaChat) {
		this.entrada = entrada;
		this.telaChat = telaChat;
	}


	public void run() {
		while (true){//this.entrada.hasNextLine()) {//enquanto a entrada tiver mensagem ele obtem a msg e coloca na tela do chat, funciona bem para o chat global.
			Pacote mensagem = null;
			try {
				mensagem = (Pacote) this.entrada.readObject();

			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//break;
			}	
			//System.out.println("Recebencdo MSG " + mensagem);
			if(mensagem != null) {

				

				if(mensagem.getTipo().equals(MessageType.GETUSERS)) {
					System.out.println("Entrou enviar lista de usu[arios conectados no receptor de msgs!");
					System.out.println("Número do ID de origem!" + mensagem.getIdDestino());
					List<User> l = (List<User>) mensagem.getMessage();
					System.out.println("LIsta de usuários!");
					if(l != null)
						for (User user : l) {
							System.out.println(user);
						}
				}


				//tratamentos por meio de ifs para entregar a msg corretamente.
				if(mensagem.getTipo().equals(MessageType.ALLUSERS)) {
					//arqui vai ter que ser feito um tratamento de acordo com o objeto que vier ;
					this.telaChat.adicionaMensagem(mensagem.getMessage().toString());
				}
				if(mensagem.getTipo().equals(MessageType.GETUSERS) &&  mensagem.getIdDestino() != null && mensagem.getIdDestino().equals(telaChat.getId())) {
					telaChat.setUsers((List<User>) mensagem.getMessage());
					System.out.println("SEtando os usu[arios na tela!");
				}
				if(mensagem.getTipo().equals(MessageType.ID)) {
					System.out.println("Setando o ID do cliente: "+ mensagem.getMessage());
					System.out.println("Merda do Id: " + mensagem.getMessage().toString());
					telaChat.setId((Integer) mensagem.getMessage());
				}
			}

		}
	}

}

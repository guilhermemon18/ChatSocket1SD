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
		while (true ){//this.entrada.hasNextLine()) {//enquanto a entrada tiver mensagem ele obtem a msg e coloca na tela do chat, funciona bem para o chat global.
			Pacote mensagem = null;
			try {
				mensagem = (Pacote) this.entrada.readObject();

			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				break;//dá um break no loop while e encerra  a Thread.
			}	
			//System.out.println("Recebencdo MSG " + mensagem);
			if(mensagem != null) {



				if(mensagem.getTipo().equals(MessageType.GETUSERS)) {
					System.out.println("Recebendo lista de usuuários conectados no receptor de msgs!");
					List<User> l = (List<User>) mensagem.getMessage();
					System.out.println("LIsta de usuários!");
					if(l != null)
						for (User user : l) {
							System.out.println(user);
						}
					telaChat.setUsers(l);
				}else {


					//tratamentos por meio de ifs para entregar a msg corretamente.
					if(mensagem.getTipo().equals(MessageType.ALLUSERS)) {
						//arqui vai ter que ser feito um tratamento de acordo com o objeto que vier ;
						String[] aux = mensagem.getMessage().toString().split(" "); 
						String newMessage = mensagem.getMessage().toString();
						if(mensagem.getIdOrigem().equals(telaChat.getId())) {
							newMessage = newMessage.replace(aux[0], "Você") + "\n"; 
						}
						else {
							newMessage = newMessage + "\n"; 
						}
						this.telaChat.adicionaMensagem(newMessage);
					}


					//				if(mensagem.getTipo().equals(MessageType.GETUSERS)) {
					//					telaChat.setUsers((List<User>) mensagem.getMessage());
					//					System.out.println("SEtando os usu[arios na tela!");
					//				}

					if(mensagem.getTipo().equals(MessageType.ID)) {
						System.out.println("Setando o ID do cliente: "+ mensagem.getMessage());
						System.out.println("Merda do Id: " + mensagem.getMessage().toString());
						telaChat.setId((Integer) mensagem.getMessage());
					}
					//conversa privada entre uma pessoa e outra.
					if(mensagem.getTipo().equals(MessageType.PRIVATE) && mensagem.getIdDestino().equals(this.telaChat.getId())) {

						telaChat.adicionaMSGPrivada(mensagem);
					}

					if(mensagem.getTipo().equals(MessageType.DISCONNET)) {
						this.telaChat.adicionaMensagem(mensagem.getMessage().toString());
						this.telaChat.desconectaConversaPrivada(mensagem);
					}

				}
			}

		}
	}

}

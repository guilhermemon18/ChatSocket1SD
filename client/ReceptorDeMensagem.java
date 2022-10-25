package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import server.Pacote;
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
					//String teste = this.entrada.readObject().toString();
					//System.out.println("Imprimindo onde a vagabunda fica enchendo o saco com String!" + teste);

					mensagem = (Pacote) this.entrada.readObject();

				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//break;
				}	
				//System.out.println("Recebencdo MSG " + mensagem);
				if(mensagem != null) {
					//tratamentos por meio de ifs para entregar a msg corretamente.
					if(mensagem.getTipo().equals(Pacote.MessageType.ALLUSERS)) {
						//arqui vai ter que ser feito um tratamento de acordo com o objeto que vier ;
						this.telaChat.adicionaMensagem(mensagem.getMessage().toString());
					}else if(mensagem.getTipo().equals(Pacote.MessageType.GETUSERS) &&  mensagem.getIdDestino() != null && mensagem.getIdDestino().equals(telaChat.getId())) {
						telaChat.setUsers((List<User>) mensagem.getMessage());
					}else if(mensagem.getTipo().equals(Pacote.MessageType.ID)) {
						System.out.println("Setando o ID do cliente: "+ mensagem.getMessage());
						telaChat.setId((Integer) mensagem.getMessage());
					}
				}
			
		}
	}

}

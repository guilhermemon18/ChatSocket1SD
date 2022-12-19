package client;

import java.io.IOException;
import java.io.ObjectOutputStream;

/*
 *Obs: Na aplicação cliente, deve existir um objeto da classe EMISSORDEMENSAGEM que envia
as mensagens digitadas pelo usuário para a aplicação servidora.
*/
public class EmissorDeMensagem {
	private ObjectOutputStream saida;

	//Construtor
	public EmissorDeMensagem(ObjectOutputStream saida) {
		this.saida = saida;
	}

	// Envia a mensagem 
	public void envia(Object mensagem) throws IOException {
		this.saida.writeObject(mensagem);
		this.saida.flush();
	}

}

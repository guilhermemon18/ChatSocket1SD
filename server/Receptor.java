package server;

import java.io.ObjectInputStream;
import java.util.List;

import server.Pacote.MessageType;

/*Para cada usuário cadastrado no chat da K19 deve ser criado um objeto da classe RECEPTOR.
A tarefa de um objeto da classe RECEPTOR é aguardar as mensagens enviadas pelo
usuário correspondente.*/
public class Receptor implements Runnable {

	/**
	 * 
	 */
	private ObjectInputStream entrada;
	private Distribuidor distribuidor;

	public Receptor(ObjectInputStream entrada, Distribuidor distribuidor) {
		this.entrada = entrada;
		this.distribuidor = distribuidor;
	}

	public void run() {
		while (true){//this.entrada.hasNextLine()) {

			try {
				Pacote mensagem = (Pacote) this.entrada.readObject();
				this.distribuidor.distribuiMensagem(mensagem);
				//this.distribuidor.distribuiMensagem(new Pacote(this.distribuidor.getUsers()));//teste enviar os clientes para todo mundo
				//sempre que uma msg é enviada.

			} catch (Exception e) {
				// TODO Auto-generated catch block

				//e.printStackTrace();
				break;
			}

		}
	}	
}


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

				if(mensagem.getTipo().equals(MessageType.GETUSERS)) {
					System.out.println("Entrou enviar lista de usu[arios conectados no receptor!");
					System.out.println("Número do ID de origem!" + mensagem.getIdOrigem());
					List<User> l = this.distribuidor.getUsers();
					System.out.println("LIsta de usuários!");
					for (User user : l) {
						System.out.println(user);
					}
					Pacote enviar = new Pacote(mensagem.getIdOrigem(),this.distribuidor.getUsers(),MessageType.GETUSERS);
					//this.distribuidor.distribuiMensagem(new Pacote(mensagem.getIdOrigem(),this.distribuidor.getUsers()));
					System.out.println("Id de destino : " + enviar.getIdDestino());
					this.distribuidor.distribuiMensagem(enviar);
				}else {
					this.distribuidor.distribuiMensagem(mensagem);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}	
}


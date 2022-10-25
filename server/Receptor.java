package server;

import java.io.ObjectInputStream;

/*Para cada usu�rio cadastrado no chat da K19 deve ser criado um objeto da classe RECEPTOR.
A tarefa de um objeto da classe RECEPTOR � aguardar as mensagens enviadas pelo
usu�rio correspondente.*/
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}	
}


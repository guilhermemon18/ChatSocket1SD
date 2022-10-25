package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*Na aplica��o servidora, deve existir um objeto da classe DISTRIBUIDOR que tem como
tarefa receber as mensagens dos receptores e repass�-las para os emissores.*/
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
		System.out.println(mensagem);
		for (Emissor emissor : this.emissores) {
			emissor.envia(mensagem );
		}
	}

	
}

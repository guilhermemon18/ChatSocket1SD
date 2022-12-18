package server;

import java.io.IOException;
import java.io.ObjectOutputStream;

/*
Para cada usuário cadastrado no chat da K19 deve ser criado um objeto da classe EMISSOR.
A tarefa de um objeto da classe EMISSOR é enviar as mensagens do chat para o usuário
correspondente.*/
public class Emissor{

	private ObjectOutputStream saida;
	private Integer id;

	public Emissor(ObjectOutputStream saida,Integer id) {
		this.saida = saida;
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void envia(Object mensagem) throws IOException {

		
		this.saida.writeObject(mensagem);
		this.saida.flush();
		//System.out.println("Imprimindo mensagem no emissor: " + mensagem);
	}

	@Override
	public String toString() {
		return "Emissor [saida=" + saida + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Emissor m = (Emissor) obj;
		return this.id.equals(m.id);
	}




}

package server;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
		this.saida.writeObject(mensagem);
		System.out.println("Imprimindo mensagem no emissor: " + mensagem);
	}

	@Override
	public String toString() {
		return "Emissor [saida=" + saida + "]";
	}
}

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/*Na aplica��o servidora, um objeto registrador deve esperar novos usu�rios do chat 
e realizar todo processo de registro de novos usu�rios quando algu�m chegar.*/
public class Registrador implements Runnable{

	private Distribuidor distribuidor;
	private ServerSocket serverSocket;
	private List<User> clients;
	private Integer nextId;

	public Registrador(Distribuidor distribuidor, ServerSocket serverSocket) {
		this.distribuidor = distribuidor;
		this.serverSocket = serverSocket;
		this.clients = new LinkedList<User>();
		this.distribuidor = new Distribuidor(clients);
		this.nextId = 1;
	}



	//vai precisar alocar um ID inteiro para cada cliente no servidor, para poder diferenciar eles de cada um, na apresenta��o na tela ser� mostrado o 
	//nome do cliente seguido do ID dele, com isso ser� poss�vel dizer para quem vai ser mandada a msg privada.
	//quando se instancia uma conversa com um usu�rio privado j� se cria uma tela que guarda essas caracteristicas para se comunicar c
	//com o destinat�rio,
	//quando mandar a msg para ele, a msg deve ser formatada especificamente para isso para que se saiba 
	//que � para um alvo �nico, l� deve ser feito o tratamento e receber a msg;
	//a msg pode ser encaminhada para todos e s� ser ignorada igual vimos em aula
	//o controle pode ser feito por "all" indicando que � para todos, ent�o s� receber todo mundo e colocar na tela
	//principal se for diferente disso, precisa ter o id e nome do remetente e o destinat�rio para fazer a l�gica da inser��o
	//no chat do destinat�rio.
	//Ent�o quando o usuario solitica uma conversa privada ele manda o remetente que pediu isso, o id e o nome do cliente
	//ent�o o servidor manda uma msg de volta para esse usu�rio com a lista de usu�rios conectados no servidor socket;
	//somente ele pode aceitar a msg porque s� tem o nome dele.
	//os ponto e virgulas v�o auxiliar na montagem da msg com a sua estrutura.
	/*Conclus�o: toda a msg deve ter o id e o nome do usu�rio do remetente  para saber de quem ela veio e o destinat�rio para saber 
	 * para quem ela � destinada, remetente e destinat�rio podem ser o server com o ID = 0 indicando isso, indicando que foi solicitado
	 * algo ao server ex: lista de usu�rio conectados nele;
	 * o server ao aceitar uma conex�o com o usu�rio ele manda o ID para o cliente e l� ele trata e cria a conex�o com esse ID.
	 * O ideal � criar um classe com toda essa estrutura por tr�s e fazer uma esp�cie de template das msgs a serem enviadas
	 *  e serializar ela e trocar os objetos e n�o mais Strings, garantindo assim uma organiza��o melhor da coisa.
	 *  ent�o a solu��o fica bem melhor e mais robusta.
	 *  Quando um cliente recebe uma msg privada � so gerar um comando com uma tela para aquele cliente e manter a conversa nela;
	 *  
	 */
	public void run()  {
		while (true) {
			try {
				//aceita a conex�o e retorna uma referencia para o socket do novo cliente que foi criado l� pela classe Client.
				Socket socket = this.serverSocket.accept();


				//provavelmente ser� aqui o controle dos clientes conectados, aqui ele pega uma nova conex�o com o socket e cria emissores e receptores para ela.


				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
					// ... 
					Pacote p = (Pacote) entrada.readObject();
					String nomeClient = p.getMessage().toString();//entrada.readLine();
					
					System.out.println("Cliente conectado: "+ nomeClient + " " + socket.getInetAddress().getHostAddress());
					saida.writeObject(new Pacote(nextId));
					Receptor receptor = new Receptor(entrada, this.distribuidor);
					Thread pilha = new Thread(receptor);
					pilha.start();
					Emissor emissor = new Emissor(saida);

					clients.add(new User(this.nextId++, nomeClient));//adiciona o novo cliente a lista de clientes do server.


					this.distribuidor.adicionaEmissor(emissor);
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("ERRO");
			}
		}

	}
}

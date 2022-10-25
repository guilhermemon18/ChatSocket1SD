package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/*Na aplicação servidora, um objeto registrador deve esperar novos usuários do chat 
e realizar todo processo de registro de novos usuários quando alguém chegar.*/
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



	//vai precisar alocar um ID inteiro para cada cliente no servidor, para poder diferenciar eles de cada um, na apresentação na tela será mostrado o 
	//nome do cliente seguido do ID dele, com isso será possível dizer para quem vai ser mandada a msg privada.
	//quando se instancia uma conversa com um usuário privado já se cria uma tela que guarda essas caracteristicas para se comunicar c
	//com o destinatário,
	//quando mandar a msg para ele, a msg deve ser formatada especificamente para isso para que se saiba 
	//que é para um alvo único, lá deve ser feito o tratamento e receber a msg;
	//a msg pode ser encaminhada para todos e só ser ignorada igual vimos em aula
	//o controle pode ser feito por "all" indicando que é para todos, então só receber todo mundo e colocar na tela
	//principal se for diferente disso, precisa ter o id e nome do remetente e o destinatário para fazer a lógica da inserção
	//no chat do destinatário.
	//Então quando o usuario solitica uma conversa privada ele manda o remetente que pediu isso, o id e o nome do cliente
	//então o servidor manda uma msg de volta para esse usuário com a lista de usuários conectados no servidor socket;
	//somente ele pode aceitar a msg porque só tem o nome dele.
	//os ponto e virgulas vão auxiliar na montagem da msg com a sua estrutura.
	/*Conclusão: toda a msg deve ter o id e o nome do usuário do remetente  para saber de quem ela veio e o destinatário para saber 
	 * para quem ela é destinada, remetente e destinatário podem ser o server com o ID = 0 indicando isso, indicando que foi solicitado
	 * algo ao server ex: lista de usuário conectados nele;
	 * o server ao aceitar uma conexão com o usuário ele manda o ID para o cliente e lá ele trata e cria a conexão com esse ID.
	 * O ideal é criar um classe com toda essa estrutura por trás e fazer uma espécie de template das msgs a serem enviadas
	 *  e serializar ela e trocar os objetos e não mais Strings, garantindo assim uma organização melhor da coisa.
	 *  então a solução fica bem melhor e mais robusta.
	 *  Quando um cliente recebe uma msg privada é so gerar um comando com uma tela para aquele cliente e manter a conversa nela;
	 *  
	 */
	public void run()  {
		while (true) {
			try {
				//aceita a conexão e retorna uma referencia para o socket do novo cliente que foi criado lá pela classe Client.
				Socket socket = this.serverSocket.accept();


				//provavelmente será aqui o controle dos clientes conectados, aqui ele pega uma nova conexão com o socket e cria emissores e receptores para ela.


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

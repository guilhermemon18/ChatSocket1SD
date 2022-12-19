package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import server.Pacote;
import view.Chat;

public class Client {

	//Componentes do cliente:
	private Socket socket;				// aqui está a conexão que deve ser encerrada ao terminar o chat.
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	private EmissorDeMensagem emissor;
	private Chat telaChat;				//tela para visualizar as ações
	private ReceptorDeMensagem receptor;//receptor para receber as informações e setar na tela.
	private Thread pilha;				//a thread para que flua.

	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome;

	//Construtor do cliente.
	public Client(String nomeCliente) throws UnknownHostException, IOException {
		socket = new Socket("localhost", 10000);
		saida = new ObjectOutputStream(socket.getOutputStream());
		entrada = new ObjectInputStream(socket.getInputStream());
		emissor = new EmissorDeMensagem(saida);
		telaChat = new Chat(emissor,nomeCliente, this);
		emissor.envia(new Pacote(nomeCliente));
		receptor = new ReceptorDeMensagem(entrada,
				telaChat);
		pilha = new Thread(receptor);
		pilha.start();
	}

	//Construtor do cliente.
	public Client() throws UnknownHostException, IOException {
		JLabel lblMessage = new JLabel("Criando Cliente!");
		//txtIP = new JTextField();
		JLabel lblinserirnome = new JLabel("Insira o nome!");
		txtNome = new JTextField();
		Object[] texts = {lblMessage, lblinserirnome, txtPorta, txtNome };
		JOptionPane.showMessageDialog(null, texts);
		socket = new Socket("localhost", 10000);


		saida = new ObjectOutputStream(socket.getOutputStream());
		entrada = new ObjectInputStream(socket.getInputStream());
		emissor = new EmissorDeMensagem(saida);
		emissor.envia(new Pacote(txtNome.getText()));
		telaChat = new Chat(emissor,txtNome.getText(), this);
		receptor = new ReceptorDeMensagem(entrada,
				telaChat);

		pilha = new Thread(receptor);
		pilha.start();

	}

	// Finaliza o chat 
	public void fecharChat() throws IOException {
		saida.close();
		entrada.close();
		socket.close();
	}

	public Socket getSocket() {
		return socket;
	}

	public static void main(String[] args)  {
		try {
			// Cria novos clientes
			new Client();
			System.out.println("Criando os cliente");
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

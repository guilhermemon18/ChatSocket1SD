package server;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class Pacote implements Serializable {
	private static final long serialVersionUID = 1L;
	public enum MessageType{PRIVATE,GETUSERS,ALLUSERS,ID,DISCONNET,NAME}
	//caracter�sticas b�sicas para a comunica��o entre dois clientes;
	private Integer idOrigem;
	private Integer idDestino;	//null indica solicita��o ao servidor
	private String nomeOrigem;
	private String nomeDestino;
	private Object mensagem;	//msg em si, pode ser qualquer coisa, String, Lista de usu�rios etc...
	private LocalTime hora;		//hora da msg
	private MessageType tipo;
	
	//Para construir uma msg de um usu�rio para o outro.
	public Pacote(Integer idOrigem, Integer idDestino, String nomeOrigem, String nomeDestino, String mensagem,
			LocalTime hora) {
		super();
		this.idOrigem = idOrigem;
		this.idDestino = idDestino;
		this.nomeOrigem = nomeOrigem;
		this.nomeDestino = nomeDestino;
		this.mensagem = mensagem;
		this.hora = hora;
		this.tipo = MessageType.PRIVATE;
	}

	// Construtor
	public Pacote(String nomeOrigem) {
		super();
		this.idOrigem = null;
		this.idDestino = null;
		this.nomeOrigem = nomeOrigem;
		this.nomeDestino = null;
		this.mensagem = nomeOrigem;
		this.hora = null;
		this.tipo = MessageType.NAME;
	}

	
	//Construtor para solicitar apenas os usu�rios conectados, uma msg que solitica os usu�rios a partir de um id;
	public Pacote(Integer idOrigem) {
		super();
		this.idOrigem = idOrigem;
		this.hora = null;
		this.idDestino = null;
		this.tipo = MessageType.GETUSERS;
		nomeOrigem = null;
		nomeDestino = null;
		mensagem = null;//msg em si		
	}
	
	//Construtor pacote para desconectar um cliente.
	public Pacote(Integer idOrigem,String nomeOrigem, LocalTime horaDesconectado) {
		super();
		this.idOrigem = idOrigem;
		this.hora = horaDesconectado;
		this.idDestino = null;
		this.tipo = MessageType.DISCONNET;
		this.nomeOrigem = nomeOrigem;
		nomeDestino = null;
		mensagem = "Desconectado";//msg em si		
	}
	
	// Construtor
	public Pacote(Integer idOrigem, MessageType t) {
		super();
		this.idOrigem = idOrigem;
		this.hora = null;
		this.idDestino = null;
		this.tipo = t;
		nomeOrigem = null;
		nomeDestino = null;
		mensagem = null;//msg em si		
	}
	
	// Construtor
	public Pacote(Integer idDestino, Object message,MessageType t) {
		super();
		this.idOrigem = null;
		this.hora = null;
		this.idDestino = idDestino;
		this.tipo = t;
		nomeOrigem = null;
		nomeDestino = null;
		mensagem = message;//msg em si		
	}
	

	//Construtor para enviar uma mensagem para todos.
	public Pacote(Integer idOrigem, String nomeOrigem, String mensagem, LocalTime hora) {
		super();
		this.idOrigem = idOrigem;
		this.nomeOrigem = nomeOrigem;
		this.mensagem = mensagem;
		this.hora = hora;
		this.tipo = MessageType.ALLUSERS;
		this.idDestino = null;
		this.nomeDestino = null;
	
//		System.out.println("Construtor para todos os usu�rios recebdndo a msg criptografada!");
//		System.out.println("Mensagem criptografada: " + this.mensagem);
//		AES aes = new AES();
//		this.mensagem = aes.Decriptar(this.mensagem.toString(), nomeOrigem);
//		System.out.println("Mensagem decriptografada: " + this.mensagem);		
	}
	
	//Construtor que enviar a lista de usu�rios para todo mundo.
	public Pacote(List<User> users) {
		super();
		this.idOrigem = null;	//indica que o servidor quem mandou!
		this.idDestino = null;
		this.nomeOrigem = null; //servidor quem mandou
		this.mensagem = users;
		this.hora = null;
		this.tipo = MessageType.GETUSERS;
		this.nomeDestino = null;
	}
	

	//getters para obter as informa��es
	public Integer getIdOrigem() {
		return idOrigem;
	}

	public Integer getIdDestino() {
		return idDestino;
	}

	public String getNomeOrigem() {
		return nomeOrigem;
	}

	public String getNomeDestino() {
		return nomeDestino;
	}

//	public Object getMensagem() {
//		return mensagem;
//	}

	public LocalTime getHora() {
		return hora;
	}

	public MessageType getTipo() {
		return tipo;
	}
	
	// Pega as mensagens e retorna para o chat de destino. Antes decripta a mensagem. 
	//Obtem a msg: faz a l�gica e dependendo do pacote d� uma msg diferente para cada situa��o requisitada.
	//getMessage � chamado sempre q se quer obter a msg, ele pode ser chamado v�rias vezes, por isso n�o � bom alterar
	//as vari�veis da classe, pois prejudicaria a pr�xima execu��o, uma msg decriptografada e armazenada na vari�vel this.mensagem
	//seria chamadada novamente para ser decriptografada j� estando assim.
	public Object getMessage() {
		if(this.tipo.equals(MessageType.GETUSERS) || this.tipo.equals(MessageType.ID)  || 
				this.tipo.equals(MessageType.NAME)) {
			return this.mensagem;
		}else if(this.tipo.equals(MessageType.ALLUSERS) || this.tipo.equals(MessageType.DISCONNET)){
			
			/** Decripitar texto da mensagem aqui **/			
			System.out.println("\nChat publico\nGetMessage decriptografando:");
			System.out.println("Mensagem a ser decriptografada: " + this.mensagem);
			/** Decripitar texto da mensagem aqui **/
			AES aes = new AES();
			String aux  = aes.Decriptar(this.mensagem.toString(), nomeOrigem);
			System.out.println("Mensagem decriptografada: " + aux);
			
			return this.nomeOrigem + " " + this.hora.toString().substring(0,8) + ": " + aux;
		}//else {
//			return this.hora.toString().substring(0, 8) + ": " + this.mensagem;
		
		/** Decripitar texto da mensagem aqui para chat privado **/			
		System.out.println("\nChat privado\nGetMessage decriptografando:");
		System.out.println("Mensagem a ser decriptografada: " + this.mensagem);
		/** Decripitar texto da mensagem aqui **/
		AES aes = new AES();
		String aux  = aes.Decriptar(this.mensagem.toString(), nomeOrigem);
		System.out.println("Mensagem decriptografada: " + aux);
		
		return this.nomeOrigem + " " + this.hora.toString().substring(0,8) + ": " + aux;
//		}
	}

	@Override
	public String toString() {
		return (String) getMessage();
		
	}
	
	
	
	
	
	
}

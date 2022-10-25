package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import client.Client;
import client.EmissorDeMensagem;
import server.Pacote;
import server.User;

public class Chat {

	private String nome;
	private Integer id;
	private final JFrame frame;
	private final JPanel panel;
	private final JScrollPane scrollPane;
	private final JTextArea textArea1;
	private final JLabel label1;
	private final JTextField textField;
	private final JButton button;
	private final Client client;
	private final EmissorDeMensagem emissorDeMensagem;
	private JPanel panel_1;
	private JPanel panel_2;
	private JButton btnLimpar;
	private JPanel panel_3;
	private JLabel lblChatPrivado;
	private JComboBox<User> cbParticipantes;
	private JLabel lblchatGeral;

	public Chat(EmissorDeMensagem emissor, String nome, Client client) {
		this.nome = nome;
		this.emissorDeMensagem = emissor;
		this.client = client;
		this.id = null;

		this.frame = new JFrame( nome);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Entrou janela fechando!");
				LocalTime agora = LocalTime.now();
				//emissorDeMensagem.envia( "msg;" + agora + ";" + "Desconectado" + ";" + nome);
				textField.setText("");
				try {
					Chat.this.client.fecharChat();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("Erro ao desconectar cliente!");
					e1.printStackTrace();
				}
			}
		});
		this.panel = new JPanel();
		this.textArea1 = new JTextArea(10, 60);
		this.textArea1.setEditable(false);
		this.scrollPane = new JScrollPane(this.textArea1);


		this.frame.setContentPane(this.panel);
		panel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		this.textField = new JTextField(60);
		panel_1.add(textField, BorderLayout.CENTER);
		textField.setForeground(Color.BLACK);
		this.label1 = new JLabel("Digite uma mensagem + Enter...");
		panel_1.add(label1, BorderLayout.WEST);

		panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.EAST);
		this.button = new JButton("Enviar");
		panel_2.add(button);


		btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea1.setText("");
			}
		});
		panel_2.add(btnLimpar);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){  
					try {
						enviarMensagem();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});


		this.panel.add(this.scrollPane);

		panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		lblChatPrivado = new JLabel("Chat Privado: Escolha um participante");
		lblChatPrivado.setToolTipText("Iniciar conversa privada com outro participante");
		lblChatPrivado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblChatPrivado.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblChatPrivado, BorderLayout.NORTH);

		cbParticipantes = new JComboBox();
		cbParticipantes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Pacote p = new Pacote(Chat.this.id);
				System.out.println("Requisitando a lista de clientes! ");
				try {
					emissorDeMensagem.envia(p);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_3.add(cbParticipantes, BorderLayout.CENTER);

		lblchatGeral = new JLabel("Chat para todos:");
		lblchatGeral.setHorizontalAlignment(SwingConstants.CENTER);
		lblchatGeral.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_3.add(lblchatGeral, BorderLayout.SOUTH);

		class EnviaMensagemListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				try {
					enviarMensagem();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		this.button.addActionListener(new EnviaMensagemListener());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(700, 300);
		this.frame.setVisible(true);

	}

	public void appendToPane(JTextPane tp, String txt, Color clr) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, clr);
		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Serif");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		tp.setCharacterAttributes(aset, false);
		tp.replaceSelection(txt);
	}

	public void adicionaMensagem(String mensagem) {
		String[] aux = mensagem.split(" ");
		if(aux[0].equalsIgnoreCase(nome)) {
			this.textArea1.append(mensagem.replace(aux[0], "Voc�") + "\n");
		}else {
			this.textArea1.append(mensagem + "\n");
		}


	}

	private void enviarMensagem() throws IOException {//arrumar isso ainda.
		LocalTime agora = LocalTime.now();
		
		String msg = textField.getText();
		Pacote p = new Pacote(this.id, this.nome, msg, agora);
		System.out.println("enviando msg do cliente: " + p.getMessage());
		emissorDeMensagem.envia(p);

		//		emissorDeMensagem.envia("msg;" + agora + ";" + textField.getText() + ";" + nome);
		textField.setText("");
	}

	public void setUsers(List<User> l)  {
		for (User user : l) {
			cbParticipantes.addItem(user);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		if(this.id == null) {
			this.id = id;
			System.out.println("Imprimindo o id do cliente " + nome + " " + this.id);
		}
	}

}




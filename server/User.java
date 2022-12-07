package server;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	
	public User(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	public Integer getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	@Override
	public String toString() {
		return  id + "-" + nome;
	}
	@Override
	public boolean equals(Object obj) {
		User u = (User) obj;
		return u != null && this.id.equals(u.id);
	}
	
	
}

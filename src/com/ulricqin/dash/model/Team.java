package com.ulricqin.dash.model;

public class Team implements java.io.Serializable {

	private static final long serialVersionUID = -8072209407169886500L;
	
	private int id;
	private String name;

	public Team() {
		
	}
	
	public Team(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + "]";
	}
	
}

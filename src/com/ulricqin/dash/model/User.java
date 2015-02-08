package com.ulricqin.dash.model;

public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1674809083412836887L;

	private int id;
	private String name;
	private String cnname;
	private String email;
	private String phone;
	private String im;
	private String qq;
	private int role;
	private int creator;
	private String created;

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

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", cnname=" + cnname
				+ ", email=" + email + ", phone=" + phone + ", im=" + im
				+ ", qq=" + qq + ", role=" + role + ", creator=" + creator
				+ ", created=" + created + "]";
	}
	
	

}

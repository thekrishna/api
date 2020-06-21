package com.kk.event.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table
public class Actor {
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "login")
	private String login;
	@Column(name = "avatar")
	@JsonProperty("avatar_url")
	private String avatar;

	public Actor() {
	}

	public Actor(Long id, String login, String avatar) {
		this.id = id;
		this.login = login;
		this.avatar = avatar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}

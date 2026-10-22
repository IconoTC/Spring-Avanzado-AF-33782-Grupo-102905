package com.example.domain.entities.models;

import org.springframework.data.annotation.PersistenceCreator;

import com.example.domain.entities.Actor;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;

@Data
public class ActorDTO {
	private int id;
	private String nombre;
	private String apellidos;
	
	@JsonCreator
	public ActorDTO() {
	}
	
	@PersistenceCreator
	public ActorDTO(int actorId, String firstName, String lastName) {
		super();
		this.id = actorId;
		this.nombre = firstName;
		this.apellidos = lastName;
	}

	public static ActorDTO from(Actor target) {
		return new ActorDTO(target.getActorId(), target.getFirstName(), target.getLastName());
	}

	public static Actor from(ActorDTO target) {
		return new Actor(target.getId(), target.getNombre(), target.getApellidos());
	}
}

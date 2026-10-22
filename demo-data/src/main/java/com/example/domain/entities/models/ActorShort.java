package com.example.domain.entities.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.example.domain.entities.Actor;

@Projection(name = "actor-short", types = { Actor.class })
public interface ActorShort {
	int getActorId();
	@Value("#{target.lastName + ', ' + target.firstName}")
	String getNombre();
}

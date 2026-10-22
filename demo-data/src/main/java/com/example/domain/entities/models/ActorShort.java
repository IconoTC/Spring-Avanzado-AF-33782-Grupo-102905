package com.example.domain.entities.models;

import org.springframework.beans.factory.annotation.Value;

public interface ActorShort {
	int getActorId();
	@Value("#{target.firstName + ' ' + target.lastName}")
	String getNombre();
}

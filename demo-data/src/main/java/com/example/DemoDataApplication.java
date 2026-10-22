package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActoresRepository;
import com.example.contracts.domain.repositories.CategoriasRepository;
import com.example.domain.entities.Actor;
import com.example.domain.entities.models.ActorDTO;
import com.example.domain.entities.models.ActorShort;
import com.fasterxml.jackson.annotation.JsonProperty;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;

@SpringBootApplication
public class DemoDataApplication {
	@Autowired @Lazy
	DemoDataApplication self;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoDataApplication.class, args);
	}
	
//	record SoloNombre(String firstName, @Value("#{target.lastName}") String apellidos) {}
	record SoloNombre(@JsonProperty("nombre") String firstName, @JsonProperty("apellidos") String lastName) {}
	
//	@Bean
	CommandLineRunner consultas(ActoresRepository dao) {
		return arg -> {
//			System.out.println(dao.count());
//			System.out.println(dao.findById(1).get());
//			var item = dao.findById(1000);
//			if(item.isPresent())
//				IO.println(item.get());
//			else {
//				IO.println("Actor no encontrado");
//			}
//			dao.findAll().forEach(System.out::println);
//			dao.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(System.out::println);
//			dao.findTop5ByFirstNameStartingWith("P", Sort.by("FirstName").ascending()).forEach(System.out::println);
//			dao.findByActorIdGreaterThanEqual(199).forEach(System.out::println);
//			dao.findNovedadesJPQL(199).forEach(System.out::println);
//			dao.findNovedadesSQL(199).forEach(System.out::println);
//			dao.findAll((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("actorId"), 199)).forEach(System.out::println);
//			dao.findByActorIdGreaterThanEqual(199).forEach(a -> {
//				System.out.println(a);
//				a.getFilmActors().forEach(p -> {
//					System.out.println("\t" + p.getFilm().getTitle());
//				});				
//			});
//			self.conexionAbierta(dao);
//			try {
//				self.transaccion(dao);
//				System.err.println("OK");
//			} catch (Exception ex) {
//				System.err.println(ex.getMessage());
//			}
			dao.findByActorIdGreaterThanEqual(199).forEach(p -> System.out.println(ActorDTO.from(p)));
			dao.readByActorIdGreaterThanEqual(199).forEach(System.out::println);
			dao.queryByActorIdGreaterThanEqual(199).forEach(p -> System.out.println("%d %s".formatted(p.getActorId(), p.getNombre())));
			dao.findByActorIdGreaterThanEqual(199, ActorDTO.class).forEach(System.out::println);
			dao.findByActorIdGreaterThanEqual(199, ActorShort.class).forEach(p -> System.out.println("%d %s".formatted(p.getActorId(), p.getNombre())));
			dao.findByActorIdGreaterThanEqual(199, SoloNombre.class).forEach(System.out::println);
//			dao.findByActorIdGreaterThanEqual(199).forEach(System.out::println);
		};
	}
	
	@Transactional
	void conexionAbierta(ActoresRepository dao) {
			dao.findByActorIdGreaterThanEqual(199).forEach(a -> {
				System.out.println(a);
//				a.getFilmActors().forEach(p -> {
//					System.out.println("\t" + p.getFilm().getTitle());
//				});				
			});
	}
	
	@Transactional(rollbackFor = {Exception.class})
	void transaccion(ActoresRepository dao) {
		dao.save(new Actor("pepito", "grillo"));
		dao.save(new Actor("otro", "grillo"));
		dao.deleteById(1);
	}
	
//	@Bean
	CommandLineRunner validaciones(ActoresRepository dao) {
		return arg -> {
			var a = new Actor("PP", "12345678z");
			if(a.isInvalid()) {
				System.err.println(a.getErrorsMessage());
			} else
			try {
				a = dao.save(a);				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		};
	}
	
//	@Bean
	CommandLineRunner crud(ActoresRepository dao) {
		return arg -> {
			var a = new Actor("pepito", "grillo");
			a = dao.save(a);
			var id = a.getActorId();
			System.out.println(a);
			var item = dao.findById(id);
			if(item.isPresent()) {
				var actor = item.get();
				actor.setFirstName(actor.getFirstName().toUpperCase());
				a = dao.save(actor);
			} else {
				IO.println("Actor no encontrado");
			}
			item = dao.findById(id);
			System.out.println(a);
			dao.deleteById(id);
			if(!dao.existsById(id)) {
				IO.println("Actor borrado");
			}
//			dao.findAll().forEach(System.out::println);
		};
	}
	@Bean
	CommandLineRunner serializacion(ActoresRepository dao, CategoriasRepository cat, JsonMapper json, XmlMapper xml) {
		return arg -> {
			var item = cat.findById(1).get();
//			IO.println(json.writeValueAsString(item));
//			IO.println(xml.writeValueAsString(item));
//			IO.println(json.writeValueAsString(cat.findAll()));
//			IO.println(json.writeValueAsString(dao.findByActorIdGreaterThanEqual(204, ActorDTO.class)));
//			IO.println(json.writeValueAsString(dao.findByActorIdGreaterThanEqual(204, ActorShort.class)));
//			IO.println(json.writeValueAsString(dao.findByActorIdGreaterThanEqual(204, SoloNombre.class)));
			var dto = json.readValue("{\"apellidos\":\"grillo\",\"id\":208,\"nombre\":\"pepito\"}", ActorDTO.class);
			IO.println(dto);
			IO.println(ActorDTO.from(dto));
//			IO.println(json.writeValueAsString(dao.findByActorIdGreaterThanEqual(204)));
		};
	}

}

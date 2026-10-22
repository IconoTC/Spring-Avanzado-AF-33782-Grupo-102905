package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActoresRepository;
import com.example.domain.entities.Actor;

@SpringBootApplication
public class DemoDataApplication {
	@Autowired @Lazy
	DemoDataApplication self;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoDataApplication.class, args);
	}
	
	@Bean
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

}

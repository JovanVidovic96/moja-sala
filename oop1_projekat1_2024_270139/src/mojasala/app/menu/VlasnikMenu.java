package mojasala.app.menu;

import java.util.Scanner;

import mojasala.model.korisnik.Vlasnik;
import mojasala.model.sala.Sala;
import mojasala.service.SalaService;

public class VlasnikMenu {
	
	private static Scanner sc = new Scanner(System.in);
	private static SalaService salaService = new SalaService();
	
	public static void show(Vlasnik v) {
		while(true) {
			System.out.println("\n--- Vlasnik ---");
			System.out.println("1. Dodaj salu");
			System.out.println("2. Prikazi sale");
			System.out.println("0. Logout");
			
			int izbor = Integer.parseInt(sc.nextLine());
			
			switch (izbor) {
			case 1 -> dodajSalu();
			case 2 -> salaService.getAllSale().forEach(s -> System.out.println(s.getNaziv()));
			case 0 -> {return;}
			}
		}
	}
	
	private static void dodajSalu() {
		System.out.println("Naziv: ");
		String naziv = sc.nextLine();
		
		System.out.println("Povrsina sale (m2): ");
		double povrsina = Double.parseDouble(sc.nextLine());
		
		System.out.println("Kapacitet: ");
		int kapacitet = Integer.parseInt(sc.nextLine());
		
		System.out.println("Klimatizovana?");
	    System.out.println("1. Da");
	    System.out.println("2. Ne");
	    int klimaIzbor = Integer.parseInt(sc.nextLine());
	    boolean klima = (klimaIzbor == 1);

	    System.out.println("Ima rasvetu?");
	    System.out.println("1. Da");
	    System.out.println("2. Ne");
	    int rasvetaIzbor = Integer.parseInt(sc.nextLine());
	    boolean rasveta = (rasvetaIzbor == 1);
		
		Sala s = new Sala(naziv, povrsina, kapacitet, klima, rasveta);
		System.out.println(salaService.addSala(s) ? "Sala dodata" : "Sala vec postoji");
	}
}

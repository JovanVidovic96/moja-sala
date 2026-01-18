package mojasala.app.menu;

import java.util.Scanner;

import mojasala.model.korisnik.Korisnik;
import mojasala.model.korisnik.Vlasnik;
import mojasala.model.korisnik.Zakupac;
import mojasala.service.AuthService;


public class LoginMenu {
	
	private static Scanner sc = new Scanner(System.in);
	private static AuthService authService = new AuthService();
	
	public static Korisnik login() {

	    while (true) {
	        System.out.println("\n --- Dobrodosli ---");
	        System.out.println("1. Login");
	        System.out.println("2. Registracija zakupca");
	        System.out.println("3. Registracija vlasnika");
	        System.out.println("0. Izlaz");

	        System.out.print("Izaberite opciju: ");
	        int izbor = Integer.parseInt(sc.nextLine());

	        switch (izbor) {
	            case 1 -> {
	                return doLogin(); 
	            }
	            case 2 -> {
	                registerZakupac();
	            }
	            case 3 -> {
	                registerVlasnik();
	            }
	            case 0 -> {
	                System.out.println("Dovidjenja");
	                System.exit(0);
	            }
	            default -> System.out.println("Nepostojeca opcija.");
	        }
	    }
	}

	
	
	private static Korisnik doLogin() {
		System.out.println("Username: ");
		String username = sc.nextLine();
		
		System.out.println("Password: ");
		String password = sc.nextLine();
		
		Korisnik k = authService.login(username, password);
		
		if(k == null) {
			System.out.println("Pogresili ste username ili password.");
		}
		return k;
	}
	
	// registracija zakupca
	private static void registerZakupac() {
		System.out.println("Username: ");
		String username = sc.nextLine();
		
		System.out.println("Password: ");
		String password = sc.nextLine();
		
		System.out.println("Telefon: ");
		String telefon = sc.nextLine();
		
		boolean ok = authService.registerZakupac(username, password, telefon);
		
		System.out.println(ok ? "Zakupac uspesno registrovan." : "Korisnik sa tim username-om vec postoji.");
	}
	
	//registracija vlasnika
	private static void registerVlasnik() {
		System.out.println("Username: ");
		String username = sc.nextLine();
		
		System.out.println("Password: ");
		String password = sc.nextLine();
		
		boolean ok = authService.registerVlasnik(username, password);
		
		System.out.println(ok ? "Vlasnik uspesno registrovan" : "Vlasnik sa tim username-om vec postoji.");
		
	}
	
	public static void redirect(Korisnik k) {
		if(k instanceof Zakupac z) {
			ZakupacMenu.show(z);
		} else if(k instanceof Vlasnik v) {
			VlasnikMenu.show(v);
		}
	}
}

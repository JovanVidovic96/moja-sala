package mojasala.app;

import mojasala.app.menu.LoginMenu;
import mojasala.model.korisnik.Korisnik;

public class Platforma {
	
 
	

	public static void main(String[] args) {
		
		while(true) {
			Korisnik korisnik = LoginMenu.login();
			if(korisnik == null) {continue;}
			
			LoginMenu.redirect(korisnik);
			
		}
		
	}

}

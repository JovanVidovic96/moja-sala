package mojasala.model.korisnik;

import mojasala.database.CsvSerializable;

public abstract class Korisnik implements CsvSerializable{
	protected String username;
	protected String password;
	
	public Korisnik(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

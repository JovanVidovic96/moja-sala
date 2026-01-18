package mojasala.repository;

import java.util.List;
import mojasala.database.CsvUtil;

public abstract class CsvRepository <T>{
	protected String filePath;

	public CsvRepository(String filePath) {
		super();
		this.filePath = filePath;
	}
	
 

	protected List<String> readLines(){
		return CsvUtil.read(filePath);
	}
	
	protected void writeLines(List<String> lines) {
		CsvUtil.write(filePath, lines);
	}
	
	public abstract List<T> findAll();
}

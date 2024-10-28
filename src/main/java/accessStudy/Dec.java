package accessStudy;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import net.ucanaccess.jdbc.JackcessOpenerInterface;

public class Dec implements JackcessOpenerInterface {

	
	@Override
	public Database open(File file, String password) throws IOException {
		DatabaseBuilder db_ = new DatabaseBuilder(file);
		db_.setAutoSync(false);
		db_.setCodecProvider(new CryptCodecProvider(password));
		db_.setReadOnly(false);
		return db_.open();
	}
	
	
}
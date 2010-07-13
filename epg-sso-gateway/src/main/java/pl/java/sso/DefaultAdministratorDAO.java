package pl.java.sso;

import java.util.List;
import java.util.Map;

public class DefaultAdministratorDAO implements AdministratorDAO {

	@Override
	public Administrator getByLogin(String login) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public List<Administrator> findByProperties(Class aClass, Map<String, Object> params) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}

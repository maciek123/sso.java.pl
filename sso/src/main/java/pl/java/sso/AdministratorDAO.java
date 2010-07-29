package pl.java.sso;

import java.util.List;
import java.util.Map;

public interface AdministratorDAO {

	abstract Administrator getByLogin(String login);

	public List<Administrator> findByProperties(Class aClass, Map<String, Object> params);

	public List<String> findRolesForAdmin(Administrator admin);
}

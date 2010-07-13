package pl.java.sso;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DefaultAdministratorDAO implements AdministratorDAO {

	@PersistenceContext(unitName = "dataPU")
	EntityManager em;

	@Override
	public Administrator getByLogin(String login) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public List<Administrator> findByProperties(Class aClass, Map<String, Object> params) {
		return em.createQuery("from Administrator").getResultList();
	}
}

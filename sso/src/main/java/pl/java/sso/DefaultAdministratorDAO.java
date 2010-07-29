package pl.java.sso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DefaultAdministratorDAO implements AdministratorDAO {

	@PersistenceContext(unitName = "dataPU")
	EntityManager em;

	/*	public DefaultAdministratorDAO() {
	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dataPU");
	em = entityManagerFactory.createEntityManager();
	} */
	@Override
	public Administrator getByLogin(String login) {
		return (Administrator) em.createQuery("from Administrator where login = '" + login + "'").getResultList().get(0);
	}

	@Override
	public List<Administrator> findByProperties(Class aClass, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder("from Administrator where ");
		for (String param : params.keySet()) {
			sb.append(param).append("='").append(params.get(param).toString()).append("'");
		}

		return em.createQuery(sb.toString()).getResultList();
	}

	@Override
	public List<String> findRolesForAdmin(Administrator admin) {
		return em.createNativeQuery("select r.name from josso_role as r join josso_user_role as ur on ur.role_id =  r.id where ur.user_id = 2;").getResultList();
	}
}

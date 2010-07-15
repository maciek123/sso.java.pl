package pl.java.sso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class DefaultAdministratorDAO implements AdministratorDAO {

//	@PersistenceContext(unitName = "dataPU")
	EntityManager em;

	public DefaultAdministratorDAO() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dataPU");
		em = entityManagerFactory.createEntityManager();
	}

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

	public String sayHello() {
		if (em != null) {
			Administrator a = em.find(Administrator.class, new Long(2));
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("email", "m.kazirod@softwaremind.pl");
			return "Hello !!" + a.getLogin() + getByLogin("user1").getId();
		} else{
			return "null";
		}
	}
}

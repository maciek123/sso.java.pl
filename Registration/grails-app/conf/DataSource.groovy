dataSource {
	pooled = true
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/test"
			driverClassName = "com.mysql.jdbc.Driver"
			dialect ="org.hibernate.dialect.MySQLDialect"
			username = "root"
			password = "admin"
		}
	}
	test {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:hsqldb:mem:testDb"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/test"
			driverClassName = "com.mysql.jdbc.Driver"
			dialect ="org.hibernate.dialect.MySQLDialect"
			username = "root"
			password = "admin"
		}
	}
}
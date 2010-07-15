// Place your Spring DSL code here
beans = {
	   myAuthenticationManager(org.josso.spring.acegi.JOSSOAuthenticationManager) {
      // attributes
	   }
	   myAuthenticationProvider(pl.java.sso.DefaultSSOIdentityProvider) {
		// attributes
	}
}

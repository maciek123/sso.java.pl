package registration

import java.security.MessageDigest;

class User {

	String username
	String password
	String email
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	static transients = ['passwordConfirm']

	static constraints = {
		username(size:3..15, blank:false, unique:true)
		password(size:5..255, blank:false, password:true)
		email(email:true, blank:false, unique:true)
	}
	

	static mapping = {
		version false
		table "josso_user"
		password column: '`password`'
		username column: '`login`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	def beforeInsert =  { hash() }
//	def beforeUpdate = {hash()}

	def hash =
	{
		def messageDigest = MessageDigest.getInstance("SHA1")
		messageDigest.update( password.getBytes() );
	//	def sha1Hex = new BigInteger(1, messageDigest.digest()).toString(16).padLeft( 40, '0' )
		def sha1Hex = new BigInteger(1, messageDigest.digest()).toString(16)
		password = sha1Hex.toString()

	}
}
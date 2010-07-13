package registration

class Role {

	String authority
	String decription

	static mapping = {
		cache true
		version false
		table "josso_role"
		authority column:  "`name`"
	}

	static constraints = {
		authority blank: false, unique: true
	}
}

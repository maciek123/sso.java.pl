package pl.java.sso;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "josso_user")
//@NamedQueries({
//	@NamedQuery(name = "JossoUser.findAll", query = "SELECT j FROM JossoUser j")})
public class Administrator implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;
	@Basic(optional = false)
	@Column(name = "account_expired")
	private boolean accountExpired;
	@Basic(optional = false)
	@Column(name = "account_locked")
	private boolean accountLocked;
	@Basic(optional = false)
	@Column(name = "email")
	private String email;
	@Basic(optional = false)
	@Column(name = "enabled")
	private boolean enabled;
	@Basic(optional = false)
	@Column(name = "password")
	private String password;
	@Basic(optional = false)
	@Column(name = "password_expired")
	private boolean passwordExpired;
	@Basic(optional = false)
	@Column(name = "login")
	private String login;

	public Administrator() {
	}

	public Administrator(Long id) {
		this.id = id;
	}

	public Administrator(Long id, boolean accountExpired, boolean accountLocked, String email, boolean enabled, String password, boolean passwordExpired, String login) {
		this.id = id;
		this.accountExpired = accountExpired;
		this.accountLocked = accountLocked;
		this.email = email;
		this.enabled = enabled;
		this.password = password;
		this.passwordExpired = passwordExpired;
		this.login = login;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Administrator)) {
			return false;
		}
		Administrator other = (Administrator) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "pl.java.sso.JossoUser[id=" + id + "]";
	}

	public String getPasswordHash() {
		return password;
	}

	public String getPasswordSalt() {
		return "";
	}
}

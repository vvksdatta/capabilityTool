package se.bth.didd.wiptool.auth.jwt;

import java.security.Principal;

/**
 * Represents an authenticated user and their associated roles. Created by
 */
public class User implements Principal {

	private final long id;
	private final String name;
	private final String role;

	public User(long id, String name, String role) {
		this.id = id;
		this.name = name;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}

		User exampleUser = (User) o;

		if (id != exampleUser.id) {
			return false;
		}
		if (name != null ? !name.equals(exampleUser.name) : exampleUser.name != null) {
			return false;
		}
		return role != null ? role.equals(exampleUser.role) : exampleUser.role == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (role != null ? role.hashCode() : 0);
		return result;
	}
}

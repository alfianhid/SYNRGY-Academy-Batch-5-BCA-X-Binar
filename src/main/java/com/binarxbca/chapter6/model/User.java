package com.binarxbca.chapter6.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "email" }) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends DateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "first_name")
	@Size(max = 40)
	private String firstName;

	@NotBlank
	@Column(name = "last_name")
	@Size(max = 40)
	private String lastName;

	@NotBlank
	@Column(name = "username")
	@Size(max = 15)
	private String username;

	@NotBlank
	@Size(max = 40)
	@Column(name = "email")
	@Email
	private String email;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(max = 100)
	@Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable( name = "userxrole",
			joinColumns = @JoinColumn(name ="user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Film> films;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invoice> invoices;

	public List<Film> getFilms() {
		return films == null ? null : new ArrayList<>(films);
	}

	public void setFilms(List<Film> films) {
		if (films == null) {
			this.films = null;
		} else {
			this.films = Collections.unmodifiableList(films);
		}
	}

	public List<Invoice> getInvoices() {
		return invoices == null ? null : new ArrayList<>(invoices);
	}

	public void setInvoices(List<Invoice> invoices) {
		if (invoices == null) {
			this.invoices = null;
		} else {
			this.invoices = Collections.unmodifiableList(invoices);
		}
	}

	public User(Long id, String firstName, String lastName, String username, String email, String password, Set<Role> roles) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
}

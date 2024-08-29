package com.example.shop.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "phone")
	private String phone;

	@Column(columnDefinition = "boolean default false")
	private boolean isDelete;
	
	@OneToMany(mappedBy = "user")
	private Set<Order> orders;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Cart cart;

	@OneToMany(mappedBy = "user")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Address> addresses;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

}

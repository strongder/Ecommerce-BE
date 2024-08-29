package com.example.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	
	@Id 
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	@Column (name = "city")
	private String city ;

	@Column (name = "district")
	private String district;

	@Column (name = "ward")
	private String ward;

	@Column (name = "address_detail")
	private String addressDetail;

	@Column( columnDefinition = "boolean default false")
	private boolean isDelete;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@ToString.Exclude
	private User user;
	
	@OneToMany(mappedBy = "address")
	private Set<Order> orders;
}

package com.example.shop.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
	
	@Id 
	
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (name = "name")
	private String name;

	private String description;

	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	
	@OneToMany(mappedBy = "category")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Product> products;


}

package com.example.shop.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	@Id 
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;

	@Column (name = "name")
	private String name ;

	@Column (name = "price")
	private Double price;

	@Column (name = "rating")
	private Float rating;

	@Column (name = "stock")
	@Min(value = 0, message = "Stock must be greater than or equal to 0")
	private Integer stock;

	@Column (name = "description")
	private String description;
	
	@Column( columnDefinition = "boolean default false")
	private boolean isDelete;

	@Column (name = "created_at")
	private LocalDateTime createdAt;
	@Column (name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "category_id")

	private Category category;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<VarProduct> varProducts;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<ImageProduct> imageUrls;

}

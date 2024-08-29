package com.example.shop.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;

	@Column (name = "quantity")
	private int quantity;

	private Double price;

	@Column(name = "is_delete", columnDefinition = "boolean default false")
	private boolean isDelete;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "var_product_id")
	private VarProduct varProduct;

}

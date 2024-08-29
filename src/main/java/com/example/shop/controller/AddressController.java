package com.example.shop.controller;

import com.example.shop.dtos.request.AddressRequest;
import com.example.shop.dtos.response.AddressResponse;
import com.example.shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@PostMapping("/{userId}")
	public ResponseEntity<AddressResponse> create(@PathVariable("userId") Long userId, @RequestBody AddressRequest request) {
		AddressResponse result = addressService.create(userId, request);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<AddressResponse> update(@PathVariable("id") Long id, @RequestBody AddressRequest request) {
        AddressResponse result = addressService.update(id, request);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<AddressResponse>> getAddressByUser(@PathVariable("userId") Long userId) {

		List<AddressResponse> addressDTOS = addressService.getAddressByUser(userId);
		return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AddressResponse> getById(@PathVariable("id") Long id) {

        AddressResponse addressDTO = addressService.getById(id);
		return new ResponseEntity<>(addressDTO, HttpStatus.OK);
	}

	//xóa địa chỉ theo Id(xóa mềm)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<AddressResponse> deleteById(@PathVariable Long id) {

        AddressResponse addressDTO = addressService.delete(id);
		return new ResponseEntity<>(addressDTO, HttpStatus.OK);
	}

}

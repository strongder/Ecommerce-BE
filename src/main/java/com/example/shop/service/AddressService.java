package com.example.shop.service;

import com.example.shop.convert.AddressConvert;
import com.example.shop.dtos.request.AddressRequest;
import com.example.shop.dtos.response.AddressResponse;
import com.example.shop.exception.AppException;
import com.example.shop.exception.ErrorResponse;
import com.example.shop.model.Address;
import com.example.shop.model.User;
import com.example.shop.repository.AddressRepository;
import com.example.shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class AddressService {

	private AddressRepository addressRepository;
	private UserRepository userRepository;
	private AddressConvert addressConvert;

	@Transactional(readOnly = true)
    public AddressResponse getById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            return addressConvert.convertToDTO(address.get());
        }
        throw new AppException(ErrorResponse.ADDRESS_NOT_EXISTED);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Set<Address> addresses = addressRepository.findByUser(user.get());
            return addresses.stream().map(address -> addressConvert.convertToDTO(address)).collect(Collectors.toList());
        } else {
            throw new AppException( ErrorResponse.USER_NOT_EXISTED);
        }
    }

	@Transactional
	public AddressResponse delete(Long id) {
		Optional<Address> existedAddress = addressRepository.findById(id);
		if (existedAddress.isPresent()) {
			existedAddress.get().setDelete(true);
			Address address = addressRepository.save(existedAddress.get());
			return addressConvert.convertToDTO(address);
		}
		throw new AppException(ErrorResponse.ADDRESS_NOT_EXISTED);
	}

    @Transactional
	public AddressResponse update(Long id, AddressRequest request) {
		Address address = addressRepository.findById(id)
				.orElseThrow(()-> new AppException(ErrorResponse.ADDRESS_NOT_EXISTED));
        address = addressConvert.convertToEntity(request);
		address.setId(id);
        addressRepository.save(address);
			return addressConvert .convertToDTO(address);
    }
    @Transactional
	public AddressResponse create(Long userId, AddressRequest request) {
		Address address = addressConvert.convertToEntity(request);
		addressRepository.save(address);
		return addressConvert.convertToDTO(address);
	}
}

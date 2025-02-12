package com.sofkau.auth.application.mappers;

import com.sofkau.auth.application.dtos.CreateCustomerRequest;
import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.dtos.UpdateCustomerRequest;
import com.sofkau.auth.domain.entities.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    GetCustomerResponse toCustomerResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer toConsumer(CreateCustomerRequest createCustomerRequest);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer updateCustomerWith(@MappingTarget Customer customer, UpdateCustomerRequest updateCustomerRequest);
}

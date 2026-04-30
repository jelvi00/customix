package com.customix.domain.model;

import com.customix.dto.AddressDTO;
import com.customix.dto.CustomerDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Customer extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String email;

    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    public CustomerDTO.Response toDTOResponse() {

        var addressList = Objects.isNull(addresses) || addresses.isEmpty()
                ? null
                : addresses.stream().map((address) -> new AddressDTO.Response(
                address.getId(), address.getStreet(), address.getCity(), address.getCountry(),
                address.getPostalCode(), address.getStatus().toString(), null
        )).toList();

        return new CustomerDTO.Response(id, name, email, this.getStatus().toString(), addressList);
    }

}

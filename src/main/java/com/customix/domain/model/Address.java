package com.customix.domain.model;

import com.customix.dto.AddressDTO;
import com.customix.dto.CustomerDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
public class Address extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public AddressDTO.Response toDTOResponse() {
        return new AddressDTO.Response(
                id, street, city, country, postalCode,
                getStatus().toString(),
                new CustomerDTO.Response(
                        customer.getId(), customer.getName(), customer.getEmail(),
                        customer.getStatus().toString(), null
                ));
    }
}

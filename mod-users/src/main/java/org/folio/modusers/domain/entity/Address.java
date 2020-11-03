package org.folio.modusers.domain.entity;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "address")
@Data
@Entity
public class Address implements Serializable {

  @Id
  @GeneratedValue
  private UUID id;

	@ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

	@OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_type_id")
  private AddressType addressType;

  private String countryId;

  private String addressLine1;

  private String addressLine2;

  private String city;

  private String region;

  private String postalCode;

  private boolean primaryAddress;


}

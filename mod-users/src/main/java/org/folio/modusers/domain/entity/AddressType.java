package org.folio.modusers.domain.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Table(name = "address_type")
@Data
@Entity
public class AddressType implements Serializable {

  @Id
  @GeneratedValue
  private UUID id;

  @OneToOne(mappedBy = "addressType", fetch = FetchType.LAZY)
  @ToString.Exclude
  private Address address;

  private String addressType;

  private String description;

}


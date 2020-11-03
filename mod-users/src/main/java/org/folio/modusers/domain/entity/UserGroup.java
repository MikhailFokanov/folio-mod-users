package org.folio.modusers.domain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Table(name = "user_group")
@Data
@Entity
public class UserGroup implements Serializable {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "name")
  private String group;

  private String description;

  @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<User> users;
}

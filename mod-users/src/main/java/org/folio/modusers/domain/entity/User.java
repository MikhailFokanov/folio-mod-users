package org.folio.modusers.domain.entity;

import static javax.persistence.FetchType.LAZY;

import com.sun.xml.bind.v2.TODO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Table(name = "users")
@Data
@Entity
public class User implements Serializable {

  @Id
  @GeneratedValue
  private UUID id;

  private String username;

  private UUID externalSystemId;

  private String barcode;

  private Boolean active;

  private String type;

//  private UUID patronGroupId;

//	private Object meta;

/*	@Column("personal")
	private UserPersonal personal;*/

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "patron_group_id")
  private UserGroup userGroup;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<Address> addresses;

  @OneToMany(mappedBy = "proxyUser", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<ProxyFor> proxyFor;

  private Date enrollmentDate;

  private Date expirationDate;

  private Date createdDate;

  private Date updatedDate;

  private String lastname;

  private String firstname;

  private String middlename;

  private String email;

  private String phone;

  private String mobilePhone;

  private Date dateOfBirth;

}


package org.folio.modusers.domain.entity;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "proxy_for")
@Data
@Entity
public class ProxyFor implements Serializable {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "proxy_user_id")
  private User proxyUser;

  @Column(name = "request_for_sponsor", nullable = false)
  private String requestForSponsor;

  @Column(name = "created_date", nullable = false)
  private Date createdDate;

  @Column(name = "notifications_to", nullable = false)
  private String notificationsTo;

  @Column(name = "accrue_to", nullable = false)
  private String accrueTo;

  @Column(name = "expiration_date", nullable = false)
  private Date expirationDate;

  private String status;

}


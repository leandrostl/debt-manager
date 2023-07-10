package dev.leandro.debtmanager;

import javax.persistence.*;

@Entity
public class Change {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Debt debt;

  private Long clientId;

  public Change() {
  }

  public Change(Debt debt, Long clientId) {
    this.debt = debt;
    this.clientId = clientId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Debt getDebt() {
    return debt;
  }

  public void setDebt(Debt debt) {
    this.debt = debt;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }
}

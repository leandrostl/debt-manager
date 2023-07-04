package dev.leandro.debtmanager;

import javax.persistence.*;

@Entity
class Debt {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private double amount;
  @Enumerated(EnumType.STRING)
  private Status status;

  public Debt() {
  }

  public Debt(double amount, Status status) {
    this.amount = amount;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}

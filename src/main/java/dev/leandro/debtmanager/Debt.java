package dev.leandro.debtmanager;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
class Debt {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private double amount;
  @Enumerated(EnumType.STRING)
  private Status status;
  @JsonFormat(pattern = "dd/MM/yyyy")
  @Column(name = "start_date")
  private LocalDate startDate;
  @JsonFormat(pattern = "dd/MM/yyyy")
  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "clientId")
  @JsonIdentityReference(alwaysAsId = true)
  private List<Change> changes = new ArrayList<>();

  public Debt() {
  }

  public Debt(double amount, Status status, LocalDate startDate, LocalDate endDate) {
    this.amount = amount;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
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

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public List<Change> getChanges() {
    return changes;
  }

  public void setChanges(List<Change> changes) {
    this.changes = changes;
  }

  public Change addChangeFromClient(Long clientId) {
    final Change change = new Change(this, clientId);
    this.changes.add(change);
    return change;
  }
}

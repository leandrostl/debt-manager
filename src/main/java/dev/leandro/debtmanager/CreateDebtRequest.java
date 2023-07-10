package dev.leandro.debtmanager;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class CreateDebtRequest {
  private double amount;
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate startDate;
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate endDate;

  public CreateDebtRequest(double amount, LocalDate startDate, LocalDate endDate) {
    this.amount = amount;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  private CreateDebtRequest() {}

  public Debt convert() {
    return new Debt(amount, Status.Active, startDate, endDate);
  }

  public double getAmount() {
    return amount;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }
}

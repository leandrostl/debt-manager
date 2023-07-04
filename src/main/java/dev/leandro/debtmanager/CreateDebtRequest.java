package dev.leandro.debtmanager;

public class CreateDebtRequest {
  private double amount;

  public CreateDebtRequest(double amount) {this.amount = amount;}

  private CreateDebtRequest() {}
  
  public Debt convert() {
    return new Debt(amount, Status.Active);
  }

  public double getAmount() {
    return amount;
  }
}

package dev.leandro.debtmanager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class DebtService {
  private final DebtRepository debtRepository;

  public DebtService(DebtRepository debtRepository) {
    this.debtRepository = debtRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Debt updateDebt(Long id, Debt updatedDebt) {
    return debtRepository.findByIdAndLock(id).map(debt -> {
      debt.setAmount(updatedDebt.getAmount());
      debt.setStatus(updatedDebt.getStatus());
      return debtRepository.save(debt);
    }).orElseThrow(() -> new IllegalArgumentException("Debt not found with id: " + id));
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public int updateOverdueDebtsStatus(Status status, Long clientId) {
    final Set<Debt> overdueDebts = debtRepository.findOverdueDebts();
    overdueDebts.forEach(debt -> {
      debt.setStatus(status);
      debt.addChangeFromClient(clientId);
      debtRepository.save(debt);
    });
    return overdueDebts.size();
  }
}

package dev.leandro.debtmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.Set;

@Repository
interface DebtRepository extends JpaRepository<Debt, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select d from Debt d where d.id = :id")
  Optional<Debt> findByIdAndLock(Long id);

  @Modifying
  @Query("UPDATE Debt d SET d.status = :status WHERE d.endDate <= CURRENT_DATE")
  int updateOverdueDebtsStatus(Status status);

  @Query("SELECT d FROM Debt d WHERE d.endDate <= CURRENT_DATE")
  Set<Debt> findOverdueDebts();
}

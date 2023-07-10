package dev.leandro.debtmanager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/debts")
class DebtController {
  private final DebtService debtService;
  private final DebtRepository debtRepository;

  public DebtController(DebtService debtService, DebtRepository debtRepository) {
    this.debtService = debtService;
    this.debtRepository = debtRepository;
  }

  @GetMapping
  public ResponseEntity<List<Debt>> getAllDebts() {
    return ResponseEntity.ok(debtRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Debt> getDebt(@PathVariable Long id) {
    return ResponseEntity.ok(
        debtRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Debt not found with id: " + id)));
  }

  @PostMapping
  public ResponseEntity<Debt> createDebt(@RequestBody CreateDebtRequest request) {
    final var createdDebt = debtRepository.save(request.convert());
    final var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")//
        .buildAndExpand(createdDebt.getId()).toUri();

    return ResponseEntity.created(location).body(createdDebt);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Debt> updateDebt(@PathVariable Long id, @RequestBody Debt updatedDebt) {
    return ResponseEntity.ok(debtService.updateDebt(id, updatedDebt));
  }

  @PatchMapping("/update-overdue-debts-status")
  public ResponseEntity<String> updateOverdueDebts(@RequestParam(name = "status") Status status,
      @RequestParam(name = "clientId") Long clientId) {
    try {
      return ResponseEntity.ok(
          MessageFormat.format("The client {0} change the status to {1} of a total of {2} overdue debts!", clientId, status,
              debtService.updateOverdueDebtsStatus(status, clientId)));
    } catch (Exception ex) {
      return ResponseEntity.unprocessableEntity().body(
          MessageFormat.format("Impossible do process client's {0} request of change status to {1}: Cause: {2}.", clientId,
              status, ex));
    }
  }
}

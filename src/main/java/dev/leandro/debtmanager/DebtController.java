package dev.leandro.debtmanager;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/debts")
class DebtController {
    private final DebtRepository debtRepository;

    public DebtController(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
    }

    @GetMapping
    public List<Debt> getAllDebts() {
        return debtRepository.findAll();
    }

    @GetMapping("/{id}")
    public Debt getDebt(@PathVariable Long id) {
        return debtRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Debt not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Debt> createDebt(@RequestBody CreateDebtRequest request) {
        final var createdDebt = debtRepository.save(request.convert());
        final var location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdDebt.getId())
            .toUri();

        return ResponseEntity.created(location).body(createdDebt);
    }

    @PutMapping("/{id}")
    public Debt updateDebt(@PathVariable Long id, @RequestBody Debt updatedDebt) {
        Optional<Debt> optionalDebt = debtRepository.findById(id);
        if (optionalDebt.isPresent()) {
            Debt debt = optionalDebt.get();
            debt.setAmount(updatedDebt.getAmount());
            debt.setStatus(updatedDebt.getStatus());
            return debtRepository.save(debt);
        } else {
            throw new IllegalArgumentException("Debt not found with id: " + id);
        }
    }
}

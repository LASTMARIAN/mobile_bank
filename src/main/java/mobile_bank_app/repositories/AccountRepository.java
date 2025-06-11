package mobile_bank_app.repositories;

import mobile_bank_app.models.AccountsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountsModel, Long> {
    List<AccountsModel> findAllByUserId(Long id);
    Optional<AccountsModel> findByAccountId(String id);
}

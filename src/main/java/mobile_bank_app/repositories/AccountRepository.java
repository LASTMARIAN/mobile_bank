package mobile_bank_app.repositories;

import mobile_bank_app.models.AccountsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountsModel, Long> {
    List<AccountsModel> findAllByUserId(Long id);
    Optional<AccountsModel> findByAccountId(String id);
    @Query(value = "SELECT * FROM accounts ORDER BY CAST(SUBSTRING(account_id, 5) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    AccountsModel findLastAccount();

}

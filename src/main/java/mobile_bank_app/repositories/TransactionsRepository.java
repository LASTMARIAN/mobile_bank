package mobile_bank_app.repositories;

import mobile_bank_app.models.TransactionsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<TransactionsModel, Long>{
    List<TransactionsModel> findAllByUserId(Long id);
}

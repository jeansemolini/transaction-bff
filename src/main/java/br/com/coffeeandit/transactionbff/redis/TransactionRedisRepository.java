package br.com.coffeeandit.transactionbff.redis;

import br.com.coffeeandit.transactionbff.dto.TransactionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRedisRepository extends CrudRepository<TransactionDto, String> {
}

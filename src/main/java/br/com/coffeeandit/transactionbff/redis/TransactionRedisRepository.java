package br.com.coffeeandit.transactionbff.redis;

import br.com.coffeeandit.transactionbff.dto.TransactionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRedisRepository extends CrudRepository<TransactionDto, String> {

    List<TransactionDto> findAll();
}

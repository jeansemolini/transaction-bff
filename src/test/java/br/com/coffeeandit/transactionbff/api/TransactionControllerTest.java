package br.com.coffeeandit.transactionbff.api;

import br.com.coffeeandit.transactionbff.config.TestRedisConfiguration;
import br.com.coffeeandit.transactionbff.config.WebFluxTestConfiguration;
import br.com.coffeeandit.transactionbff.domain.TransactionService;
import br.com.coffeeandit.transactionbff.dto.RequestTransactionDto;
import br.com.coffeeandit.transactionbff.feign.TransactionClient;
import br.com.coffeeandit.transactionbff.mock.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@ExtendWith(SpringExtension.class)
@WithMockUser(roles = {"USER", "ADMIN", "CoffeeAndItRole"})
@EmbeddedKafka(topics = "transaction", partitions = 2)
@WebFluxTest(value = {TransactionController.class}, properties = {"spring.data.redis.repositories.enabled=true"})
@Import(value = {KafkaAutoConfiguration.class, TestRedisConfiguration.class, WebFluxTestConfiguration.class, TransactionService.class})
@DirtiesContext
public class TransactionControllerTest {

    public static final String TRANSACTION = "/transaction/";

    @MockBean
    private TransactionClient transactionClient;

    @Autowired
    private WebTestClient client;

    @BeforeEach
    public void setup() {
        when(transactionClient.buscarTransacoes(anyLong(), anyLong())).thenReturn(List.of(DataMock.getTransactionDto()));
    }

    @ParameterizedTest
    @MethodSource("getTransaction")
    void testEnviarTransacao(final RequestTransactionDto transaction) {
        client.mutateWith(csrf()).post()
                .uri(TRANSACTION)
                .bodyValue(transaction)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @MethodSource("getTransaction")
    void testTransactionId(final RequestTransactionDto transaction) {
        FluxExchangeResult<RequestTransactionDto> requestTransactionDtoFluxExchangeResult = client.mutateWith(csrf()).post()
                .uri(TRANSACTION)
                .bodyValue(transaction)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().returnResult(RequestTransactionDto.class);

        var requestTransactionDto = requestTransactionDtoFluxExchangeResult.getResponseBody().blockFirst();

        client.get().uri(TRANSACTION + requestTransactionDto.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @MethodSource("getTransaction")
    void testBuscaPorAgenciaConta(final RequestTransactionDto transaction) {
        client.get().uri(TRANSACTION + "245/9876")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();
    }

    private static Stream<RequestTransactionDto> getTransaction() {
        return Stream.of(DataMock.getTransactionDto());
    }
}

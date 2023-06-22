package br.com.coffeeandit.transactionbff.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class FilmesCompradosMetricsGauge implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry registry) {
        Gauge.builder("filmes_comprados", this, value ->
                obterQuantidadeFilmesComprados())
                .description("")
                .tags(Tags.of(Tag.of("data",
                        LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")))))
                .baseUnit("jeansemolini")
                .register(registry);
    }

    private Integer obterQuantidadeFilmesComprados() {
        return new Random().nextInt(8);
    }
}

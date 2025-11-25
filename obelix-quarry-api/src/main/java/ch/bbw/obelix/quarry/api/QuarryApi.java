package ch.bbw.obelix.quarry.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.UUID;


public interface QuarryApi {

    @GetExchange("/menhirs")
    List<MenhirDto> getAllMenhirs();

    @GetExchange("/menhirs/{menhirId}")
    MenhirDto getMenhirById(@PathVariable UUID menhirId);

    @DeleteExchange("/quarry/{menhirId}")
    void deleteById(@PathVariable UUID menhirId);
}

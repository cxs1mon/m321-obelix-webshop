package ch.bbw.obelix.quarry.controller;

import ch.bbw.obelix.quarry.api.MenhirDto;
import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class QuarryController implements QuarryApi {

    private final MenhirRepository menhirRepository;

    @Override
    public List<MenhirDto> getAllMenhirs() {
        return menhirRepository.findAll()
                .stream().map(MenhirEntity::toDto).toList();
    }

    @Override
    public MenhirDto getMenhirById(UUID menhirId) {
        return menhirRepository.findById(menhirId)
                .map(MenhirEntity::toDto)
                .orElseThrow(() -> new UnknownMenhirException("unknown menhir with id " + menhirId));
    }

    @Override
    public void deleteById(UUID menhirId) {
        menhirRepository.deleteById(menhirId);
    }

    @StandardException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UnknownMenhirException extends RuntimeException {}
}


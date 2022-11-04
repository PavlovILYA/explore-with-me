package ru.practicum.explore.with.me.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.compilation.exception.CompilationNotFoundException;
import ru.practicum.explore.with.me.compilation.model.Compilation;
import ru.practicum.explore.with.me.compilation.repository.CompilationRepository;
import ru.practicum.explore.with.me.event.model.Event;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;

    public Compilation saveCompilation(Compilation compilation) {
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Saved compilation: {}", savedCompilation);
        return savedCompilation;
    }

    public void deleteCompilation(Long compilationId) {
        compilationRepository.deleteById(compilationId);
        log.info("Compilation {} was deleted", compilationId);
    }

    public void addEventToCompilation(Long compilationId, Event event) {
        Compilation compilation = getCompilation(compilationId);
        compilation.getEvents().add(event);
        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Event {} was added to compilation: {}", event.getId(), updatedCompilation);
    }

    public void deleteEventFromCompilation(Long compilationId, Event event) {
        Compilation compilation = getCompilation(compilationId);
        compilation.getEvents().remove(event);
        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Event {} was deleted from compilation: {}", event.getId(), updatedCompilation);
    }

    public void pinCompilation(Long compilationId) {
        Compilation compilation = getCompilation(compilationId);
        compilation.setPinned(true);
        Compilation pinnedCompilation = compilationRepository.save(compilation);
        log.info("Pinned compilation: {}", pinnedCompilation);
    }

    public void unpinCompilation(Long compilationId) {
        Compilation compilation = getCompilation(compilationId);
        compilation.setPinned(false);
        Compilation unpinnedCompilation = compilationRepository.save(compilation);
        log.info("Unpinned compilation: {}", unpinnedCompilation);
    }

    public Compilation getCompilation(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        log.info("Get compilation: {}", compilation);
        return compilation;
    }

    public List<Compilation> getCompilations(boolean pinned, int from, int size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest).getContent();
        log.info("Get compilations: {}", compilations);
        return compilations;
    }
}

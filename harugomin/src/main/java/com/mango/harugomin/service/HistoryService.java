package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.History;
import com.mango.harugomin.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public Optional<History> findById(Long historyId){
        return historyRepository.findById(historyId);
    }
}

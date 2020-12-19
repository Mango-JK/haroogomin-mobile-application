package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.History;
import com.mango.harugomin.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public Optional<History> findById(Long historyId){
        return historyRepository.findById(historyId);
    }

    public Page<History> myHistoryPost(Long userId, PageRequest pageRequest) {
        return historyRepository.findAllByUserUserId(userId, pageRequest);
    }

    @Transactional
    public void deleteUserHistories(Long userId) {
        historyRepository.deleteAllByUsers(userId);
    }
}

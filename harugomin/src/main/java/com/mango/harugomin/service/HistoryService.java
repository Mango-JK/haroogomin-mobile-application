package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.History;
import com.mango.harugomin.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public Page<History> myHistoryPost(Long userId, PageRequest pageRequest) {
        return historyRepository.findAllByUserUserId(userId, pageRequest);
    }
}

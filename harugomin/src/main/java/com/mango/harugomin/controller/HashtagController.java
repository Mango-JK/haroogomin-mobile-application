package com.mango.harugomin.controller;

import com.mango.harugomin.service.HashtagService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "4. HashTag")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class HashtagController {
    private final HashtagService hashtagService;

}

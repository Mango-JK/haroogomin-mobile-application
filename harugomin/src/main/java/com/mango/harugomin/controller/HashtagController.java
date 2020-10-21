package com.mango.harugomin.controller;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.service.HashtagService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "4. HashTag")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class HashtagController {
    private final HashtagService hashtagService;

    @PostMapping(value = "/hashtag")
    public long addUserHashtag(@RequestParam("tagName") String tagName){
        Hashtag hashtag = new Hashtag(tagName, 0);
        return hashtagService.saveHashtag(hashtag).getTagId();
    }

}

package com.mango.harugomin.controller;

import com.mango.harugomin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "2. Post")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {

    private final UserService userService;

    @ApiOperation(value = "index", notes = "연습용 메인 페이지")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/")
    public String Hello() {
        return "index";
    }
}

package com.mango.harugomin.controller;

import com.mango.harugomin.domain.repository.MemberRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "{1. Member}")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {
    private final MemberRepository memberRepository;

    @ApiOperation(value = "hello", notes = "예제입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/")
    public String Hello(){
        return "hello";
    }

}

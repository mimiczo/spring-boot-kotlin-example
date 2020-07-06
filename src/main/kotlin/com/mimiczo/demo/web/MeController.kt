package com.mimiczo.demo.web

import com.mimiczo.demo.service.AccountService
import com.mimiczo.demo.web.dto.AccountDto
import com.mimiczo.demo.web.dto.MeParam
import com.mimiczo.demo.web.support.DemoSession
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * com.mimiczo.demo.web
 * Created by j on 2017.08.01
 */
@RestController
@RequestMapping("/me")
class MeController(val accountService: AccountService) {

    @ApiOperation(value = "내정보 조회"
            , response = AccountDto::class)
    @GetMapping("")
    fun getMe(): ResponseEntity<AccountDto> {
        return ResponseEntity.ok(AccountDto.of(accountService.get(DemoSession.getId())))
    }

    @ApiOperation(value = "내정보 변경"
            , response = AccountDto::class)
    @PutMapping("")
    fun putMe(@Validated @RequestBody param: MeParam, bindingResult: BindingResult): ResponseEntity<AccountDto> {
        if(bindingResult.hasErrors()) throw BindException(bindingResult)

        param.id = DemoSession.getId()
        return ResponseEntity.ok(AccountDto.of(accountService.modifyAccount(param)))
    }
}
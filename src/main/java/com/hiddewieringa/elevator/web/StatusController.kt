package com.hiddewieringa.elevator.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/status")
class StatusController {

    @GetMapping("/live")
    fun live() = ""

    @GetMapping("/ready")
    fun ready() = ""
}

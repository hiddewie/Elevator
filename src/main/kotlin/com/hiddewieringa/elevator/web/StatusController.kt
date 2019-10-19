package com.hiddewieringa.elevator.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * For application aliveness/readiness detection
 */
@RestController
@RequestMapping("/status")
class StatusController {

    @GetMapping("/live")
    fun live() = "OK"

    @GetMapping("/ready")
    fun ready() = "OK"
}

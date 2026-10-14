
package com.autosync.autosync;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthController {

    @GetMapping("/api/health")
    public String health() {
        return "AutoSync is running";
    }
}


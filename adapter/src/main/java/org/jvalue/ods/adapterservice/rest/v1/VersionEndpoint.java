package org.jvalue.ods.adapterservice.rest.v1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class VersionEndpoint {

    @Value("${app.version}")
    private String VERSION;

    @GetMapping("/version")
    public String getApplicationVersion() {
        return VERSION;
    }
}

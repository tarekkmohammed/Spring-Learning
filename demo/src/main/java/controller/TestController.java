package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/cors")
    public String testCors() {
        System.out.println("🧪 Test CORS endpoint hit!");
        return "CORS is working!";
    }
}
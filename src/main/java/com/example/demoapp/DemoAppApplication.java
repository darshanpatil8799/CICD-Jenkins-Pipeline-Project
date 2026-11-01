package com.example.demoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoAppApplication.class, args);
    }

    @RestController
    static class HomeController {
        @GetMapping("/")
        public String home() {
            return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "<title>Jenkins DemoApp</title><style>" +
                "body{margin:0;font-family:Arial,sans-serif;background:#0b1220;color:#eaf2ff;" +
                "min-height:100vh;display:grid;place-items:center}" +
                "main{max-width:720px;margin:24px;padding:36px;border:1px solid #263955;" +
                "border-radius:20px;background:#111d31;box-shadow:0 20px 70px #0006}" +
                "h1{color:#5eead4}.badge{display:inline-block;padding:8px 12px;border-radius:99px;" +
                "background:#123d42;color:#99f6e4}p{line-height:1.7;color:#c7d5e8}" +
                "code{color:#93c5fd}</style></head><body><main>" +
                "<span class='badge'>Application is running</span>" +
                "<h1>Jenkins CI/CD DemoApp</h1>" +
                "<p>This Spring Boot application was built with Maven, packaged with Docker, " +
                "and is ready to deploy on Kubernetes.</p>" +
                "<p>Pipeline: <code>GitHub → Jenkins → Maven → SonarQube → Docker Hub → Kubernetes</code></p>" +
                "</main></body></html>";
        }

        @GetMapping("/health")
        public String health() {
            return "OK";
        }
    }
}

package edu.wgu.d387_sample_code.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@CrossOrigin("http://localhost:4200")
@RestController
public class WelcomeController {
    @GetMapping(value = "api/welcome")
    public Set<String> DisplayWelcome() throws InterruptedException {
        Set<String> messages = new HashSet<>();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties properties = new Properties();
                try {
                    InputStream stream = new ClassPathResource("language_en_CA.properties").getInputStream();
                    properties.load(stream);
                    var englishWelcome = properties.getProperty("welcome") + ", ThreadID: " + Thread.currentThread().getName();
                    System.out.println(englishWelcome);
                    messages.add(englishWelcome);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties properties = new Properties();
                try {
                    InputStream stream = new ClassPathResource("language_fr_CA.properties").getInputStream();
                    properties.load(stream);
                    var frenchMessage = properties.getProperty("welcome") + ", ThreadID: " + Thread.currentThread().getName();
                    System.out.println(frenchMessage);
                    messages.add(frenchMessage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        Thread.sleep(100);
        return messages;
    }

    @GetMapping("api/timezone")
    public String GetTimeZone() {
        return TimezoneConversion();
    }

    private String TimezoneConversion() {
        ZoneId easternZone = ZoneId.of("America/New_York");
        ZoneId mountainZone = ZoneId.of("America/Denver");
        ZoneId utcZone = ZoneId.of("UTC");

        ZonedDateTime time = ZonedDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime easternTime = time.withZoneSameInstant(easternZone);
        System.out.println("Eastern time: " + easternTime);

        ZonedDateTime mountainTime = time.withZoneSameInstant(mountainZone);
        System.out.println("Mountain time: " + mountainTime);

        ZonedDateTime utcTime = time.withZoneSameInstant(utcZone);
        System.out.println("UTC time: " + utcTime);

        return easternTime.format(formatter) + " EST, " + mountainTime.format(formatter) + " MST, " + utcTime.format(formatter) + " UTC";
    }
}

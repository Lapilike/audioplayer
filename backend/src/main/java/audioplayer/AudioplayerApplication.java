package audioplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "audioplayer.utils")
public class AudioplayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AudioplayerApplication.class, args);
    }
}

package club.focus.focus.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.focus.focus.dto.MessageDto;
import club.focus.focus.utils.MessagesReader;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

//import init.Init;

@Controller
public class FocusController {
	// rate limit Bucket4j
	private final Bucket bucket;

	public FocusController() {
		Bandwidth limit = Bandwidth.classic(2, Refill.greedy(2, Duration.ofSeconds(2)));
		this.bucket = Bucket.builder().addLimit(limit).build();

		MessagesReader mr = new MessagesReader(sourceDirectory);
		messagesList = mr.readAllFiles();
	}

	List<MessageDto> messagesList = new ArrayList<MessageDto>();
	final Path sourceDirectory = Paths.get("/home/gtg/focus/org");

	@Value("${local.switch.value}")
	String value;

	@Value("${userBucket.path}")
	String userBucket;

	@GetMapping("/debug")
	public String debug(Model model) {
		String bucketsLeft = Long.toString(bucket.getAvailableTokens());
		model.addAttribute("bucketsLeft", bucketsLeft);
		return "debug";
	}

	@GetMapping("/greetings")
	// public String greetings(@RequestParam(name="hash", required=false,
	// defaultValue="") int hash, String header, String textBody, Model model) {
	public String greetings(@RequestParam(name = "keey", required = true) int keey, Model model) {
		System.out.println("bucket.getAvailableTokens()=" + bucket.getAvailableTokens());

		while (!bucket.tryConsume(1)) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
			}
			// ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		}

		if (keey != 20240101) {
			return null;
		}

		Random rand = new Random();
		int n = rand.nextInt(messagesList.size() - 1);
		MessageDto message;
		message = messagesList.get(n);

		System.out.println(message.sections().toString());

		model.addAttribute("hash", message.hash());
		model.addAttribute("msgHeader", message.sections().toString());
		model.addAttribute("textBody", message.textBody());
		return "greetings";

		// throw new UnsupportedOperationException("max concurrent requests reached");
	}

}

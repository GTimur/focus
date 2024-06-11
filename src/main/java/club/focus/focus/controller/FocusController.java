package club.focus.focus.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import club.focus.focus.dto.MessageDto;
import club.focus.focus.utils.MessageGenerator;
import club.focus.focus.utils.MessagesReader;

//import init.Init;

@Controller
public class FocusController {
	List<MessageDto> messagesList = new ArrayList<MessageDto>();
	final Path sourceDirectory = Paths.get("/media/gtg/DATA01_WDREDSMR/WORK/2024/kk/org");

	public FocusController() {
		MessagesReader mr = new MessagesReader(sourceDirectory);
		messagesList = mr.readAllFiles();
	}

	@Value("${local.switch.value}")
	String value;

	@Value("${userBucket.path}")
	String userBucket;

	@ResponseBody
	@RequestMapping("/hello")
	public String Hello() {
		MessageGenerator mg = new MessageGenerator();

		return mg.composeNotEnoughMoneyForDocsInProgressMessage("test");
		// "Test message\n"+appInit.getParamsState() + "userBucket="+userBucket + "
		// local.switch.value="+value;
	}

	@GetMapping("/greetings")
	// public String greetings(@RequestParam(name="hash", required=false,
	// defaultValue="") int hash, String header, String textBody, Model model) {
	public String greetings(Model model) {
		Random rand = new Random();
		int n = rand.nextInt(messagesList.size() - 1);
		MessageDto message;
		message = messagesList.get(n);

		// logic to build Message data
//		model.addAttribute("message", message);

		System.out.println(message.sections().toString());
		
		model.addAttribute("hash", message.hash());
		model.addAttribute("msgHeader", message.sections().toString());
		model.addAttribute("textBody", message.textBody());
		return "greetings";
	}

}

package pt.ulisboa.tecnico.softeng.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.dto.ClientDto;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

@Controller
@RequestMapping(value = "/banks/bank/{code}/clients")
public class ClientController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String clientForm(Model model, @PathVariable String code) {
		Bank bank = Bank.getBankByCode(code);
		logger.info("clientForm");
		model.addAttribute("client", new ClientDto());
		model.addAttribute("bank", bank);
		model.addAttribute("clients", bank.getClients());
		return "clientsView";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String clientSubmit(Model model, @ModelAttribute ClientDto clientDto, @PathVariable String code) {
		logger.info("clientSubmit name:{}, age:{}, id:{}", clientDto.getName(), clientDto.getAge(), clientDto.getId());

		Bank bank = Bank.getBankByCode(code);

		try {
			new Client(bank, clientDto.getId(), clientDto.getName(), clientDto.getAge());
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			model.addAttribute("client", clientDto);
			model.addAttribute("clients", bank.getClients());
			return "clientsView";
		}

		return "redirect:/banks/bank/" + code + "/clients";
	}

	@RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
	public String showClient(Model model, @PathVariable String code, @PathVariable String id) {
		logger.info("showClinet id:{}", id);

		Bank bank = Bank.getBankByCode(code);

		model.addAttribute("client", bank.getClientById(id));
		return "clientView";
	}
}

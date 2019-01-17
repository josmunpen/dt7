
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import services.CustomerService;
import services.HandyWorkerService;
import services.MessageService;
import services.RefereeService;
import domain.Actor;
import domain.Box;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	MessageService		messageService;

	@Autowired
	ActorService		actorService;

	@Autowired
	CustomerService		customerService;

	@Autowired
	HandyWorkerService	handyworkerService;

	@Autowired
	RefereeService		refereeService;

	@Autowired
	BoxService			boxService;


	// Constructors -----------------------------------------------------------

	public MessageController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int boxId) {
		ModelAndView res;
		Collection<Message> messages;
		messages = this.boxService.findOne(boxId).getMessages();
		res = new ModelAndView("message/list");
		res.addObject("messages", messages);
		res.addObject("requestURI", "message/list.do");

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Message m;

		m = this.messageService.create();

		res = this.createEditModelAndView(m);
		return res;
	}

	@RequestMapping(value = "/createBroadcast", method = RequestMethod.GET)
	public ModelAndView createBroadcast() {
		ModelAndView res;
		Message m;

		m = this.messageService.create();

		res = this.createEditModelAndViewBroadcast(m);
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		final ModelAndView res;
		Message m = new Message();

		m = this.messageService.findOne(messageId);
		Assert.notNull(m);
		res = this.createEditModelAndView(m);

		return res;
	}

	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId) {
		final ModelAndView res;
		Message m = new Message();

		m = this.messageService.findOne(messageId);
		Assert.notNull(m);
		res = this.createMoveModelAndView(m);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute("m") final Message m, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors()) {
			m.setBody(binding.getAllErrors().toString());
			res = this.createEditModelAndView(m);
		} else

			try {

				this.messageService.save(m);
				final Box box = this.boxService.findOutBoxByUserPrincipal();
				final String id = new Integer(box.getId()).toString();
				res = new ModelAndView("redirect:list.do?boxId=" + id);
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(m, "Error");
			}

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "broadcast")
	public ModelAndView saveBroadcast(@Valid @ModelAttribute("m") final Message m, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(m);
		else

			try {
				for (final Actor a : this.actorService.findAll()) {
					m.setRecipient(a);
					this.messageService.save(m);
				}
				final Box box = this.boxService.findOutBoxByUserPrincipal();
				final String id = new Integer(box.getId()).toString();
				res = new ModelAndView("redirect:list.do?boxId=" + id);
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(m, "Error");
			}

		return res;
	}
	@RequestMapping(value = "/move", method = RequestMethod.POST, params = "save")
	public ModelAndView moveMessage(@Valid @ModelAttribute("m") final Message m, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(m);
		else

			try {
				final List<Box> oldBoxes = new ArrayList<Box>(this.actorService.findByPrincipal().getBoxes());
				for (final Box b5 : oldBoxes) {
					Message messageToSave = null;
					for (final Message m1 : b5.getMessages())
						if (m1.getId() == m.getId()) {
							final List<Message> m3 = new ArrayList<Message>(b5.getMessages());
							messageToSave = m1;
							m3.remove(m1);
							b5.setMessages(m3);
							this.boxService.save(b5, false);
							break;
						}
					for (final Box b6 : oldBoxes)
						if (m.getVersion() == b6.getId()) {
							final List<Message> m3 = new ArrayList<Message>(b6.getMessages());
							m3.add(messageToSave);
							b6.setMessages(m3);
							this.boxService.save(b6, false);
							break;
						}
				}
				res = new ModelAndView("redirect:http://localhost:8080/Acme-Handy-Worker/box/list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(m, "Error");
			}

		return res;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("m") final Message m, final BindingResult binding) {
		ModelAndView res;
		try {
			this.messageService.delete(m);
			res = new ModelAndView("redirect:http://localhost:8080/Acme-Handy-Worker/box/list.do");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(m, "Error");
		}
		return res;
	}
	protected ModelAndView createEditModelAndView(final Message m) {
		ModelAndView res;

		res = this.createEditModelAndView(m, null);

		return res;
	}

	protected ModelAndView createMoveModelAndView(final Message m) {
		ModelAndView res;

		res = this.createMoveModelAndView(m, null);

		return res;
	}
	protected ModelAndView createEditModelAndViewBroadcast(final Message m) {
		ModelAndView res;

		res = this.createEditModelAndViewBroadcast(m, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final Message m, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("message/edit");
		final List<Actor> listActors = new ArrayList<Actor>();
		listActors.addAll(this.actorService.findAll());
		res.addObject("m", m);
		res.addObject("messageCode", messageCode);
		res.addObject("listActors", listActors);
		res.addObject("broadcast", false);

		return res;
	}
	protected ModelAndView createMoveModelAndView(final Message m, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("message/move");
		final Actor a = this.actorService.findByPrincipal();
		res.addObject("m", m);
		res.addObject("messageCode", messageCode);
		res.addObject("listBoxes", a.getBoxes());

		return res;
	}
	protected ModelAndView createEditModelAndViewBroadcast(final Message m, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("message/broadcast");
		final List<Actor> listActors = new ArrayList<Actor>();
		listActors.addAll(this.actorService.findAll());
		res.addObject("m", m);
		res.addObject("messageCode", messageCode);
		res.addObject("listActors", listActors);
		res.addObject("broadcast", true);

		return res;
	}

}

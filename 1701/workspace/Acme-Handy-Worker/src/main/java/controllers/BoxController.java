
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BoxService;
import services.MessageService;
import domain.Box;

@Controller
@RequestMapping("/box")
public class BoxController extends AbstractController {

	@Autowired
	BoxService		boxService;

	@Autowired
	MessageService	messageService;


	// Constructors -----------------------------------------------------------

	public BoxController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Box> boxes;
		boxes = this.boxService.findByUser();
		res = new ModelAndView("box/list");
		res.addObject("boxes", boxes);
		res.addObject("requestURI", "box/list.do");

		return res;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Box box;

		box = this.boxService.create();
		res = this.createEditModelAndView(box);
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int boxId) {
		final ModelAndView res;
		Box box = new Box();

		box = this.boxService.findOne(boxId);
		Assert.notNull(box);
		res = this.createEditModelAndView(box);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Box box, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors()) {
			box.setName(binding.getAllErrors().toString());
			res = this.createEditModelAndView(box);
		} else
			try {
				Boolean creating = false;
				if (box.getId() == 0)
					creating = true;
				this.boxService.save(box, creating);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(box, "Error");
			}

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Box box, final BindingResult binding) {
		ModelAndView res;
		try {
			this.boxService.delete(box);
			res = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(box, "Error");
		}
		return res;
	}
	protected ModelAndView createEditModelAndView(final Box box) {
		ModelAndView res;

		res = this.createEditModelAndView(box, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final Box box, final String messageCode) {
		ModelAndView res;

		res = new ModelAndView("box/edit");
		res.addObject("box", box);

		res.addObject("message", messageCode);

		return res;
	}

}

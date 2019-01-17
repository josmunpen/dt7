
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

import services.SocialProfileService;
import domain.SocialProfile;

@Controller
@RequestMapping("/socialprofile")
public class SocialProfileController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<SocialProfile> socialprofiles;

		socialprofiles = this.socialProfileService.findByUser();

		res = new ModelAndView("socialprofile/list");
		res.addObject("socialprofiles", socialprofiles);
		res.addObject("requestURI", "socialprofile/list.do");
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SocialProfile socialprofile, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(socialprofile);
		else
			try {
				this.socialProfileService.save(socialprofile);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialprofile, "socialprofile.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		SocialProfile socialprofile;

		socialprofile = this.socialProfileService.create();

		res = this.createEditModelAndView(socialprofile);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int socialprofileId) {
		ModelAndView result;
		SocialProfile socialprofile;

		socialprofile = this.socialProfileService.findOne(socialprofileId);
		Assert.notNull(socialprofile);
		result = this.createEditModelAndView(socialprofile);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final SocialProfile socialprofile, final BindingResult binding) {
		ModelAndView result;

		try {
			this.socialProfileService.delete(socialprofile);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(socialprofile, "socialprofile.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialprofile) {
		ModelAndView result;

		result = this.createEditModelAndView(socialprofile, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile, final String message) {
		ModelAndView result;

		result = new ModelAndView("socialprofile/edit");
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", message);

		return result;
	}
}

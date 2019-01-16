
package controllers.referee;

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

import services.NoteService;
import services.RefereeService;
import services.ReportService;
import controllers.AbstractController;
import domain.Note;
import domain.Referee;
import domain.Report;

@Controller
@RequestMapping("/note/referee")
public class NoteRefereeController extends AbstractController {

	@Autowired
	private NoteService		noteService;

	@Autowired
	private RefereeService	refereeService;

	@Autowired
	private ReportService	reportService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Note> notes;

		final Referee r = this.refereeService.findByPrincipal();
		notes = this.noteService.findByRefereeId(new Integer(r.getId()));

		res = new ModelAndView("note/list");
		res.addObject("notes", notes);
		res.addObject("requestURI", "note/referee/list.do");

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Note note;

		note = this.noteService.create();

		res = this.createEditModelAndView(note);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int noteId) {
		final ModelAndView res;
		final Note note;

		note = this.noteService.findOne(noteId);
		Assert.notNull(note);
		res = this.createEditModelAndView(note);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Note note, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(note);
		else
			try {
				this.noteService.save(note);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(note, "note.commit.error");
			}

		return res;
	}

	protected ModelAndView createEditModelAndView(final Note note) {
		ModelAndView result;

		result = this.createEditModelAndView(note, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Note note, final String message) {
		ModelAndView result;

		final Collection<Report> reps = this.reportService.findAll();
		result = new ModelAndView("note/edit");
		result.addObject("note", note);
		result.addObject("message", message);
		//result.addObject("reports", reps);

		return result;
	}
}

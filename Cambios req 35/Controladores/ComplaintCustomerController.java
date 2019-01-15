
package controllers.customer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ComplaintService;
import services.CustomerService;
import services.FixUpTaskService;
import services.ReportService;
import controllers.AbstractController;
import domain.Complaint;
import domain.Customer;
import domain.FixUpTask;
import domain.Report;

@Controller
@RequestMapping("/complaint/customer")
public class ComplaintCustomerController extends AbstractController {

	@Autowired
	private ComplaintService	complaintService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private ReportService		reportService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Complaint> complaints;

		final Customer c = this.customerService.findByPrincipal();
		complaints = this.complaintService.findByCustomerId(new Integer(c.getId()));

		res = new ModelAndView("complaint/list");
		res.addObject("complaints", complaints);
		res.addObject("requestURI", "complaint/customer/list.do");

		return res;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int complaintId) {
		final ModelAndView result;
		//		final Customer customer1 = this.customerService.findByPrincipal();
		//		final Collection<Complaint> complaints = this.complaintService.findByCustomerId(new Integer(customer1.getId()));

		final Complaint complaint = this.complaintService.findOne(complaintId);

		result = new ModelAndView("complaint/show");
		result.addObject("complaint", complaint);
		//result.addObject("complaints", complaints);
		//result.addObject("requestURI", "complaint/customer/show.do");
		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Complaint complaint;

		complaint = this.complaintService.create();

		res = this.createEditModelAndView(complaint);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Complaint complaint, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(complaint);
		else
			try {
				this.complaintService.saveForTest(complaint);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(complaint, "complaint.commit.error");
			}

		return res;
	}

	protected ModelAndView createEditModelAndView(final Complaint complaint) {
		ModelAndView result;

		result = this.createEditModelAndView(complaint, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Complaint complaint, final String message) {
		ModelAndView result;

		final Customer logged = this.customerService.findByPrincipal();
		final Collection<FixUpTask> futs = this.fixUpTaskService.findByCustomerId(new Integer(logged.getId()));
		final Collection<Report> reps = this.reportService.findAll();
		result = new ModelAndView("complaint/edit");
		result.addObject("complaint", complaint);
		result.addObject("message", message);
		result.addObject("fixUpTasks", futs);
		//result.addObject("reports", reps);

		return result;
	}
}

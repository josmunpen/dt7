
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.NoteRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Customer;
import domain.HandyWorker;
import domain.Note;
import domain.Referee;

@Service
@Transactional
public class NoteService {

	//Repositories
	@Autowired
	public NoteRepository		noteRepository;

	//Services
	@Autowired
	public ComplaintService		complaintService;
	@Autowired
	public RefereeService		refereeService;

	public ReportService		reportService;

	@Autowired
	public CustomerService		customerService;
	@Autowired
	public HandyWorkerService	handyWorkerService;


	//Constructor
	public NoteService() {
		super();
	}

	//35.2
	public Note create() {

		//Logged user must be a customer
		final Note res = new Note();
		final Authority a = new Authority();
		final Authority b = new Authority();
		final Authority c = new Authority();
		final UserAccount user = LoginService.getPrincipal();
		a.setAuthority(Authority.CUSTOMER);
		b.setAuthority(Authority.REFEREE);
		c.setAuthority(Authority.HANDYWORKER);

		final ArrayList<HandyWorker> hs = new ArrayList<>(this.handyWorkerService.findAll());
		final HandyWorker h1 = hs.get(0);
		final ArrayList<Referee> rs = new ArrayList<>(this.refereeService.findAll());
		final Referee r1 = rs.get(0);
		final ArrayList<Customer> cs = new ArrayList<>(this.customerService.findAll());
		final Customer c1 = cs.get(0);

		if (user.getAuthorities().contains(a)) {
			Assert.isTrue(user.getAuthorities().contains(a));
			res.setCustomer(this.customerService.findByPrincipal());
			res.setHandyWorker(h1);
			res.setReferee(r1);
			res.setCustomerComment("");
			res.setHandyWorkerComment("");
			res.setRefereeComment("");
			res.setMandatoryComment("A");
			res.setMoment(Calendar.getInstance().getTime());
			res.setCustomerComment("");
		} else if (user.getAuthorities().contains(b)) {
			res.setReferee(this.refereeService.findByPrincipal());
			res.setCustomer(c1);
			res.setHandyWorker(h1);
			res.setCustomerComment("");
			res.setHandyWorkerComment("");
			res.setRefereeComment("");
			res.setMandatoryComment("A");
			res.setMoment(Calendar.getInstance().getTime());
			res.setCustomerComment("");
		} else if (user.getAuthorities().contains(c)) {
			res.setHandyWorker(this.handyWorkerService.findByPrincipal());
			res.setCustomer(c1);
			res.setReferee(r1);
			res.setCustomerComment("");
			res.setHandyWorkerComment("");
			res.setRefereeComment("");
			res.setMandatoryComment("A");
			res.setMoment(Calendar.getInstance().getTime());
			res.setCustomerComment("");
		}
		return res;
	}

	public Note save(final Note note) {
		Assert.notNull(note);

		//Logged user must be a customer/referee/handyworker
		final Authority a = new Authority();
		final Authority b = new Authority();
		final Authority c = new Authority();
		final UserAccount user = LoginService.getPrincipal();
		a.setAuthority(Authority.CUSTOMER);
		b.setAuthority(Authority.REFEREE);
		c.setAuthority(Authority.HANDYWORKER);
		Assert.isTrue(user.getAuthorities().contains(a));

		//Note's owner is logged customer/referee/handyworker
		//Assert.isTrue(this.customerService.findByPrincipal().equals(note.getCustomer()) || this.refereeService.findByPrincipal().equals(note.getReferee()) || this.handyWorkerService.findByPrincipal().equals(note.getHandyWorker()));

		final Note res = note;

		res.setMoment(Calendar.getInstance().getTime());

		final Note result = this.noteRepository.save(res);
		return result;
	}
	//35.3
	public Note addCommentCustomer(final Note note, final String comment) {
		Assert.notNull(note);
		Assert.notNull(note.getId());

		//Logged user must be a customer
		final Authority a = new Authority();
		final UserAccount user = LoginService.getPrincipal();
		a.setAuthority(Authority.CUSTOMER);
		Assert.isTrue(user.getAuthorities().contains(a));

		//Note's owner is logged customer
		Assert.isTrue(this.customerService.findByPrincipal() == note.getCustomer());
		note.setCustomerComment(comment);
		this.noteRepository.save(note);
		return note;
	}

	public Note addCommentReferee(final Note note, final String comment) {
		Assert.notNull(note);
		Assert.notNull(note.getId());

		//Logged user must be a referee
		final Authority a = new Authority();
		final UserAccount user = LoginService.getPrincipal();
		a.setAuthority(Authority.REFEREE);
		Assert.isTrue(user.getAuthorities().contains(a));

		//Note's owner is logged customer
		Assert.isTrue(this.refereeService.findByPrincipal() == note.getReferee());
		note.setRefereeComment(comment);
		this.noteRepository.save(note);
		return note;
	}

	public Note addCommentHandyWorker(final Note note, final String comment) {
		Assert.notNull(note);
		Assert.notNull(note.getId());

		//Logged user must be a referee
		final Authority a = new Authority();
		final UserAccount user = LoginService.getPrincipal();
		a.setAuthority(Authority.HANDYWORKER);
		Assert.isTrue(user.getAuthorities().contains(a));

		//Note's owner is logged customer
		Assert.isTrue(this.handyWorkerService.findByPrincipal() == note.getHandyWorker());
		note.setHandyWorkerComment(comment);
		this.noteRepository.save(note);
		return note;
	}

	public Collection<Note> findAll() {
		return this.noteRepository.findAll();
	}

	public Collection<Note> findByCustomerId(final Integer integer) {
		return this.noteRepository.findByCustomerId(integer);
	}

	public Note findOne(final int noteId) {
		return this.noteRepository.findOne(noteId);
	}

	public void delete(final Note note) {
		this.noteRepository.delete(note);

	}
}

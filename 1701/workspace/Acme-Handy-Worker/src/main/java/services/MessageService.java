
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Message;

@Service
@Transactional
public class MessageService {

	//Repository
	@Autowired
	public MessageRepository	messageRepository;

	//Services
	@Autowired
	public ActorService			actorService;

	@Autowired
	public BoxService			boxService;

	@Autowired
	public CustomisationService	customisationService;


	//Constructor
	public MessageService() {
		super();
	}

	//Simple CRUD
	public Message create() {
		Message res;
		res = new Message();
		//Logged actor is sender
		final Actor logActor;
		logActor = this.actorService.findByPrincipal();
		Assert.notNull(logActor);
		Assert.notNull(logActor.getId());
		res.setSender(logActor);
		res.setId(0);
		final Date d1 = Calendar.getInstance().getTime();
		res.setMoment(d1);

		res.setFlagSpam(false);
		return res;
	}
	//9.3
	public Message save(final Message message) {
		Assert.notNull(message);
		//Restrictions
		final Collection<Customisation> customisation = this.customisationService.findAll();
		final List<Customisation> customisation1 = new ArrayList<Customisation>(customisation);
		final List<String> spamWords = new ArrayList<String>(customisation1.get(0).getSpamWords());
		final String asunto = message.getSubject();
		final String cuerpo = message.getBody();
		final String texto = asunto + cuerpo;
		for (final String spamWord : spamWords)
			if (texto.toLowerCase().contains(spamWord)) {
				message.setFlagSpam(true);
				break;
			}
		Assert.isTrue(message.getFlagSpam() == false);

		//Logged actor is sender
		final Actor logActor;
		logActor = this.actorService.findByPrincipal();
		Assert.notNull(logActor);
		Assert.notNull(logActor.getId());
		Assert.isTrue(logActor.equals(message.getSender()));
		//Message saves in sender's out box

		final Message res;
		final Date d1 = Calendar.getInstance().getTime();
		message.setMoment(d1);
		res = this.messageRepository.save(message);

		final Box box = this.boxService.findOutBoxByUserPrincipal();
		final Collection<Message> ms = box.getMessages();
		final List<Message> mres = new ArrayList<Message>(ms);
		mres.add(res);
		box.setMessages(mres);
		this.boxService.save(box, false);

		//Message saves in recipient's corresponding box
		if (!message.getFlagSpam()) {
			final Box boxRecipient = this.boxService.findInBoxByUser(message);
			final Collection<Message> mRecipient = boxRecipient.getMessages();
			final List<Message> mResRecipient = new ArrayList<Message>(mRecipient);
			mResRecipient.add(res);
			boxRecipient.setMessages(mResRecipient);
			this.boxService.save(boxRecipient, false);

		} else if (message.getFlagSpam()) {
			final Box boxSRecipient = this.boxService.findSpamBoxByUser(message);
			boxSRecipient.getMessages().add(message);
			this.boxService.save(boxSRecipient, false);
		}

		return res;
	}
	public void delete(final Message message) {
		Assert.notNull(message);
		final Actor a = this.actorService.findByPrincipal();
		for (final Box b : a.getBoxes())
			for (final Message m : b.getMessages())
				if (m.getId() == message.getId()) {
					final List<Message> messages = new ArrayList<Message>(b.getMessages());
					messages.remove(message);
					b.setMessages(messages);
					this.boxService.save(b, false);
					break;
				}

		//Restrictions
		//		Assert.isTrue(message.getFlagSpam() == false);

	}

	//12.4
	//TODO: REVISAR ENTERA
	public Message createForActor(final Actor actor) {
		final Actor admin;
		admin = this.actorService.findByPrincipal();
		Assert.notNull(admin);

		final Message m = new Message();
		m.setRecipient(actor);
		m.setSender(admin);

		return m;

	}
	public Message sendAll(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Message res = null;

		//Logged user must be an administrator
		final Authority a = new Authority();
		final UserAccount user = LoginService.getPrincipal();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(user.getAuthorities().contains(a));

		for (final Actor act : this.actorService.findAll())
			//Message saves in recipient's corresponding box
			for (final Box b : act.getBoxes())
				if (b.getName().contains("In")) {
					message.setRecipient(act);
					res = this.messageRepository.save(message);

					final Box box = this.boxService.findOutBoxByUserPrincipal();
					final Collection<Message> ms = box.getMessages();
					final List<Message> mres = new ArrayList<Message>(ms);
					mres.add(res);
					box.setMessages(mres);
					this.boxService.save(box, false);

					final List<Message> col = new ArrayList<Message>(b.getMessages());
					res.setRecipient(act);
					col.add(res);
					b.setMessages(col);
					this.boxService.save(b, false);

				}

		return res;
	}
	public Collection<Message> findAll() {
		return this.messageRepository.findAll();
	}

	public Message findOne(final Message mes) {
		return this.messageRepository.findOne(mes.getId());
	}

	public Message findOne(final int id) {
		return this.messageRepository.findOne(id);
	}

}

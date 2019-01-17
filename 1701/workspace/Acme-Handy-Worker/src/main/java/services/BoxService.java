
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class BoxService {

	//Repository
	@Autowired
	public BoxRepository	boxRepository;

	@Autowired
	public ActorService		actorService;


	//Services

	//Constructor
	public BoxService() {
		super();
	}

	//Simple CRUD
	public Box create() {
		final Box box = new Box();
		box.setPredefined(false);
		final List<Message> m = new ArrayList<Message>();
		box.setMessages(m);
		return box;
	}
	//9.4
		public Box save(final Box box) {
		Assert.notNull(box);

		//Logged user must be a customer/handyworker
		//		final Authority a = new Authority();
		//		final Authority b = new Authority();
		//		final UserAccount user = LoginService.getPrincipal();
		//		a.setAuthority(Authority.CUSTOMER);
		//		b.setAuthority(Authority.HANDYWORKER);
		//Assert.isTrue(user.getAuthorities().contains(a) || user.getAuthorities().contains(b));

		//Restrictions
		//		Assert.notNull(box.getName());
		//		Assert.notNull(box.getPredefined());
		//		if (box.getPredefined() == true)
		//			Assert.isTrue(box.getName() == "In" || box.getName() == "out" || box.getName() == "trash" || box.getName() == "spam");
		final Box res;
		res = this.boxRepository.save(box);
		return res;
	}
	public Box save(final Box box, final Boolean creating) {
		Assert.notNull(box);

		//Restrictions
		//Assert.notNull(box.getName());
		//Assert.notNull(box.getPredefined());
		//		if (box.getPredefined() == true)
		//			Assert.isTrue(box.getName().contains("In") || box.getName().contains("Out") || box.getName().contains("Trash") || box.getName().contains("Spam"));
		final Box res;
		res = this.boxRepository.save(box);
		Boolean laTiene = false;
		if (creating == true) {
			final Actor outActor = this.actorService.findByPrincipal();
			final List<Box> resBox = new ArrayList<Box>(outActor.getBoxes());
			for (final Box b : resBox)
				if (b.getName().contains(box.getName())) {
					laTiene = true;
					break;
				}
			if (laTiene == false) {
				final List<Box> box1 = new ArrayList<Box>(outActor.getBoxes());
				box1.add(res);
				outActor.setBoxes(box1);
				this.actorService.save(outActor);
			}
		}

		return res;
	}
	public void delete(final Box box) {
		Assert.notNull(box);
		Assert.notNull(box.getId());
		if (box.getId() != 0) {
			Assert.isTrue(this.boxRepository.exists(box.getId()));

			//Restrictions
			Assert.notNull(box.getName());
			//			Assert.isTrue(box.getPredefined() == false);
			final Actor a = this.actorService.findByPrincipal();
			a.getBoxes().remove(box);
			this.boxRepository.delete(box);
		}
	}

	public Collection<Box> findAll() {
		return this.boxRepository.findAll();
	}

	public Box findOne(final int boxId) {
		return this.boxRepository.findOne(boxId);
	}

	public Collection<Box> findByUser() {
		Collection<Box> boxes = null;
		final Actor user = this.actorService.findByPrincipal();
		boxes = this.boxRepository.findBoxByActor(user.getId());

		return boxes;
	}

	public Box findOutBoxByUserPrincipal() {
		Collection<Box> boxes = null;
		Box res = null;
		final Actor user = this.actorService.findByPrincipal();
		boxes = this.boxRepository.findBoxByActor(user.getId());
		for (final Box b : boxes)
			if (b.getPredefined() == true && b.getName().contains("Out")) {
				res = b;
				break;
			}
		return res;
	}
	public Box findInBoxByUser(final Message message) {
		Collection<Box> box = null;
		Box res = null;
		box = this.boxesByUser(message);
		for (final Box b : box)
			if (b.getPredefined() == true && b.getName().contains("In")) {
				res = b;
				break;
			}
		return res;
	}

	public Collection<Box> boxesByUser(final Message message) {
		final Collection<Box> boxes = this.boxRepository.findBoxesByActorRecipient(message.getRecipient().getId());
		return boxes;
	}

	public Box findSpamBoxByUser(final Message message) {
		Collection<Box> box = null;
		Box res = null;
		box = this.boxesByUser(message);
		for (final Box b : box)
			if (b.getPredefined() == true && b.getName().contains("Spam")) {
				res = b;
				break;
			}
		return res;
	}

}

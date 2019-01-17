
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	@Autowired
	public SocialProfileRepository	socialProfileRepository;

	@Autowired
	public ActorService				actorService;


	//12.3
	public SocialProfile create() {

		final SocialProfile result = new SocialProfile();

		result.setLink("");
		result.setNick("");
		result.setSocialNetwork("");

		return result;
	}
	public SocialProfile save(final SocialProfile socialProfile) {

		Assert.notNull(socialProfile);

		final Actor logActor;
		logActor = this.actorService.findByPrincipal();

		if (socialProfile.getId() != 0) {
			//Logged actor is socialProfile's owner
			Assert.notNull(logActor);
			Assert.notNull(logActor.getId());
			final Collection<SocialProfile> col1 = logActor.getSocialProfiles();
			Assert.isTrue(col1.contains(socialProfile));
		}
		SocialProfile res;
		res = this.socialProfileRepository.save(socialProfile);
		final Collection<SocialProfile> oldSP = logActor.getSocialProfiles();
		oldSP.add(res);
		logActor.setSocialProfiles(oldSP);
		this.actorService.save(logActor);
		return res;
	}

	public void delete(final SocialProfile category) {

		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);

		final Actor logActor;
		logActor = this.actorService.findByPrincipal();
		final Collection<SocialProfile> oldSP = logActor.getSocialProfiles();
		oldSP.remove(category);
		logActor.setSocialProfiles(oldSP);
		this.actorService.save(logActor);

		this.socialProfileRepository.delete(category);

	}
	public Collection<SocialProfile> findAll() {
		return this.socialProfileRepository.findAll();
	}

	public SocialProfile findOne(final int categoryId) {
		return this.socialProfileRepository.findOne(categoryId);
	}

	public Collection<SocialProfile> findByUser() {
		Collection<SocialProfile> socialProfiles = null;
		final Actor user = this.actorService.findByPrincipal();
		socialProfiles = this.socialProfileRepository.findSocialProfilesByActor(user.getId());

		return socialProfiles;
	}
}

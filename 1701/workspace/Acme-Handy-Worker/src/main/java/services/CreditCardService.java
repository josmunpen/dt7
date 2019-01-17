
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.CreditCardRepository;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	@Autowired
	public CreditCardRepository	creditCardRepository;


	public CreditCard save(final CreditCard creditCard) {
		return this.creditCardRepository.save(creditCard);
	}

}

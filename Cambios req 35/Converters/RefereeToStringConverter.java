
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Referee;

@Component
@Transactional
public class RefereeToStringConverter implements Converter<Referee, String> {

	@Override
	public String convert(final Referee referee) {
		String res;

		if (referee == null)
			res = null;
		else
			res = String.valueOf(referee.getId());

		return res;
	}

}

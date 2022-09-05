import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pvt.crud.entity.User;

public class AuditorAwareImpl implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null || !auth.isAuthenticated()) {
				return null;
			}
			User user = (User) auth.getPrincipal();
			return Optional.of(user.getId());

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}

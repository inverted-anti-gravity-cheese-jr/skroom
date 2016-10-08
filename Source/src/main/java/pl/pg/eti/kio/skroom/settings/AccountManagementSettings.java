package pl.pg.eti.kio.skroom.settings;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Global settings for account management in app.
 *
 * @author Wojciech Stanisławski
 * @since 22.08.16
 */
public class AccountManagementSettings {
	private static final AtomicBoolean anonymousCanCreateAccount = new AtomicBoolean(true);

	public static boolean canAnonymousCreateAccount() {
		return anonymousCanCreateAccount.get();
	}

	public static void allowAnonymousToCreateAccount(boolean allow) {
		anonymousCanCreateAccount.getAndSet(allow);
	}

}

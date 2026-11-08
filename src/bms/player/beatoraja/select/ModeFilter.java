package bms.player.beatoraja.select;

import bms.player.beatoraja.PlayerConfig;
import bms.model.Mode;

/**
 * モードフィルター
 *
 * @author exch
 */
public enum ModeFilter {
	ALL("ALL", 0),
	BEAT_7K("7KEY", 2, Mode.BEAT_7K),
	BEAT_14K("14KEY", 4, Mode.BEAT_14K),
	POPN_9K("9KEY", 5, Mode.POPN_9K),
	BEAT_5K("5KEY", 1, Mode.BEAT_5K),
	BEAT_10K("10KEY", 3, Mode.BEAT_10K),
	KEYBOARD_24K("24KEY", 6, Mode.KEYBOARD_24K),
	KEYBOARD_24K_DOUBLE("48KEY", 7, Mode.KEYBOARD_24K_DOUBLE),
	BEAT_5K_7K("SINGLE", 8, Mode.BEAT_5K, Mode.BEAT_7K),
	BEAT_10K_14K("DOUBLE", 9, Mode.BEAT_10K, Mode.BEAT_14K);

	private static final ModeFilter[] DEFAULT_FILTERS = {
			ALL,
			BEAT_7K,
			BEAT_14K,
			POPN_9K,
			BEAT_5K,
			BEAT_10K,
			KEYBOARD_24K,
			KEYBOARD_24K_DOUBLE
	};

	private final String displayName;
	private final int skinNumber;
	private final Mode[] modes;

	ModeFilter(String displayName, int skinNumber, Mode... modes) {
		this.displayName = displayName;
		this.skinNumber = skinNumber;
		this.modes = modes;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getSkinNumber() {
		return skinNumber;
	}

	public Mode getPrimaryMode() {
		return modes.length > 0 ? modes[0] : null;
	}

	public boolean matches(int mode) {
		if (mode == 0 || modes.length == 0) {
			return true;
		}
		for (Mode m : modes) {
			if (m.id == mode) {
				return true;
			}
		}
		return false;
	}

	public boolean isEnabled(PlayerConfig config) {
		return config.isModeFilterEnabled(this);
	}

	public static ModeFilter nextEnabled(ModeFilter current, PlayerConfig config, int direction) {
		ModeFilter[] filters = values();
		int index = current != null ? current.ordinal() : ALL.ordinal();
		int step = direction >= 0 ? 1 : filters.length - 1;
		for (int count = 1; count <= filters.length; count++) {
			ModeFilter filter = filters[(index + step * count) % filters.length];
			if (filter.isEnabled(config)) {
				return filter;
			}
		}
		return ALL;
	}

	public static ModeFilter[] defaultFilters() {
		return DEFAULT_FILTERS.clone();
	}

	public static ModeFilter fromMode(Mode mode) {
		if (mode == null) {
			return ALL;
		}
		for (ModeFilter filter : values()) {
			if (filter.modes.length == 1 && filter.modes[0] == mode) {
				return filter;
			}
		}
		return ALL;
	}
}

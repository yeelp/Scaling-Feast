package yeelp.scalingfeast.features;

import java.util.Arrays;

public enum FilterListType {
	BLACKLIST {
		@Override
		boolean doesPassFilterList(int[] dims, int i) {
			return Arrays.binarySearch(dims, i) < 0;
		}
	},
	WHITELIST {
		@Override
		boolean doesPassFilterList(int[] dims, int i) {
			return Arrays.binarySearch(dims, i) >= 0;
		}
	};
	
	abstract boolean doesPassFilterList(int[] dims, int i);
}

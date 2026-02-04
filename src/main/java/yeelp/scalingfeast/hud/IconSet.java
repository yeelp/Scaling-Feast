package yeelp.scalingfeast.hud;

public enum IconSet {
    SHANKS,
    CARROTS(37);

    private final int offset;
    IconSet() {
        this(0);
    }

    IconSet(int offset) {
        this.offset = offset;
    }

    public int getVOffset() {
        return this.offset;
    }

    public int getContainerVCoord() {
        return this.offset + 45;
    }

    public boolean isCustom() {
        return this != SHANKS;
    }
}

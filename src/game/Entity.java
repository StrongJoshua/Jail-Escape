package game;

public interface Entity
{
	public int getTrueXCord();
	public int getRelativeXCord();
	public int getTrueYCord();
	public int getRelativeYCord();
	public int getHeight();
	public int getWidth();
	public float getWalkSpeedMod();
	public float getSpeed();
	public int getFacing();
	public int getBaseScale();
	public float getJumpSpeedMod();
	public String getImageFile();
	public int getAnimationSequence();
}

package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Entity
{
	private final int defMaxSpeed = 2, defFallWaitTime = 5, defJumpWaitTime = 20, defJumpRecovTime = 15, defRebound = 1, defDisableTime = 0,
			defReboundDistance = 15, defHurtTime = 60, defReboundSpeed = 3, defJumpSpeedMod = 1, maxHealth = 100, defSwitchTime = 20, baseScale = 2;
	private float walkSpeedMod, jumpSpeedMod, speed, lastSpeed, maxSpeed;
	private int width, height;
	private int xCord, yCord;
	private String imageFile;
	private final String healthBorderFile = "HealthBorder.png", healthFile = "Health.png", hurtSoundFile = "Hurt.wav";
	private Level currentLevel;
	private int fallWaitTime, jumpWaitTime, jumpRecovTime, disableTime, hurtTime, facing, animationSequence, scale, health, damageReceived, damageDone,
			switchTime, money;
	private boolean didWin, isMoveable, isMovingX, invOpenChange;
	private InventoryPlayer inv;
	private PlatformerCanvas instance;
	private ArrayList<Indicator> indicators;
	private Camera camera;
	
	public Player(){}
	
	public void initPlayer(int x, int y, Level currentLevel)
	{
		walkSpeedMod = 1;
		jumpSpeedMod = 0;
		speed = 0;
		facing = AI.RIGHT;
		this.currentLevel = currentLevel;
		scale = Level.getLevelScale() + 1;
		maxSpeed = defMaxSpeed * ((scale - 1)/2);
		updateSize();
		xCord = x;
		yCord = y;
		imageFile = "JailEscapePlayer.png";
		fallWaitTime = defFallWaitTime;
		jumpWaitTime = defJumpWaitTime;
		disableTime = 0;
		jumpRecovTime = 0;
		hurtTime = 0;
		didWin = false;
		isMoveable = true;
		isMovingX = false;
		inv = new InventoryPlayer(this);
		invOpenChange = false;
		animationSequence = 0;
		health = maxHealth;
		switchTime = defSwitchTime;
		indicators = new ArrayList<Indicator>();
		camera = new Camera(this, instance);
		money = 0;
	}

	public void tick(boolean[] keys)
	{
		for(int i = 2; i < keys.length; i++)
		{
			if(i != 3 && i != 5 && keys[i] == true)
			{
				isMovingX = true;
				break;
			}
			isMovingX = false;
		}
		disableTime--;
		hurtTime--;
		if(disableTime > 0)
		{
			setMoveable(false);
		}
		else if(disableTime <= 0 && !getIfWon())
		{
			setMoveable(true);
		}
		if(isMoveable == false)
		{
			return;
		}
		
		if(keys[5] == false)
		{
			invOpenChange = false;
		}
		
		if(keys[5] == true && invOpenChange == false)
		{
			if(inv.getIsOpen())
			{
				closeInv();
			}
			else
				openInv();
		}
		
		if(inv.getIsOpen())
		{
			inv.tick(instance);
			return;
		}

		updateSize();
		
		move();
		//update the camera
		camera.update();
		
		if(Level.isOnGround(this, currentLevel.getBlocks()))
		{
			jumpWaitTime--;
		}
		if(keys[1] == true)
		{
			if(Level.isOnGround(this, currentLevel.getBlocks()))
			{
				if(jumpWaitTime <= 0)
				{
					jumpRecovTime = defJumpRecovTime;
					jumpSpeedMod = defJumpSpeedMod * (scale);
					jumpWaitTime = defJumpWaitTime;
				}
			}
				
		}
		if(keys[2] == true)
		{
			if(speed > maxSpeed * -1)
			{
				speed--;
			}
			else if(speed < maxSpeed * -1)
			{
				speed++;
			}
		}
		
		if(keys[4] == true)
		{
			if(speed < maxSpeed)
			{
				speed++;
			}
			else if(speed > maxSpeed)
			{
				speed--;
			}
		}
		
		//slow down if not moving
		if(keys[2] == false && keys[4] == false)
		{
			if(speed < 0)
			{
				speed++;
			}
			else if(speed > 0)
			{
				speed--;
			}
		}
		/////////////////
		/////////////////
		//Attacking//////
		/////////////////
		/////////////////
		updateAnimationSequence();
		updateIndicators();
		checkHealth();
	}
	
	public String getImageFile()
	{
		return imageFile;
	}
	
	public int getTrueXCord()
	{
		return xCord;
	}
	
	public int getRelativeXCord()
	{
		int x = getTrueXCord();
		if(x - (instance.getWidth()/2) < 0)
			x = getTrueXCord();
		else if(x + (instance.getWidth()/2) > currentLevel.getWidth())
			x = instance.getWidth() - (currentLevel.getWidth() - x);
		else
			x = instance.getWidth()/2;
		return x;
	}
	
	public int getTrueYCord()
	{
		return yCord;
	}
	
	public int getRelativeYCord()
	{
		int y = getTrueYCord();
		if(y - (instance.getHeight()/2) < 0)
			y = getTrueYCord();
		else if(y + (instance.getHeight()/2) > currentLevel.getHeight())
			y = instance.getHeight() - (currentLevel.getHeight() - y);
		else
			y = instance.getHeight()/2;
		return y;
	}
	
	public void setXCord(int i)
	{
		xCord = i;
	}
	
	public void setYCord(int i)
	{
		yCord = i;
	}
	
	@Deprecated
	public void moveLeft(int mod)
	{
		walkSpeedMod += mod;
		setXCord((int)(getTrueXCord() - (maxSpeed * walkSpeedMod)));
		if(mod != 0)
		{
			walkSpeedMod = 1;
		}
		//check world collisions
		if(getTrueXCord() < 0)
		{
			setXCord(0);
			return;
		}
		//check block collisions
		Object [] o = Level.checkCollisions(this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
				setXCord(((Block)o[1]).getTrueXCord() + ((Block)o[1]).getScaledWidth());
				//check if you won
				if(((Block)o[1]).isWinBlock())
				{
					setDidWin(true);
					setMoveable(false);
				}
					
		}
		//check NPC collisions
		o = Level.checkNPCCollisions(this, currentLevel.getNPCs());
		if((boolean) o[0] && hurtTime <= 0)
		{
			setXCord(((NPC)o[1]).getTrueXCord() + ((NPC)o[1]).getWidth());
			hurtTime = defHurtTime;
			moveRight(defReboundDistance);
			jumpSpeedMod = defRebound;
		}
	}
	
	@Deprecated
	public void moveRight(int mod)
	{
		walkSpeedMod += mod;
		setXCord((int)(getTrueXCord() + (maxSpeed * walkSpeedMod)));
		if(mod != 0)
		{
			walkSpeedMod = 1;
		}
		//check world collisions
		if(getTrueXCord() + width >= instance.getWidth())
		{
			setXCord(instance.getWidth() - width);
			return;
		}
		//check block collisions
		Object [] o = Level.checkCollisions(this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
				setXCord(((Block)o[1]).getTrueXCord() - width);
				//check if you won
				if(((Block)o[1]).isWinBlock())
				{
					setDidWin(true);
					setMoveable(false);
				}
		}
		//check NPC collisions
		o = Level.checkNPCCollisions(this, currentLevel.getNPCs());
		if((boolean) o[0] && hurtTime <= 0)
		{
			setXCord(((NPC)o[1]).getTrueXCord() - getWidth());
			hurtTime = defHurtTime;
			moveLeft(defReboundDistance);
			jumpSpeedMod = defRebound;
		}
	}
	
	public void move()
	{
		int [] lastCord = new int [] {getTrueXCord(), getTrueYCord()};
		/*//check if you would hit a block
				Object [] o2 = Level.checkWouldMoveThroughX(this, currentLevel.getBlocks());
				//if not, move x
				if(!(boolean) o2[0])
				{
					setXCord((int)(getTrueXCord() + (speed * walkSpeedMod)));
				}
				else
				{
					Block b = (Block) o2[1];
					//check whether moving left or right
					if(speed > 0)
					{
						setXCord(b.getTrueXCord() - getWidth());
					}
					else if(speed < 0)
					{
						setXCord(b.getTrueXCord() + b.getScaledWidth());
					}
				}
				//set collision check
				Object [] o = Level.checkCollisions(this, currentLevel.getBlocks());
				//check x block collisions
				if((boolean) o[0])
				{
					int x = 0;
					if((Block)o[1] instanceof BlockDoor)
						x = ((Block)o[1]).getScaledWidth() - (2 * (scale - 1));
					//check which side you're on
					if(speed > 0)
					{
						setXCord(((Block)o[1]).getTrueXCord() - getWidth() + x);
					}
					else if(speed < 0)
					{
						setXCord(((Block)o[1]).getTrueXCord() + ((Block)o[1]).getScaledWidth());
					}
					//check if you won
					if(((Block)o[1]).isWinBlock())
					{
						setDidWin(true);
						setMoveable(false);
					}
					//open doors
					if((Block)o[1] instanceof BlockDoor && useItem("ItemKey", true))
					{
						((BlockDoor)o[1]).setIsOpen(true);
					}
				}
		//move y
		setYCord((int)(getYCord() - (jumpSpeedMod * (scale/2))));
		//check y block collisions
		o = Level.checkCollisions(this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
			if(jumpSpeedMod < 0)
				setYCord(((Block)o[1]).getYCord());
			if(jumpSpeedMod > 0)
			{
				setYCord(((Block)o[1]).getYCord() + ((Block)o[1]).getScaledHeight() + getHeight());
				jumpSpeedMod = 0;
			}
			//check if you won
			if(((Block)o[1]).isWinBlock())
			{
				setDidWin(true);
				setMoveable(false);
			}
			//open doors
			if((Block)o[1] instanceof BlockDoor && useItem("ItemKey", true))
			{
				((BlockDoor)o[1]).setIsOpen(true);
			}
		}
		*/
		Object [] o;
		
		//check if you would move through a block
		o = Level.checkWouldMoveThroughX(this, currentLevel.getBlocks());
		if(!(boolean) o[0])
		{
			//if not, do regular x movement
			o = Level.checkXCollisions(lastCord, this, currentLevel.getBlocks());
			if((boolean) o[0])
			{
				int x = 0;
				if((Block)o[1] instanceof BlockDoor)
					x = ((Block)o[1]).getScaledWidth() - (2 * (scale - 1));
				//check which side you're on
				if(speed > 0)
				{
					setXCord(((Block)o[1]).getTrueXCord() - getWidth() + x);
				}
				else if(speed < 0)
				{
					setXCord(((Block)o[1]).getTrueXCord() + ((Block)o[1]).getScaledWidth());
				}
				//check if you won
				if(((Block)o[1]).isWinBlock())
				{
					setDidWin(true);
					setMoveable(false);
				}
				//open doors
				if((Block)o[1] instanceof BlockDoor && useItem("ItemKey", true))
				{
					((BlockDoor)o[1]).setIsOpen(true);
				}
			}
			else
			{
				setXCord((int)(getTrueXCord() + (speed * walkSpeedMod)));
			}
		}
		else
		{
			//if you would, then stop at the block
			Block b = (Block) o[1];
			//check which side you're on
			if(speed > 0)
			{
				setXCord(b.getTrueXCord() - getWidth());
			}
			else if(speed < 0)
			{
				setXCord(b.getTrueXCord() + b.getScaledWidth());
			}
		}
		//check y collisions
		lastCord = new int [] {getTrueXCord(), getTrueYCord()};
		o = Level.checkYCollisions(lastCord, this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
			//if you hit, then adjust accordingly
			if(lastCord[1] < ((Block)o[1]).getTrueYCord())
				setYCord(((Block)o[1]).getTrueYCord());
			if(lastCord[1] > ((Block)o[1]).getTrueYCord())
			{
				setYCord(((Block)o[1]).getTrueYCord() + ((Block)o[1]).getScaledHeight() + getHeight());
				jumpSpeedMod = 0;
			}
			//check if you won
			if(((Block)o[1]).isWinBlock())
			{
				setDidWin(true);
				setMoveable(false);
			}
			//open doors
			if((Block)o[1] instanceof BlockDoor && useItem("ItemKey", true))
			{
				((BlockDoor)o[1]).setIsOpen(true);
			}
		}
		else
		{
			//if you don't hit, move as normally
			setYCord((int)(getTrueYCord() - (jumpSpeedMod * (scale/2))));
		}
		//check NPC collisions
		o = Level.checkNPCCollisions(this, currentLevel.getNPCs());
		//check player with NPC
		if((boolean) o[0] && hurtTime <= 0 && getIsMovingX())
		{
			NPC npc = (NPC)o[1];
			if(speed > 0)
			{
				setXCord(npc.getTrueXCord() - width);
				hurtTime = defHurtTime;
				jumpSpeedMod = defRebound * ((scale - 1)/2);
				speed = defReboundSpeed * -1 * ((scale - 1)/2);
			}
			else if(speed < 0)
			{
				setXCord(npc.getTrueXCord() + npc.getWidth());
				hurtTime = defHurtTime;
				jumpSpeedMod = defRebound * ((scale - 1)/2);
				speed = defReboundSpeed * ((scale - 1)/2);
			}
			getHurt(npc);
		}
		//reset NPC check
		o = Level.checkNPCCollisions(this, currentLevel.getNPCs());
		//check NPC with player
		if((boolean) o[0] && hurtTime <= 0 && getIsMovingX() == false)
		{
			NPC npc = (NPC)o[1];
			if(npc.getFacing() == AI.RIGHT)
			{
				setXCord(npc.getTrueXCord() + npc.getWidth());
				hurtTime = defHurtTime;
				jumpSpeedMod = defRebound * ((scale - 1)/2);
				speed = defReboundSpeed * ((scale - 1)/2);
			}
			else if(npc.getFacing() == AI.LEFT)
			{
				setXCord(npc.getTrueXCord() - getWidth());
				hurtTime = defHurtTime;
				jumpSpeedMod = defRebound * ((scale - 1)/2);
				speed = defReboundSpeed * -1 * ((scale - 1)/2);
			}
			else if(npc.getFacing() == AI.FORWARD)
			{
				hurtTime = defHurtTime;
				jumpSpeedMod = defRebound * ((scale - 1)/2);
			}
			getHurt(npc);
		}
		//check world collisions
		if(getTrueXCord() + width > currentLevel.getWidth())
		{
			setXCord(currentLevel.getWidth() - width);
		}
		if(getTrueXCord() < 0)
		{
			setXCord(0);
		}
		//check item collisions
		Object [] o3 = Level.checkItemCollisions(this, currentLevel.getItems());
		if((boolean) o3[0] && ((Item) o3[1]).getDropTime() <= 0)
		{
			pickupItem((Item) o3[1]);
		}
		//handle fall speed
		fallWaitTime--;
		if(fallWaitTime <= 0)
		{
			jumpSpeedMod -= ((scale - 1)/2);
			fallWaitTime = defFallWaitTime;
		}
		//handle jump recovery, jump wait time, and reset fall time
		if(Level.isOnGround(this, currentLevel.getBlocks()))
		{
			jumpRecovTime--;
			walkSpeedMod = .5f;
			if(jumpRecovTime <= 0)
			{
				walkSpeedMod = 1;
			}
			if(hurtTime <= 0)
			{
				jumpSpeedMod = 0;
			}
			fallWaitTime = defFallWaitTime;
		}
		lastSpeed = speed;
	}
	
	public void setDidWin(boolean b)
	{
		didWin = b;
	}
	
	public boolean getIfWon()
	{
		return didWin;
	}
	
	public void setMoveable(boolean b)
	{
		isMoveable = b;
	}
	
	@Override
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
	
	public boolean getIsMovingX()
	{
		return isMovingX;
	}
	
	public int getHurtTime()
	{
		return hurtTime;
	}
	
	public float getWalkSpeedMod()
	{
		return walkSpeedMod;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public int getFacing()
	{
		return facing;
	}
	
	public void openInv()
	{
		inv.setIsOpen(true);
		invOpenChange = true;
	}
	
	public void closeInv()
	{
		inv.setIsOpen(false);
		inv.tick(instance);
		invOpenChange = true;
	}
	
	public InventoryPlayer getInventory()
	{
		return inv;
	}
	
	public void pickupItem(Item item)
	{
		inv.addItem(item);
		item.setIfInInv(true);
	}
	
	public boolean useItem(String s, boolean used)
	{
		return inv.removeItemType(s, used);
	}
	
	public int getAnimationSequence()
	{
		return animationSequence;
	}
	
	@Override
	public int getBaseScale()
	{
		return baseScale;
	}
	
	@Override
	public float getJumpSpeedMod()
	{
		return jumpSpeedMod;
	}
	
	public void dropItem(Item i)
	{
		i.setXCord(getTrueXCord());
		i.setYCord(getTrueYCord() - getHeight());
		Random r = new Random();
		int s = (r.nextInt(17) - 8);
		int j = r.nextInt(8);
		i.setSpeed(s);
		i.setJumpSpeedMod(j);
		i.setIfInInv(false);
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public Level getCurrentLevel()
	{
		return currentLevel;
	}
	
	private void updateFacing()
	{
		if(instance == null)
		{
			return;
		}
		if(instance.input.getMouseX() == 0 || instance.input.getMouseX() >= instance.getWidth())
			return;
		int mouseX = instance.input.getMouseX();
		if(mouseX < this.getRelativeXCord())
		{
			facing = AI.LEFT;
		}
		else if(mouseX > this.getRelativeXCord())
		{
			facing = AI.RIGHT;
		}
		
	}
	
	public void giveInstance(PlatformerCanvas instance)
	{
		this.instance = instance;
	}
	
	private void updateSize()
	{
		BufferedImage image = instance.painter.getImage(this);
		if(image == null)
			return;
		width = image.getWidth() * ((scale - 1)/2);
		height = image.getHeight() * ((scale - 1)/2);
	}
	
	public int getDamageReceived()
	{
		return damageReceived;
	}
	
	public int getDamageDone()
	{
		return damageDone;
	}
	
	public int getHealth()
	{
		int h = health;
		if(h < 0)
			h = 0;
		return h;
	}
	
	public int getMaxHealth()
	{
		return maxHealth;
	}
	
	private void updateAnimationSequence()
	{
		int f = facing;
		updateFacing();
		//fix animation drawings
		animationSequence = 0;
		if(animationSequence == 0)
		return;
		switchTime--;
		if(f != facing || switchTime <= 0)
		{
			switchTime = defSwitchTime;
			//update direction image
			if(facing == AI.RIGHT && (animationSequence != 1 && animationSequence != 2 && animationSequence != 3 && animationSequence != 4))
			{
				animationSequence = 1;
				return;
			}
			else if(facing == AI.LEFT && (animationSequence != 5 && animationSequence != 6 && animationSequence != 7 && animationSequence != 8))
			{
				animationSequence = 4;
				return;
			}
			//update animation state
			if(animationSequence == 1)
			{
				animationSequence = 2;
			}
			else if(animationSequence == 2)
			{
				animationSequence = 3;
			}
			else if(animationSequence == 3)
			{
				animationSequence = 4;
			}
			else if(animationSequence == 4)
			{
				animationSequence = 1;
			}
			else if(animationSequence == 5)
			{
				animationSequence = 6;
			}
			else if(animationSequence == 6)
			{
				animationSequence = 7;
			}
			else if(animationSequence == 7)
			{
				animationSequence = 8;
			}
			else if(animationSequence == 8)
			{
				animationSequence = 5;
			}
		}
	}
	
	private void updateIndicators()
	{
		tickIndicators();
		reorganizeIndicatorList();
	}
	
	private void tickIndicators()
	{
		Object [] inds = indicators.toArray();
		for(int i = 0; i < inds.length; i++)
		{
			((Indicator)inds[i]).tick();
		}
	}
	
	private void reorganizeIndicatorList()
	{
		ArrayList<Indicator> temp = new ArrayList<Indicator>();
		for(int i = 0; i < indicators.size(); i++)
		{
			if(indicators.get(i).getTimer() < 100)
				temp.add(indicators.get(i));
		}
		indicators = temp;
	}
	
	public ArrayList<Indicator> getIndicators()
	{
		return indicators;
	}
	
	public String getHealthBorderFile()
	{
		return healthBorderFile;
	}
	
	public String getHealthFile()
	{
		return healthFile;
	}
	
	private void getHurt(NPC npc)
	{
		damageReceived = npc.getDamage();
		health -= damageReceived;
		indicators.add(new IndicatorDamage(damageReceived, Level.getMiddleX(npc), npc.getTrueYCord() - npc.getHeight(), false, this));
		instance.input.getSound(hurtSoundFile).play();
	}
	
	private void checkHealth()
	{
		if(health <= 0)
		{
			instance.game.setSession("RestartMenu");
		}
	}
}

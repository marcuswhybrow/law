package net.marcuswhybrow.minecraft.law.prison;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.marcuswhybrow.minecraft.law.InventoryManager;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.events.LawDetainEndEvent;
import net.marcuswhybrow.minecraft.law.events.LawFreeEvent;
import net.marcuswhybrow.minecraft.law.interfaces.Referenceable;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;
import net.marcuswhybrow.minecraft.law.utilities.Utils;

public class PrisonDetainee implements Referenceable, Serializable {
	/** The states which a prison detainee can be in. */
	public static enum State {TO_BE_IMPRISONED, IMPRISONED, TO_BE_FREED, FREE};
	
	private static final long serialVersionUID = 4744294654991533826L;
	/** The case-sensitive in-game name of this player. */
	private String playerName;
	/** The time (in seconds from January 1st 1970) that this prisoner was detained. */
	private long detentionTime;
	/** The period (in seconds) that this prisoner should be detained for. */
	private long detentionDuration;
	/** The id of the synchronous task which will release this prisoner in the future. */
	private int releaseTaskId;
	/** The prison cell which the prisoner is detained within. */
	private PrisonCell prisonCell;
	/** The current state this detainee is in. */
	private State state;
	
	public PrisonDetainee(String playerName, PrisonCell prisonCell, long detentionDuration) {
		if (playerName == null)
			throw new IllegalArgumentException("Player name cannot be null.");
		if (prisonCell == null)
			throw new IllegalArgumentException("Prison cell cannot be null.");
		
		this.playerName = playerName;
		this.setDetentionDuration(detentionDuration);
		this.prisonCell = prisonCell;
		this.state = State.FREE;
		
		// The initial state of -2 equates to not being set yet
		// whereas a value of -1 would mean that setting the task failed
		this.releaseTaskId = -2;
	}

	@Override
	public void setName(String name) {
		this.playerName = name;
	}

	@Override
	public String getName() {
		return this.playerName;
	}
	
	/**
	 * Get the Bukkit player instance which represents this
	 * prison detainee if they are online, otherwise returns null.
	 * 
	 * @return The Player representation of this prisoner if they are online or null
	 */
	public Player getPlayer() {
		Player player = Bukkit.getPlayerExact(this.getName());
		
		if (player != null) {
			// Update the playerName name to use correct case
			this.setName(player.getName());
		}
		
		return player;
	}

	/**
	 * @return the detentionDuration
	 */
	public long getDetentionDuration() {
		return detentionDuration;
	}

	/**
	 * @param detentionDuration the detentionDuration to set
	 */
	public void setDetentionDuration(long detentionDuration) {
		this.detentionDuration = Math.max(detentionDuration, 0);
	}
	
	/**
	 * Ensures that the delayed synchronous task which will release
	 * this detainee is set, by creating it if it is not found.
	 * 
	 * @return True if the task had to be set, false if the task was already set
	 */
	public boolean ensureReleaseIsScheduled() {
		
		boolean taskNotSet = this.releaseTaskId < 0;
		boolean taskNotRunning = Bukkit.getScheduler().isCurrentlyRunning(releaseTaskId) == false;
		boolean taskNotQueued = Bukkit.getScheduler().isQueued(releaseTaskId) == false;
		boolean taskShouldBeScheduled = taskNotSet || taskNotRunning && taskNotQueued;
		
		if (taskShouldBeScheduled) {
			// Get the necessary parameters to create the delayed task
			Plugin plugin = Law.get().getPlugin();
			PrisonDetaineeReleaseTask task = new PrisonDetaineeReleaseTask(this);
			long delay = Utils.toTicks(this.getSecondsUntilRelease());
			
			// Create the task
			this.releaseTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
			
			if (this.releaseTaskId == -1) {
				// The task could not be created for some reason
				MessageDispatcher.consoleWarning("Prisoner release could not be scheduled.");
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes any in-place release task and creates a new one.
	 * This should be called when the detention time or duration
	 * changes.
	 */
	public void updateReleaseSchedule() {
		this.cancelReleaseSchedule();
		this.ensureReleaseIsScheduled();
	}
	
	/**
	 * Get the number of seconds between now and the time which this
	 * detainee should be released.
	 * 
	 * @return THe number of seconds between now and when this detainee should be released
	 */
	public long getSecondsUntilRelease() {
		return this.detentionTime + this.detentionDuration - (System.currentTimeMillis() / 1000L);
	}
	
	@Override
	public int hashCode() {
		return this.playerName.hashCode();
	}
	
	/**
	 * If the detainee is currently free, moves
	 * this detainee's state to TO_BE_IMPRISONED.
	 */
	public boolean startDetention() {
		if (state == State.FREE) {
			this.state = State.TO_BE_IMPRISONED;			
			this.prisonCell.addDetainee(this);
			this.detentionTime = System.currentTimeMillis() / 1000L;
			this.ensureReleaseIsScheduled();
			return true;
		}
		return false;
	}
	
	/**
	 * Effects the in-game player so that they are
	 * regarded as in-prison.
	 * 
	 * @return True if the action could be performed according
	 * to the current state of the detainee and the player was
	 * on-line, false otherwise
	 */
	public boolean imprisonInGamePlayer() {
		if (state == State.TO_BE_IMPRISONED) {
			Player player = this.getPlayer();
			if (player != null) {
				// Effect the player
				player.teleport(this.prisonCell.getLocation());
				InventoryManager.confiscate(player);
				player.setSleepingIgnored(true);
				
				this.state = State.IMPRISONED;
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Effects the in-game player so that they are
	 * regarded as free.
	 * 
	 * @return True if the action could be performed
	 * according to the current state of the detainee,
	 * and the player was on-line, false otherwise.
	 */
	public boolean freeInGamePlayer() {
		if (state == State.TO_BE_FREED) {
			Player player = this.getPlayer();
			if (player != null) {
				// Get the best location
				Location location = this.prisonCell.getPrison().getExitPoint();
				if (location == null) {
					location = this.prisonCell.getPrison().getLawWorld().getBukkitWorld().getSpawnLocation();
				}
				
				// Effect the player
				player.teleport(location);
				InventoryManager.restore(player);
				player.setSleepingIgnored(false);
				
				this.state = State.FREE;
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If the player is detained but not imprisoned
	 * the state is set to FREE, if the detainee is
	 * imprisoned then the state is set to TO_BE_FREED.
	 * 
	 * @return True if state could be changed to FREE
	 * or TO_BE_FREED, false otherwise.
	 */
	public boolean endDetention() {
		if (state == State.TO_BE_IMPRISONED) {
			this.state = State.FREE;
			this.prisonCell.removeDetainee(this.getName());
			return true;
		} else if (state == State.IMPRISONED) {
			this.state = State.TO_BE_FREED;
			this.prisonCell.removeDetainee(this.getName());
			return true;
		}
		return false;
	}
	
	/**
	 * Moves this detainee into the free state.
	 */
	public void setFreed() {
		this.state = State.FREE;
	}
	
	/**
	 * Returns the current state of this detainee.
	 * 
	 * @return The current state
	 */
	public State getState() {
		return this.state;
	}
	
	/**
	 * Returns the prison cell for this player.
	 * 
	 * @return The prison cell for this player
	 */
	public PrisonCell getPrisonCell() {
		return this.prisonCell;
	}
	
	// Private Methods
	
	/**
	 * Removes the release task from the Bukkit scheduler
	 * and resets the stored task id to -2.
	 */
	private void cancelReleaseSchedule() {
		if (this.releaseTaskId >= 0) {
			Bukkit.getScheduler().cancelTask(this.releaseTaskId);
			this.releaseTaskId = -2;
		}
	}
	
	// Nested classes
	
	/**
	 * A runnable task which is executed when a prisoner is to be released
	 * from prison.
	 * 
	 * @author Marcus Whybrow
	 *
	 */
	private class PrisonDetaineeReleaseTask implements Runnable {
		/** The prison detainee which this task will release from prison. */
		private PrisonDetainee prisonDetainee;
		
		public PrisonDetaineeReleaseTask(PrisonDetainee prisonDetainee) {
			this.prisonDetainee = prisonDetainee;
		}

		@Override
		public void run() {
			Law.fireEvent(new LawDetainEndEvent(prisonDetainee));
			Law.fireEvent(new LawFreeEvent(prisonDetainee));
		}
		
	}
}

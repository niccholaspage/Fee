package org.melonbrew.fee.commands;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TrapDoor;
import org.melonbrew.fee.Fee;
import org.melonbrew.fee.Phrase;
import org.melonbrew.fee.Session;

public class YesCommand implements CommandExecutor {
	private final Fee plugin;

	private final Random random;

	public YesCommand(Fee plugin){
		this.plugin = plugin;

		random = new Random();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)){
			if (random.nextFloat() <= 0.50F){
				Phrase.YOU_ARE_NOT_A_PLAYER.sendWithPrefix(sender);
			}else {
				Phrase.YOU_ARE_NOT_A_PLAYER_TWO.sendWithPrefix(sender);
			}

			return true;
		}

		Player player = (Player) sender;

		Session session = plugin.getSession(player);

		if (session == null){
			Phrase.NO_PENDING_COMMAND.sendWithPrefix(sender);

			return true;
		}

		String command = session.getCommand();

		Block block = session.getBlock();

		Sign sign = null;

		if (block != null){
			sign = plugin.getSign(player, block, true);
		}

		if (sign == null && block != null){
			plugin.removeSession(player);

			Phrase.NO_PENDING_COMMAND.sendWithPrefix(sender);

			return true;
		}

		if (!isBlockInRadius(block, player.getLocation().getBlock(), 5)){
			plugin.removeSession(player);

			Phrase.FAR_AWAY_FROM_BLOCK.sendWithPrefix(sender);

			return true;
		}

		double money;

		if (command == null){
			money = Double.parseDouble(sign.getLine(1));
		}else {
			money = plugin.getKeyMoney(command);
		}

		if (money == -1){
			plugin.removeSession(player);

			Phrase.NO_PENDING_COMMAND.sendWithPrefix(sender);

			return true;
		}

		if (!plugin.getEconomy().has(player.getName(), money)){
			Phrase.NEED_MONEY.sendWithPrefix(sender, plugin.getEconomy().format(money));

			return true;
		}

		plugin.getEconomy().withdrawPlayer(player.getName(), money);

		String reciever = null;

		if (sign != null){
			reciever = sign.getLine(2);
		}else {			
			reciever = plugin.getConfig().getString("serveraccount");
		}

		if (plugin.getEconomy().hasAccount(reciever)){
			plugin.getEconomy().depositPlayer(reciever, money);
		}

		if (command == null){
			BlockState state = block.getState();

			MaterialData data = state.getData();

			if (data instanceof Door || data instanceof TrapDoor){
				openDoor(block);
			} else if (state instanceof Furnace){
				player.openInventory(((Furnace) state).getInventory());
			} else if (state instanceof Chest){
				player.openInventory(((Chest) state).getInventory());
			} else if (data instanceof Gate){
				((Gate) data).setOpen(true);
			}

			plugin.removeSession(player);
		}else {
			session.setNextCommandFree(true);

			player.chat(command);
		}

		return true;
	}

	private boolean isBlockInRadius(Block block, Block playerBlock, int radius){
		int x = block.getX();

		int y = block.getY();

		int z = block.getZ();

		int playerX = playerBlock.getX();

		int playerY = playerBlock.getY();

		int playerZ = playerBlock.getZ();

		if (x > playerX){
			if (x - playerX > radius){
				return false;
			}
		}else {
			if (playerX - x > radius){
				return false;
			}
		}

		if (y > playerY){
			if (y - playerY > radius){
				return false;
			}
		}else {
			if (playerY - y > radius){
				return false;
			}
		}

		if (z > playerZ){
			if (z - playerZ > radius){
				return false;
			}
		}else {
			if (playerZ - z > radius){
				return false;
			}
		}

		return true;
	}

	private boolean isDoorClosed(Block block){
		if (block.getType() == Material.TRAP_DOOR){
			
			TrapDoor trapdoor = (TrapDoor)block.getState().getData();
			
			return !trapdoor.isOpen();
		} else{
			byte data = block.getData();
			
			if ((data & 0x8) == 0x8){
				block = block.getRelative(BlockFace.DOWN);
				
				data = block.getData();
			}
			
			return ((data & 0x4) == 0);
		}
	}

	private void openDoor(Block block) {
		if (block.getType() == Material.TRAP_DOOR){
			BlockState state = block.getState();

			TrapDoor trapdoor = (TrapDoor)state.getData();

			trapdoor.setOpen(true);

			state.update();
		} else {
			byte data = block.getData();

			if ((data & 0x8) == 0x8){
				block = block.getRelative(BlockFace.DOWN);

				data = block.getData();
			}
			if (isDoorClosed(block)){
				data = (byte) (data | 0x4);

				block.setData(data, true);

				block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
			}
		}
	}

	private void closeDoor(Block block){
		if (block.getType() == Material.TRAP_DOOR){
			BlockState state = block.getState();

			TrapDoor trapdoor = (TrapDoor)state.getData();

			trapdoor.setOpen(false);

			state.update();
		} else {
			byte data = block.getData();

			if ((data & 0x8) == 0x8){
				block = block.getRelative(BlockFace.DOWN);

				data = block.getData();
			}
			if (!isDoorClosed(block)) {
				data = (byte) (data & 0xb);

				block.setData(data, true);

				block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
			}
		}
	}
}

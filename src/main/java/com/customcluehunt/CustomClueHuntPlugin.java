package com.customcluehunt;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.util.ImageUtil;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import java.util.Set;



@Slf4j
@PluginDescriptor(
	name = "Custom Clue Hunt"
)





public class CustomClueHuntPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CustomClueHuntConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	private CustomClueHuntPanel panel;
	private NavigationButton navButton;

	private final Set<NPC> attackedNpcs = new HashSet<>();


	@Override
	protected void startUp() throws Exception
	{
		panel = new CustomClueHuntPanel();

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/panel_icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Custom Clue Hunt")
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);

		log.info("Example started!");

	}

	@Override
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
		log.info("Example stopped!");

	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged event)
	{
		if (event.getSource() == client.getLocalPlayer() && event.getTarget() instanceof NPC)
		{
			NPC npc = (NPC) event.getTarget();
			attackedNpcs.add(npc);
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		NPC npc = event.getNpc();

		if (attackedNpcs.remove(npc) && npc.getHealthRatio() == 0)
		{
			log.info("Player killed NPC: " + npc.getName());
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	public void writeMessageToPanel(String message)
	{
		panel.setMessage(message);
	}

	public void writeWhatsEquiped(String message)
	{
		panel.getEquiped(message);
	}


	@Subscribe
	public void onGameTick(GameTick event)
	{
		String allEquipment = "";

		int x = 0;
		int y = 0;
		int plane = 0;
		if (client.getLocalPlayer() != null)
		{
			WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
			x = playerLocation.getX();
			y = playerLocation.getY();
			plane = playerLocation.getPlane();

			// Log or use the player's position
			System.out.println("Player position: X=" + x + ", Y=" + y + ", Plane=" + plane);
		}

		if (client.getLocalPlayer().getAnimation() == AnimationID.DIG)
		{
			writeMessageToPanel("Digging at X:" + x + " Y: " + y);
		}


		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			return;
		}

		for (Item item : equipment.getItems())
		{
			int itemId = item.getId();
			int quantity = item.getQuantity();

			// Do something with the item ID and quantity...
			//log.info("Player has equipped item with ID: " + itemId + " and quantity: " + quantity);
			allEquipment = "Player has equipped item with ID:\n" + itemId + "\n and quantity: \n" + quantity;
		}

		Item headItem = equipment.getItem(EquipmentInventorySlot.HEAD.getSlotIdx());
		if (headItem == null) { log.info("No item equipped in head slot."); }
		else
		{ allEquipment += "\nItem equipped in head slot: " + headItem.getId(); }

		writeWhatsEquiped(allEquipment);



	}

	@Provides
	CustomClueHuntConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomClueHuntConfig.class);
	}


}

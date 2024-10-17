package com.customcluehunt;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ExampleConfig config;

	//For sidebar panel
	@Inject
	private ClientToolbar clientToolbar;

	private NavigationButton navButton;
	private PluginPanel panel;


	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");

		//Sidebar panel
		panel = new PluginPanel();
		panel.add(new JLabel("Hello, this is the sidebar content!"));
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "com.customcluehunt/icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Sidebar Menu")
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");

		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Im standing at:" + localPoint.getX() + " " + localPoint.getY() , null);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getLocalPlayer() != null)
		{
			WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
			int x = playerLocation.getX();
			int y = playerLocation.getY();
			int plane = playerLocation.getPlane();

			// Log or use the player's position
			System.out.println("Player position: X=" + x + ", Y=" + y + ", Plane=" + plane);
		}
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}


}

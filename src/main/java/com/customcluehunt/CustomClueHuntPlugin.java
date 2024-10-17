package com.customcluehunt;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(name = "Custom Clue Hunt")

public class CustomClueHuntPlugin extends Plugin {
    // Default inject
    @Inject private Client client;

    // Config inject
    @Inject private CustomClueHuntConfig config;

    // Sidebar panel inject + setup
    @Inject private ClientToolbar clientToolbar;
    private CustomClueHuntPanel panel;
    private NavigationButton navButton;

    // For attacked NPC
    private final Set<NPC> attackedNpcs = new HashSet<>();

    public void writeMessageToPanel(String message) { panel.setMessage(message); }
    public void writeWhatsEquiped(String message) { panel.getEquiped(message); }

    @Override
    protected void startUp() throws Exception {
        // Add sidebar panel
        panel = new CustomClueHuntPanel();
        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/panel_icon.png");
        navButton = NavigationButton.builder().tooltip("Custom Clue Hunt").icon(icon).priority(5).panel(panel).build();
        clientToolbar.addNavigation(navButton);

        log.info("Custom Clue Hunt plugin started!");
    }

    @Override
    protected void shutDown() throws Exception {
        // Close sidebar panel
        clientToolbar.removeNavigation(navButton);
        log.info("Custom Clue Hunt plugin stopped!");
    }

    // Checking if you are in combat with NPC
    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        if (event.getSource() == client.getLocalPlayer() && event.getTarget() instanceof NPC) {
            NPC npc = (NPC)event.getTarget();
            attackedNpcs.add(npc);
        }
    }

    // Check for NPC despawning
    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        NPC npc = event.getNpc();

        if (attackedNpcs.remove(npc) && npc.getHealthRatio() == 0) {
            // Check if player killed NPC and what NPC it was
            log.info("Player killed NPC: " + npc.getName());
        }
    }

    /*
    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
            if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
            {
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
    "Example says " + config.greeting(), null);
            }
    }
*/

    @Subscribe
    public void onGameTick(GameTick event) {

        //======================================================================================
        // Get player position
        int x = 0;
        int y = 0;
        int plane = 0;
        if (client.getLocalPlayer() != null) {
            WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
            x = playerLocation.getX();
            y = playerLocation.getY();
            plane = playerLocation.getPlane();

            // Log or use the player's position
            System.out.println("Player position: X=" + x + ", Y=" + y + ", Plane=" + plane);
        }

        //=======================================================================================

        //=======================================================================================
        // Get player animation
        if (client.getLocalPlayer().getAnimation() == AnimationID.DIG) { writeMessageToPanel("Digging at X:" + x + " Y: " + y); }
        //=======================================================================================

//        String allEquipment = "";

        //======================================================================================
        // Get player equipment
        ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipment == null) { return; }
/*
        for (Item item : equipment.getItems()) {
            int itemId = item.getId();
            int quantity = item.getQuantity();
            allEquipment = "Player has equipped item with ID:\n" + itemId + "\n and quantity: \n" + quantity;
        }

        Item headItem = equipment.getItem(EquipmentInventorySlot.HEAD.getSlotIdx());
        if (headItem == null) {
            //log.info("No item equipped in head slot.");
        } else {
            allEquipment += "\nItem equipped in head slot: " + headItem.getId();
        }

        Item weaponItem = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
        if (weaponItem == null) {
            //log.info("No item equipped in weapon slot.");
        } else {
            allEquipment += "\nItem equipped in weapon slot: " + weaponItem.getId();
        }
*/
        StringBuilder allEquipment = new StringBuilder();

        for (EquipmentInventorySlot slot : EquipmentInventorySlot.values()) {
            Item item = equipment.getItem(slot.getSlotIdx());
            if (item != null) {
                allEquipment.append("\nItem equipped in ").append(slot.name().toLowerCase()).append(" slot: ").append(item.getId());
            }
        }

        // Convert StringBuilder to String if needed
        String allEquipmentStr = allEquipment.toString();
        writeWhatsEquiped(allEquipmentStr);
        //======================================================================================
    }

    @Provides
    CustomClueHuntConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CustomClueHuntConfig.class);
    }
}

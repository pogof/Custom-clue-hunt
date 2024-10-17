package com.customcluehunt;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;

public class CustomClueHuntPanel extends PluginPanel
{
    private final JLabel label = new JLabel();
    private final JTextArea equiped = new JTextArea();


    public CustomClueHuntPanel()
    {
        super();
        add(new JLabel("Custom Clue Hunt"));

        add(label);
        add(equiped);
    }

    public void setMessage(String message)
    {
        label.setText(message);
    }

    public void getEquiped(String message){
        equiped.setText(message);
    }
}
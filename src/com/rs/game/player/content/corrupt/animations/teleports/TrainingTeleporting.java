package com.rs.game.player.content.corrupt.animations.teleports;

import com.rs.game.player.Player;

public class TrainingTeleporting {

    public static int inter = 3009;

    public static void sendInterface(Player player){
        int tab = player.getPlayerVars().getInterface3009Tab();
        int boss = player.getPlayerVars().getInterface3009Boss();

        player.getInterfaceManager().sendInterface(inter);

        for(int i = 31; i < 76; i+=3){
            player.getPackets().sendHideIComponent(inter, i, true);
        }

        int l;
        switch(tab){

            case 1: //low
                l = 31;
                for(TrainingTeleports tele : player.getTeleports().lowTraining()){
                    for(int k = l; k < l+3; k++){
                        player.getPackets().sendHideIComponent(inter, k, false);
                    }
                    player.getPackets().sendIComponentSprite(inter, l+1, 22159);
                    player.getPackets().sendIComponentText(inter, l+2, tele.getName());
                    l+=3;
                }
                TrainingTeleports teleport = player.getTeleports().lowTraining()[boss];
                player.getPackets().sendIComponentText(inter, 78, teleport.getName());
                break;

            case 2:
                l = 31;
                for(TrainingTeleports tele : player.getTeleports().highTraining()){
                    for(int k = l; k < l+3; k++){
                        player.getPackets().sendHideIComponent(inter, k, false);
                    }
                    player.getPackets().sendIComponentSprite(inter, l+1, 22159);
                    player.getPackets().sendIComponentText(inter, l+2, tele.getName());
                    l+=3;
                }
                TrainingTeleports teleport1 = player.getTeleports().highTraining()[boss];
                player.getPackets().sendIComponentText(inter, 78, teleport1.getName());
                break;

            case 3:
                l = 31;
                for(TrainingTeleports tele : player.getTeleports().slayerTeleports()){
                    for(int k = l; k < l+3; k++){
                        player.getPackets().sendHideIComponent(inter, k, false);
                    }
                    player.getPackets().sendIComponentSprite(inter, l+1, 22159);
                    player.getPackets().sendIComponentText(inter, l+2, tele.getName());
                    l+=3;
                }
                TrainingTeleports teleport2 = player.getTeleports().slayerTeleports()[boss];
                player.getPackets().sendIComponentText(inter, 78, teleport2.getName());
                break;
        }

    }

    public static void handleButtons(Player player, int componentId){

        switch(componentId){
            case 21:
                player.getPlayerVars().setInterface3009Tab(1);
                player.getPlayerVars().setInterface3009Boss(1);
                sendInterface(player);
                break;
            case 22:
                player.getPlayerVars().setInterface3009Tab(2);
                player.getPlayerVars().setInterface3009Boss(1);
                sendInterface(player);
                break;
            case 23:
                player.getPlayerVars().setInterface3009Tab(3);
                player.getPlayerVars().setInterface3009Boss(1);
                sendInterface(player);
                break;
            case 24:
                if(player.getPlayerVars().getInterface3009Boss() != -1 && player.getPlayerVars().getInterface3009Tab() != -1)
                    player.getTeleports().teleportToTraining();
                break;
        }

        if(componentId >= 31 && componentId < 75){
            int id = (componentId - 31) / 3;
            player.getPlayerVars().setInterface3009Boss(id);
            sendInterface(player);
        }
    }

}

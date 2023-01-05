package com.rs.game.player.content.corrupt.inters.impl;

import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.inters.RSInterface;

public class BlockExplorer extends RSInterface {

    public BlockExplorer(Player player, int INTER) {
        super(player, INTER);
        properties.put("inter_tab", 1);
    }

    @Override
    public void open() {
       // MasterChain chain = MasterChain.singleton;
        //Hide blocks
        for(int i = 42; i < 132; i+=6){
            sendHideComponent(i, true);
        }

        //Block Info
//        sendTextComponent(20, "Block : "+chain.getLatestBlock().getChainLink());
//        sendTextComponent(21, "Transactions : "+chain.getLatestBlock().getTransactions().size());
//        sendTextComponent(22, "Reward : "+ChainHelper.formattedDoubleString(chain.getLatestBlock().getBalance()));
//
//        //Block Explorer
//        int index = (int) properties.get("inter_tab");
//        int iterate = 0;
//        for(int j = 42; j < 132; j+=6){
//            int block = ((j-42) - (iterate * 6) + iterate) + ((index - 1) * 15);
//            System.out.println("Index : "+block);
//            if(block >= chain.getChain().size())
//                break;
//            if(chain.getChain().get(block) != null){
//                sendHideComponent(j, false);
//                sendTextComponent(j + 1, ""+chain.getChain().get(block).getChainLink());
//                sendTextComponent(j + 2, ""+chain.getChain().get(block).getTransactions().size());
//                sendTextComponent(j + 3, ""+ ChainHelper.formattedDoubleString(chain.getChain().get(block).getBalance()));
//            }
//            iterate++;
//        }
//
//        //Player Info
//        sendTextComponent(133, "Open Futures : "+player.getWallet().amountOfFutures());
//        sendTextComponent(135, "Contracts : "+chain.getContracts().size());
//        sendTextComponent(137, "Transactions : "+chain.totalTransactions());
//        sendTextComponent(139, "Wallets : "+chain.totalWallets());

        //Hide buttons for now
        sendHideComponent(141, true);
        sendHideComponent(146, true);
        sendHideComponent(151, true);
        sendHideComponent(156, true);

        //Page
        //sendTextComponent(165, ""+index);

    }

    @Override
    public void onClose() {

    }

    @Override
    public void handleButtons(int button, int packet, int itemId, int slotId) {
        switch(button){
            case 134 -> {
                player.getInterfaceManager().clearRSInterface();
                player.getInterfaceManager().sendInterface(new OpenFutures(player, 3022));
            }
            case 136 -> {
                player.getInterfaceManager().clearRSInterface();
                player.getInterfaceManager().sendInterface(new ContractExplorer(player, 3023));
            }

            //Pages
            case 163 -> {
                int index = (int) properties.get("inter_tab");
                if(index > 1){
                    properties.remove("inter_tab");
                    properties.put("inter_tab", index - 1);
                    refresh();
                }
            }
            case 164 -> {
                int index = (int) properties.get("inter_tab");
                properties.remove("inter_tab");
                properties.put("inter_tab", index + 1);
                refresh();
            }
        }
    }
}

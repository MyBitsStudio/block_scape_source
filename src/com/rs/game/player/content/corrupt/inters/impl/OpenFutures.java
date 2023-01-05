package com.rs.game.player.content.corrupt.inters.impl;

import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.inters.RSInterface;

import java.util.ArrayList;
import java.util.List;

public class OpenFutures extends RSInterface {

    public OpenFutures(Player player, int INTER) {
        super(player, INTER);
        properties.put("future", 0);
    }

    @Override
    public void open() {
        //MasterChain chain = MasterChain.singleton;

        //Hide everything

//        for(int i = 22; i < 58; i+=3){
//            sendHideComponent(i, true);
//        }
//        for(int i = 64; i < 78; i+=2){
//            sendHideComponent(i, true);
//        }
//
//        int size = player.getWallet().getFutures().size();
//        if(size >= 1){
//            TransactionFuture future = player.getWallet().getFuture((int) properties.get("future"));
//
//            String type = "";
//            List<String> transactions = new ArrayList<>();
//            if(future instanceof DuelArenaFuture){
//                type = "Duel Arena";
//                transactions.add("Type : Interaction");
//                transactions.add("Type : Interaction");
//            }
//
//            int sip = 0;
//            for(int i = 22; i < 58; i+=3){
//                if(sip >= size)
//                    break;
//                sendHideComponent(i, false);
//                sendTextComponent(i + 1, "Type : "+type);
//                sendTextComponent(i + 2, "hash : "+future.getSerial());
//
//                sip++;
//            }
//
//            sendTextComponent(59, "Type : "+type);
//            sendTextComponent(60, "Hash : "+future.getSerial());
//            sendTextComponent(61, "Ends : "+ ChainHelper.timeStamp(chain.getTransactionFutures(player.getWallet().getPublicAddress()).get(future)));
//
//            for(int i = 64; i < 78; i+=2){
//                int index = (i - 64) / 2;
//                sendHideComponent(i, false);
//                if(index >= transactions.size())
//                    break;
//                sendTextComponent(i + 1, transactions.get(index));
//            }
//
//        } else {
//
//            sendTextComponent(59, "Type : ");
//            sendTextComponent(60, "Hash : ");
//            sendTextComponent(61, "Ends : ");
//
//        }


    }

    @Override
    public void onClose() {
        player.getInterfaceManager().sendInterface(new BlockExplorer(player, 3021));
    }

    @Override
    public void handleButtons(int button, int packet, int itemId, int slotId) {

    }
}

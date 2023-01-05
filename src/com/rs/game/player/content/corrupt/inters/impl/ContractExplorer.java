package com.rs.game.player.content.corrupt.inters.impl;

import com.rs.game.player.Player;
import com.rs.game.player.content.contracts.Contract;
import com.rs.game.player.content.corrupt.inters.RSInterface;

public class ContractExplorer extends RSInterface {

    public ContractExplorer(Player player, int INTER) {
        super(player, INTER);
        properties.put("contract", 0);
    }

    @Override
    public void open() {
        //Hide blocks
        for(int i = 19; i < 87; i+=4){
            sendHideComponent(i, true);
        }
        for(int i = 95; i < 115; i+=2){
            sendHideComponent(i, true);
        }

//        Contracts contract = chain.getContracts().get((Integer) properties.get("contract"));
//        //Contract info
//        sendTextComponent(88, "Contract - : "+contract.getTokenName());
//        sendTextComponent(89, "Circulation : "+ChainHelper.formattedDoubleString(contract.inCirculation()));
//        sendTextComponent(90, "Transactions : "+contract.transactionSize());
//        sendTextComponent(91, "Gas Collected : "+ChainHelper.formattedDoubleString(contract.getGasProvider().getBalance(contract.getAddress())));
//
//        //Contracts
//        int iterate = 0;
//        for(int j = 19; j < 87; j+=4){
//            if(iterate > chain.getContracts().size() - 1)
//                break;
//            Contracts send = chain.getContracts().get(iterate);
//            sendHideComponent(j, false);
//            sendTextComponent(j+1, "Contract - : "+send.getTokenName());
//            sendTextComponent(j+2, "Address : "+send.getAddress());
//            sendTextComponent(j+3, "Balance : "+ChainHelper.formattedDoubleString(send.getGenesis().getBalance(send.getAddress())));
//            iterate++;
//        }
//
//        //Transactions
//        int start = contract.transactionSize() - 10;
//        if(start < 0)
//            start = 0;
//        for(int i = 95; i < 115; i++){
//            if(start >= contract.transactionSize())
//                break;
//            sendHideComponent(i, false);
//            sendTextComponent(i, ((Transaction) contract.getTransactions().values().toArray()[start]).getTransactionId());
//            start++;
//        }


    }

    @Override
    public void onClose() {
        player.getInterfaceManager().sendInterface(new BlockExplorer(player, 3021));
    }

    @Override
    public void handleButtons(int button, int packet, int itemId, int slotId) {
        if(button >= 19 && button <= 83){
            int index = (button - 19) / 4;
            properties.put("contract", index);
            refresh();
        }

//        if(button >= 95 && button <= 113){
//            int index = button - 95 / 2;
//            int start = chain.getContracts().get((Integer) properties.get("contract")).transactionSize() - 10;
//            if(start < 0)
//                start = 0;
//            start += index;
//            if(start >= chain.getContracts().get((Integer) properties.get("contract")).transactionSize())
//                return;
//            Transaction transaction = (Transaction) chain.getContracts().get((Integer) properties.get("contract")).getTransactions().values().toArray()[start];
//            System.out.println(transaction.getTransactionId());
//        }
    }
}

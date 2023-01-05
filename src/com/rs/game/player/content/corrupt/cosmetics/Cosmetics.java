package com.rs.game.player.content.corrupt.cosmetics;

import java.util.ArrayList;
import java.util.Arrays;

public class Cosmetics {

    public static final class Cosmetic {
        private final String name;
        private final int itemId, slot;

        Cosmetic(String name, int itemId, int slot) {
            this.name = name;
            this.itemId = itemId;
            this.slot = slot;
        }

        public String getName() {
            return name;
        }

        public int getItemId() {
            return itemId;
        }

        public int getSlot() {
            return slot;
        }
    }

    public enum CosmeticSets{
       HIDE_ALL("Hide All", 0, CosmeticPiece.HIDE_BOOTS, CosmeticPiece.HIDE_EFFECTS, CosmeticPiece.HIDE_CAPE, CosmeticPiece.HIDE_HELMET,
               CosmeticPiece.HIDE_GLOVES, CosmeticPiece.HIDE_LEGS, CosmeticPiece.HIDE_NECKLACE, CosmeticPiece.HIDE_TORSO),

       CABARET_OUTFIT("Cabaret Outfit", 250, CosmeticPiece.CABARET_LEGS, CosmeticPiece.CABARET_GLOVES, CosmeticPiece.CABARET_HAT,
               CosmeticPiece.CABARET_JACKET, CosmeticPiece.CABARET_SHOES),

       COLOSSEUM_OUTFIT("Colosseum Outfit", 200, CosmeticPiece.COLOSSEUM_HEAD, CosmeticPiece.COLOSSEUM_JACKET, CosmeticPiece.COLOSSEUM_SHOES,
               CosmeticPiece.COLOSSEUM_LEGS),

       FELINE_OUTFIT("Feline Outfit", 250, CosmeticPiece.FELINE_EARS, CosmeticPiece.FELINE_JACKET, CosmeticPiece.FELINE_LEGS,
               CosmeticPiece.FELINE_TAIL, CosmeticPiece.FELINE_SHOES),

       GOTHIC_OUTFIT("Gothic Outfit", 240, CosmeticPiece.GOTHIC_CAPE, CosmeticPiece.GOTHIC_SHOES, CosmeticPiece.GOTHIC_JACKET,
                CosmeticPiece.GOTHIC_LEGS),

       SWASHBUCKLER_OUTFIT("Swashbuckler Outfit", 250, CosmeticPiece.SWASHBUCKLER_CAPE, CosmeticPiece.SWASHBUCKLER_JACKET, CosmeticPiece.SWASHBUCKLER_LEGS,
                CosmeticPiece.SWASHBUCKLER_MASK, CosmeticPiece.SWASHBUCKLER_SHOES),


       ;

       private final String name;
       private final int price;
       private final CosmeticPiece[] cosmetics;

       CosmeticSets(String name, int price, CosmeticPiece... cosmetics){
           this.name = name;
           this.price = price;
           this.cosmetics = cosmetics;
       }

       public String getName() {
            return name;
        }

       public int getPrice() {
            return price;
        }

       public CosmeticPiece[] getCosmetics() {
            return cosmetics;
        }

    }

    public enum CosmeticPiece{
        HIDE_HELMET("Hide Helmet", 0,  new Cosmetic("Hide helmet", 27146, 0)),
        HIDE_CAPE("Hide Cape", 0,  new Cosmetic("Hide cape", 27147, 1)),
        HIDE_NECKLACE("Hide Necklace", 0,  new Cosmetic("Hide necklace", 30038, 2)),
        HIDE_TORSO("Hide Torso", 0,  new Cosmetic("Hide torso", 30039, 4)),
        HIDE_LEGS("Hide Legs", 0,  new Cosmetic("Hide legs", 30040, 7)),
        HIDE_GLOVES("Hide Gloves", 0,  new Cosmetic("Hide gloves", 30042, 9)),
        HIDE_BOOTS("Hide Boots", 0,  new Cosmetic("Hide boots", 30041, 10)),
        HIDE_EFFECTS("Hide Effects", 0,  new Cosmetic("Hide effects", 35865, 14)),

        CABARET_HAT("Cabaret Hat", 50, new Cosmetic("Cabaret hat", 24583, 0)),
        CABARET_JACKET("Cabaret Jacket", 50, new Cosmetic("Cabaret jacket", 24585, 4)),
        CABARET_LEGS("Cabaret Legs", 50, new Cosmetic("Cabaret legs", 24587, 7)),
        CABARET_GLOVES("Cabaret Gloves", 50, new Cosmetic("Cabaret gloves", 24591, 9)),
        CABARET_SHOES("Cabaret Shoes", 50, new Cosmetic("Cabaret shoes", 24589, 10)),

        COLOSSEUM_HEAD("Colosseum Head", 50,new Cosmetic("Colosseum head", 24595, 0)),
        COLOSSEUM_SHOES("Colosseum Shoes", 50,new Cosmetic("Colosseum shoes", 24601, 10)),
        COLOSSEUM_JACKET("Colosseum Jacket", 50,new Cosmetic("Colosseum jacket", 24597, 4)),
        COLOSSEUM_LEGS("Colosseum Legs", 50,new Cosmetic("Colosseum legs", 24599, 7)),

        FELINE_EARS("Feline Ears", 50, new Cosmetic("Feline ears", 24605, 0)),
        FELINE_TAIL("Feline Tail", 50, new Cosmetic("Feline tail", 24613, 1)),
        FELINE_JACKET("Feline Jacket", 50, new Cosmetic("Feline jacket", 24607, 4)),
        FELINE_LEGS("Feline Legs", 50, new Cosmetic("Feline legs", 24609, 7)),
        FELINE_SHOES("Feline Shoes", 50, new Cosmetic("Feline shoes", 24611, 10)),

        GOTHIC_CAPE("Gothic Cape", 60, new Cosmetic("Gothic cape", 24623, 1)),
        GOTHIC_SHOES("Gothic Shoes", 60, new Cosmetic("Gothic shoes", 24621, 10)),
        GOTHIC_JACKET("Gothic Jacket", 60, new Cosmetic("Gothic jacket", 24617, 4)),
        GOTHIC_LEGS("Gothic Legs", 60, new Cosmetic("Gothic legs", 24619, 7)),

        SWASHBUCKLER_MASK("Swashbuckler Mask", 50, new Cosmetic("Swashbuckler mask", 24627, 0)),
        SWASHBUCKLER_CAPE("Swashbuckler Cape", 50, new Cosmetic("Swashbuckler cape", 24635, 1)),
        SWASHBUCKLER_JACKET("Swashbuckler Jacket", 50, new Cosmetic("Swashbuckler jacket", 24629, 4)),
        SWASHBUCKLER_LEGS("Swashbuckler Legs", 50, new Cosmetic("Swashbuckler legs", 24631, 7)),
        SWASHBUCKLER_SHOES("Swashbuckler Shoes", 50, new Cosmetic("Swashbuckler shoes", 24633, 10)),

        ASSASSIN_HOOD("Assassin Hood", 50, new Cosmetic("Swashbuckler mask", 24627, 0)),
        ASSASSIN_CAPE("Assassin Cape", 50, new Cosmetic("Swashbuckler cape", 24635, 1)),
        ASSASSIN_SCIMITAR("Assassin Scimitar", 50, new Cosmetic("Swashbuckler jacket", 24629, 4)),
        ASSASSIN_JACKET("Assassin Jacket", 50, new Cosmetic("Swashbuckler legs", 24631, 7)),
        ASSASSIN_OFF_HAND("Assassin Off-Hand", 50, new Cosmetic("Swashbuckler shoes", 24633, 10)),
        ASSASSIN_LEGS("Assassin Legs", 50, new Cosmetic("Swashbuckler shoes", 24633, 10)),
        ASSASSIN_GLOVES("Assassin Gloves", 50, new Cosmetic("Swashbuckler shoes", 24633, 10)),
        ASSASSIN_SHOES("Assassin Shoes", 50, new Cosmetic("Swashbuckler shoes", 24633, 10)),



        ;

        private final String name;
        private final int price;
        private final Cosmetic cosmetic;

        CosmeticPiece(String name, int price, Cosmetic cosmetic){
            this.name = name;
            this.price = price;
            this.cosmetic = cosmetic;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public Cosmetic getCosmetic() {
            return cosmetic;
        }

    }

    public static ArrayList<CosmeticPiece> headPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 0)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> backPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 1)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> neckPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 2)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> weaponPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 3)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> chestPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 4)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> offHandPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 5)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> legsPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 7)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> handsPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 9)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> bootsPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 10)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> effectPieces(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        for(CosmeticPiece pieces : CosmeticPiece.values())
            if(pieces.getCosmetic().getSlot() == 14)
                piece.add(pieces);

        return piece;
    }

    public static ArrayList<CosmeticPiece> restricted(){
        ArrayList<CosmeticPiece> piece = new ArrayList<>();
        //add pieces here

        return piece;
    }

    public static ArrayList<CosmeticSets> sets(){
        return new ArrayList<>(Arrays.asList(CosmeticSets.values()));
    }

}

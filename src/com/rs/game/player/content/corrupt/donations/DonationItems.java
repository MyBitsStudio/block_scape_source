package com.rs.game.player.content.corrupt.donations;

import com.rs.game.item.Item;

public enum DonationItems {

    //VIP 1
    PROTEAN_MINI(1, "Protean Mini", 1, 25, new Item(36813)),
    PROTEAN_SMALL(2, "Protean Small", 1, 60, new Item(34023)),
    DRAGONBONE_KIT(3, "Dragonbone Upgrade Kit", 1, 50, new Item(24352)),
    WHIP_VINE(4, "Whip Vine", 1, 50, new Item(21369)),
    KEEPSAKE_KEY(5, "Keepsake Key", 1, 25, new Item(25430)),
    GAS_MASK(6, "Gas Mask", 1, 50, new Item(1506)),
    FROG_MASK(7, "Frog Mask", 1, 20, new Item(6188)),
    TRIBAL_MASK(8, "Tribal Mask", 1, 40, new Item(6335)),
    CAMEL_MASK(9, "Camel Mask", 1, 30, new Item(7003)),
    INFERNO_ODZE(10, "Inferno Odze", 1, 25, new Item(13661)),
    EXTRA_BANK(11, "Extra Bank", 1, 100, new Item(13663)),
    SOF_SPIN(12, "SoF Spin", 1, 5, new Item(24154)),
    PORTABLES_PACK(13, "Portable Pack", 1, 25, new Item(34026)),


    //VIP 2
    PROTEAN_MEDIUM(51, "Protean Medium", 2, 90, new Item(34024)),
    PROTEAN_LARGE(52, "Protean Large", 2, 115, new Item(34025)),
    TIARA_ADDON(53, "Tiara Add-On", 2, 60, new Item(34921)),
    BANDANA_ADDON(54, "Artisan Add-On", 2, 60, new Item(32277)),
    BOTANIST_ADDON(55, "Botanist Add-On", 2, 60, new Item(34919)),
    DIVINERS_ADDON(56, "Diviner's Add-On", 2, 60, new Item(32275)),
    SHAMAN_ADDON(57, "Shaman's Add-On", 2, 60, new Item(32274)),
    CHEF_ADDON(58, "Chef's Add-On", 2, 60, new Item(34920)),
    BLACKSMITH_ADDON(59, "Blacksmith's Add-On", 2, 60, new Item(32276)),
    FARMER_ADDON(60, "Farmer's Add-On", 2, 60, new Item(34922)),
    CRYSTAL_SEED(61, "Crystal Seed", 2, 25, new Item(32625)),

    //VIP 3
    PROTEAN_CHEST(101, "Protean Chest", 3, 150, new Item(40189)),
    CHRISTMAS_NOX_DYE(102, "Nox Christmas Dye", 3, 250, new Item(34838)),
    BARROWS_DYE(103, "Barrow Dye", 3, 100, new Item(33294)),
    THIRD_AGE_DYE(104, "Third-Age Dye", 3, 150, new Item(33298)),
    BLOOD_DYE(105, "Blood Dye", 3, 125, new Item(36274)),
    SHADOW_DYE(106, "Shadow Dye", 3, 125, new Item(33296)),
    CHRISTMAS_CRACKER(107, "Christmas Cracker", 3, 250, new Item(962)),
    BUNNY_EARS(108, "Bunny Ears", 3, 100, new Item(1037)),
    BASKET_EGGS(109, "Egg Baskets", 3, 125, new Item(4565)),
    CHRISTMAS_PRESENT(110, "Christmas Present 1", 3, 50, new Item(33610)),
    HALLOWEEN_PACK_2(111, "Halloween Pack 2", 3, 30, new Item(34354)),


    //VIP STORE
    POTION_PACK(150, "Potion Pack", 4, 100, new Item(28566)),
    FOOD_SACK(151, "Food Sack", 4, 100, new Item(29441)),
    LARGE_FOOD_SACK(152, "Large Food Sack", 4, 200, new Item(29442)),
    SUPPLY_CACHE(153, "Supply Cache", 4, 100, new Item(35886)),
    SOF_VIP(154, "SoF Spin", 4, 5, new Item(24154)),

    ;

    private int id, rank, price;
    private String name;
    private Item item;

    DonationItems(int id, String name, int rank, int price, Item items){
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.price = price;
        this.item = items;
    }

    public int getId(){
        return this.id;
    }

    public int getRank(){
        return this.rank;
    }

    public int getPrice(){
        return this.price;
    }

    public String getName(){
        return this.name;
    }

    public Item getItem(){
        return this.item;
    }

    public static DonationItems getDonationItem(Item item){
        for(DonationItems itemz : DonationItems.values())
            if(itemz.getItem() == item)
                return itemz;
        return null;
    }

    public static DonationItems[] level1(){
        return new DonationItems[]{
            PROTEAN_MINI, PROTEAN_SMALL,
        };
    }

    public static DonationItems[] level2(){
        return new DonationItems[]{
                PROTEAN_MEDIUM
        };
    }

    public static DonationItems[] level3(){
        return new DonationItems[]{
                PROTEAN_LARGE
        };
    }

    public static DonationItems[] level4(){ // VIP Points Store
        return new DonationItems[]{
                PROTEAN_CHEST
        };
    }
}

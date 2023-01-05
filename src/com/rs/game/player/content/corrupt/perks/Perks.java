package com.rs.game.player.content.corrupt.perks;

public enum Perks {

    BANK(0, "Bank Anywhere", "Can open your bank anywhere", 100),
    STAMINA_BOOST(1, "Stamina Boost", "Boost your run energy", 30),
    GREEN_THUMB(2, "Green Thumb", "Increased Farming output", 120),
    BIRD_MAN(3, "Bird Man", "Increased Bird Nests", 50),
    UNBREAKABLE(4, "Unbreakable Forge", "Unlimited Forging Charges", 120),
    SLEIGHT(5, "Slieght of Hand", "Increased Thieving", 100),
    FAMILIAR(6, "Familiar Expert", "Increased Familiar Health", 50),
    CHARGE(7, "Charge Befriender", "Avoid Armor Degrading", 120),
    CHARM(8, "Charm Collector", "Auto Pickup Charms to Bank", 40),
    HERBIVORE(9, "Herbivore", "Increased Herbalore", 100),
    FISHERMAN(10, "Master Fisherman", "Increased Fishing", 70),
    CRAFTSMAN(11, "Delicate Craftsman", "Increased Crafting", 80),
    COLLECTOR(12, "Coin Collector", "Extra coins on pickup", 50),
    BETRAYER(13, "Prayer Betrayer", "Increased Prayer", 80),
    AVA(14, "Avas Secret", "Get Ava", 40),
    KEY_EXPERT(15, "Key Expert", "Double Rewards on Crystal/Raptor", 100),
    DRAGON_TRAINER(16, "Dragon Expert", "Perm Anti-Fire", 60),
    GWD_SPEC(17, "GWD Specialist", "-20 Kill Count", 50),
    DUNGEON(18, "Dungeon Master", "Increased Dungeonerring", 100),
    CHANTER(19, "Pet'chanter", "Increased Pet Drop", 100),
    PERSLAY(20, "Per'slay'sion", "Increased Slayer", 70),
    OVERCLOCKED(21, "OverClocked", "Increased Auras", 60),
    ELFFIEND(22, "Elf Fiend", "Early Priff & Teleport", 30),
    CHEF(23, "Master Chef", "Increased Cooking", 30),
    DIVINER(24, "Master Diviner", "Increased Divination", 30),
    QUARRY(25, "Quarrymaster", "Increased Mining", 50),
    MINI_GAMER(26, "Mini Gamer", "Increased Mini Games Rewards", 30),
    FLEDGER(23, "Master Fledger", "Increased Fletching", 40),
    PYROMANIAC(23, "Pryomaniac", "Increased Bonfire", 10),
    HUNTSMAN(23, "Huntsman", "Increased Hunting", 25),
    PORTS(23, "Ports Master", "Increased Ports", 30),
    INVESTIGATOR(23, "Investigator", "Clue Hunter", 50),
    DIVINE(23, "Divine Doubler", "Double Divination", 30),
    IMBUED(23, "Imbued Focus", "Increased Siphon", 20),
    ALCHEMIC(23, "Alchemic Smithing", "Increased Smithing", 40),
    PETLOOT(23, "Pet Loot", "Increased Pet Loot", 60),
    COMPANION(23, "D'companion", "Dungeon Companion", 40),
    STAGGER(23, "Stagger", "Stuns Target", 40),
    ANNIHILATOR(23, "Annihilator", "Annihilate Target", 50),
    DOMINATOR(23, "Dominator", "Steal Life", 40),
    TARRASQUE(23, "Heart of Tarrasque", "Regenerate Health", 50),
    BREATH(23, "Corruption Blast", "Breath of Corruption", 40),
    HAZELMERE(23, "Hazelmere Luck", "", 100),//finish here
    PRAESUL(23, "Praesul", "", 100)

    ;


    private final String name, description;
    private final int cost, id;

    Perks(int id, String name, String description, int cost){
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public String getName(){return name;}
    public String getDescription(){ return description; }
    public int getCost(){ return cost; }
    public int getId(){ return id; }

    public Perks forId(int id){
        for(Perks perk : Perks.values())
            if(perk.getId() == id)
                return perk;
        return null;
    }
}

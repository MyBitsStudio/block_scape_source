package com.rs.game.player.saving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.game.player.AuraManager;
import com.rs.game.player.Bank;
import com.rs.game.player.GlobalPlayerUpdater;
import com.rs.game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class PlayerSaving {

    private final Gson gson = new GsonBuilder()
            .setLenient()
            .setPrettyPrinting()
            .create();
    public static final String LOCATION = "./data/saves/players/";

    public PlayerSaving(){

    }

    public Player load(String username, String password){
        Player player = null;
        if(new File(LOCATION + username+".json").exists()){
            player = gson.fromJson(username, Player.class);
            if(player.getPassword().equals(password)){
                return player;
            }
        }
        return player;
    }

    public void save(@NotNull Player player){
        System.out.println("Saving player: " + player.getUsername());


        try (FileWriter file = new FileWriter(LOCATION+player.getUsername()+".json")) {
            //file.write(data);
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    static class SaveDetails {
//        boolean isMainhand;
//        int lastSkillEventViewed;
//        int skillEventPoints;
//        int SkillEventPosition;
//        boolean togglePouchMessages;
//        int prestigePoints;
//        boolean Hween;
//        boolean saloon;
//        boolean DrunkenSailor;
//        boolean agrithNaNa;
//        boolean allowChatEffects;
//        GlobalPlayerUpdater globalPlayerUpdater;
//        AuraManager auraManager;
//        Bank bank;
//        int barrowsKillCount;
//        int barrowsRunsDone;
//        int damagepoints;
//        int TentMulti;
//        boolean hasOpenedTentShop;
//        boolean hasOpenedTentShop2;
//        int[] FastestTime;
//        int highestKillStreak;
//        int killStreak;
//        int killStreakPoints;
//        int totalkillStreakPoints;
//        int highestKill;
//        boolean iplocked;
//         charges
//        clanStatus
//                combatDefinitions
//        completedFightCaves
//                completedFightKiln
//        completedRfd
//                completionistCapeCustomized
//        controlerManager
//                creationDate
//        crucibleHighScore
//                culinaromancer
//        coal
//                currentFriendChatOwner
//        connectedClanChannel
//                dessourt
//        dominionTower
//                donator
//        donatorTill
//                emotesManager
//        treasureTrails
//                equipment
//        extremeDonator
//                extremeDonatorTill
//        fairyRingCombination
//                filterGame
//        fireImmune
//                superAntiFire
//        flamBeed
//                forceNextMapLoadRefresh
//        friendChatSetup
//                friendsIgnores
//        hiddenBrother
//                hideWorldAnnouncements
//        inAnimationRoom
//                inventory
//        isInDefenderRoom
//                jailed
//        karamel
//                khalphiteLairEntranceSetted
//        khalphiteLairSetted
//                killCount
//        deathCount
//                killedBarrowBrothers
//        killedBork
//                killedQueenBlackDragon
//        lastBonfire
//                lastLoggedIn
//        maxedCapeCustomized
//                money
//        mouseButtons
//                musicsManager
//        overloadDelay
//                ownedObjectsManagerKeys
//        password
//                permBanned
//        permMuted
//                pestControlGames
//        pestPoints
//                petManager
//        pkPoints
//                pkPointReward
//        pointsHad
//                poisonImmune
//        defenderPassive
//                pouch
//        pouches
//                prayer
//        prayerRenewalDelay
//                privateChatSetup
//        publicStatus
//                questManager
//        registeredMac
//                reportOption
//        rights
//                runEnergy
//        runeSpanPoints
//                skills
//        skullDelay
//                skullId
//        slayerPoints
//                slayerPoints2
//        specRestoreTimer
//                spins
//        infusedPouches
//                summoningLeftClickOption
//        talkedtoCook
//                tasksCompleted
//        talkedWithVannaka
//                talkedWithMarv
//        taskStreak
//                cHandler
//        temporaryMovementType
//                tradeStatus
//        updateMovementType
//                usedMacs
//        vecnaTimer
//                votePoints
//        HweenPoints
//                TuskenPoints
//        ElitePoints
//                StarfirePoints
//        wonFightPits
//                xpLocked
//        yellColor
//                yellDisabled
//        yellOff
//                muted
//        banned
//                xmasTitle1
//        xmasTitle2
//                xmasTitle3
//        xmasTitle4
//                lastVoteClaim
//        totalpkPoints
//                bountyHunter
//        dropRate
//                lendMessage
//        warriorPoints
//                thievingDelay
//        veteran
//                intermediate
//        easy
//                ironman
//        hcironman
//                expert
//        moneySpent
//                farmingManager
//        geManager
//                oresMined
//        barsSmelt
//                logsChopped
//        logsBurned
//                lapsRan
//        bonesOffered
//                potionsMade
//        timesStolen
//                itemsMade
//        itemsFletched
//                creaturesCaught
//        fishCaught
//                foodCooked
//        produceGathered
//                pouchesMade
//        memoriesCollected
//                runesMade
//        max
//                comp
//        compT
//                toolBelt
//        toolBeltNew
//                titles
//        combinedCloaks
//                guthixTitle
//        boxesOpened
//                chestsOpened
//        dungTokens
//                dungKills
//        inDungeoneering
//                StarfireBoss1
//        StarfireBoss2
//                zeals
//        toleranceTimer
//                setLootBeam
//        lootBeam
//                hasHouse
//        inRing
//                house
//        coOpSlayer
//                hasInvited
//        hasHost
//                hasGroup
//        hasOngoingInvite
//                slayerPartner
//        slayerHost
//                slayerInvite
//        ReaperPoints
//                totalkills
//        totalcontract
//                loyaltyPoints
//        times
//                loyaltybox
//        halloweenEmotes
//                christmasEmotes
//        easterEmotes
//                thanksGiving
//        votes
//                legendaryDonator
//        supremeDonator
//                ultimateDonator
//        sponsorDonator
//                youtube
//        dicer
//                forummanager
//        communitymanager
//                perkManager
//        ironOres
//                triviaPoints
//        hasAnswered
//                totalPlayTime
//        xpert_bonus
//                recordedPlayTime
//        foundShootingStar
//                lastStarSprite
//        starsFound
//                completedClues
//        squealOfFortune
//                prayerBook
//        acceptAid
//                profanityFilter
//        frozenKeyCharges
//                displayNameChange
//        donatedToWell
//                boons
//        divine
//                gathered
//        lastGatherLimit
//                lastCreationTime
//        createdToday
//                created
//        edgeville
//                market
//        dZone
//                prifddinas
//        air
//                mind
//        water
//                earth
//        fire
//                body
//        cosmic
//                chaos
//        nature
//                law
//        death
//                blood
//        support
//                receivedCracker
//        serenStonesMined
//                hefinLaps
//        hefinLapReward
//                motherlodeMaw
//        thievIorwerth
//                thievIthell
//        thievCadarn
//                thievAmlodd
//        thievTrahaearn
//                thievHefin
//        thievCrwys
//                thievMeilyr
//        caughtIorwerth
//                caughtIthell
//        caughtCadarn
//                caughtAmlodd
//        caughtTrahaearn
//                caughtHefin
//        caughtCrwys
//                caughtMeilyr
//        lastBork
//                beforeName
//        overrides
//                chroniclesOffered
//        taskPoints
//                consumeFish
//        defeatedVorago
//                isSiphoning
//        firstTime
//                animations
//        ports
//                xmas
//        spokenToVorago
//                mauledWeeksNM
//        mauledWeeksHM
//                elderTreeManager
//        banksManager
//                petLootManager
//        unlockedCostumesIds
//                showSearchOption
//        gearPresets
//                sendTentiDetails
//        questPoints
//                newQuestManager
//        slayerManager
//                instancedPVPPoints
//        instancedPVPKillStreak
//                dailyTaskManager
//        dayOfWeekManager
//                referralPoints
//        recievedReferralReward
//                timePlayedWeekly
//        voteCountWeekly
//                donationAmountWeekly
//        currentTimeOnline
//                resetedTimePlayedWeekly
//        sawMillProgress
//                hasWheatInHooper
//        lividFarmProduce
//                zombiesMinigamePoints
//        EasterStage
//                customEXP
//        hasClaimedspins
//                extraDiv
//        ArtisansWorkShopSupplies
//                dungManager
//        pvmpoints
//                rospoints
//        TRpoints
//                lastWellDonation
//        recentWellDonated
//                pyramidReward
//        ballakdmg
//                petLastPreventedDeath
//        petLastHealCd
//                isDefskill
//        timerPage
//                hasCompleted
//        hasLogedIn
//                hitpoints
//        mapSize
//                run
//        poison
//                freezeImmune
//        stunImmune
//                cantDoDefenceEmote
//        boundImmune
//                x
//        y
//                spawnedByEd
//        plane
//    }
}

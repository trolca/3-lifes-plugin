package me.trololo11.lifesplugin.utils.questUtils;

import me.trololo11.lifesplugin.utils.questTypes.TaskListenerType;

import java.util.ArrayList;

public class TaskListenerArrays {

    private final ArrayList<QuestClass> eatQuests;
    private final ArrayList<QuestClass> killQuests;
    private final ArrayList<QuestClass> craftItemQuests;
    private final ArrayList<QuestClass> walkQuests;
    private final ArrayList<QuestClass> rideQuests;
    private final ArrayList<QuestClass> flyQuests;
    private final ArrayList<QuestClass> tradeQuests;
    private final ArrayList<QuestClass> breakBlocksQuests;
    private final ArrayList<QuestClass> getItemQuests;
    private final ArrayList<QuestClass> haveHeartQuests;
    private final ArrayList<QuestClass> tameEntityQuests;
    private final ArrayList<QuestClass> placeBlocksQuests;
    private final ArrayList<QuestClass> levelUpQuests;
    private final ArrayList<QuestClass> breakItemQuests;
    private final ArrayList<QuestClass> breakBlocksNoSilkQuests;
    private final ArrayList<QuestClass> swimBlocks;
    private final ArrayList<QuestClass> susBlocksBreak;
    private final ArrayList<QuestClass> breedEntity;
    private final ArrayList<QuestClass> smithingUseQuests;
    private final ArrayList<QuestClass> rightClickEntityQuests;
    private final ArrayList<QuestClass> snowFoxClickQuests;
    private final ArrayList<QuestClass> minecartMoveQuests;
    private final ArrayList<QuestClass> villagerCureQuests;
    private final ArrayList<QuestClass> punchEntityQuests;
    private final ArrayList<QuestClass> useItemQuests;
    private final ArrayList<QuestClass> smeltItemQuests;


    public TaskListenerArrays(ArrayList<QuestClass> allQuests) {
        //Filters every task to its special category of listeners to allow for custom tasks which are not coded in the plugin itself

        eatQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.EAT).toList());
        killQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.KILL_MOBS).toList());
        craftItemQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.CRAFT).toList());
        walkQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.WALK_DISTANCE).toList());
        breakBlocksQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.BREAK_BLOCKS).toList());
        rideQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.RIDE_DISTANCE).toList());
        flyQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.FLY_DISTANCE).toList());
        tradeQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.VILLAGER_TRADE).toList());
        getItemQuests  = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.GET_ITEM).toList());
        haveHeartQuests  = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.PLAYER_HEART).toList());
        tameEntityQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.TAME_ANIMAL).toList());
        placeBlocksQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.PLACE_BLOCKS).toList());
        levelUpQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.LEVEL_UP).toList());
        breakItemQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.BREAK_ITEM).toList());
        breakBlocksNoSilkQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.BREAK_BLOCKS_NO_SILK).toList());
        swimBlocks = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.SWIM_DISTANCE).toList());
        susBlocksBreak = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.MINE_SUS_BLOCK).toList());
        breedEntity = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.BREED_ENTITY).toList());
        smithingUseQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.SMITHING_USE).toList());
        rightClickEntityQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.RIGHT_CLICK_ENTITY).toList());
        snowFoxClickQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.SNOW_FOX_INTERACT).toList());
        minecartMoveQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.MINECART_RIDE).toList());
        villagerCureQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.VILLAGER_CURE).toList());
        punchEntityQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.PUNCH_ENTITY).toList());
        useItemQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.USE_ITEM).toList());
        smeltItemQuests = new ArrayList<>(allQuests.stream().filter(s -> s.getListenerType() == TaskListenerType.SMELT_ITEM).toList());


    }

    public ArrayList<QuestClass> getEatQuests() {
        return eatQuests;
    }

    public ArrayList<QuestClass> getKillQuests() {
        return killQuests;
    }

    public ArrayList<QuestClass> getCraftItemQuests() {
        return craftItemQuests;
    }

    public ArrayList<QuestClass> getWalkQuests() {
        return walkQuests;
    }

    public ArrayList<QuestClass> getRideQuests() {
        return rideQuests;
    }

    public ArrayList<QuestClass> getFlyQuests() {
        return flyQuests;
    }

    public ArrayList<QuestClass> getTradeQuests() {
        return tradeQuests;
    }

    public ArrayList<QuestClass> getBreakBlocksQuests() {
        return breakBlocksQuests;
    }

    public ArrayList<QuestClass> getGetItemQuests() {
        return getItemQuests;
    }

    public ArrayList<QuestClass> getHaveHeartQuests() {
        return haveHeartQuests;
    }

    public ArrayList<QuestClass> getTameEntityQuests() {
        return tameEntityQuests;
    }

    public ArrayList<QuestClass> getPlaceBlocksQuests() {
        return placeBlocksQuests;
    }

    public ArrayList<QuestClass> getLevelUpQuests() {
        return levelUpQuests;
    }

    public ArrayList<QuestClass> getBreakItemQuests() {
        return breakItemQuests;
    }

    public ArrayList<QuestClass> getBreakBlocksNoSilkQuests() {
        return breakBlocksNoSilkQuests;
    }

    public ArrayList<QuestClass> getSwimBlocks() {
        return swimBlocks;
    }

    public ArrayList<QuestClass> getSusBlocksBreak() {
        return susBlocksBreak;
    }

    public ArrayList<QuestClass> getBreedEntity() {
        return breedEntity;
    }

    public ArrayList<QuestClass> getSmithingUseQuests() {
        return smithingUseQuests;
    }

    public ArrayList<QuestClass> getRightClickEntityQuests() {
        return rightClickEntityQuests;
    }

    public ArrayList<QuestClass> getSnowFoxClickQuests() {
        return snowFoxClickQuests;
    }

    public ArrayList<QuestClass> getMinecartMoveQuests() {
        return minecartMoveQuests;
    }

    public ArrayList<QuestClass> getVillagerCureQuests() {
        return villagerCureQuests;
    }

    public ArrayList<QuestClass> getPunchEntityQuests() {
        return punchEntityQuests;
    }

    public ArrayList<QuestClass> getUseItemQuests() {
        return useItemQuests;
    }

    public ArrayList<QuestClass> getSmeltItemQuests() {
        return smeltItemQuests;
    }



}

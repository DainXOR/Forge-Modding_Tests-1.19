package net.dain.testsmod.client;

public class ClientThirstData {
    private static int playerThirst;
    private static int playerSatiety;

    public static void setPlayerThirst(int thirst){
        ClientThirstData.playerThirst = thirst;
    }
    public static int getPlayerThirst(){
        return playerThirst;
    }

    public static void setPlayerSatiety(int satiety){
        ClientThirstData.playerSatiety = satiety;
    }
    public static int getPlayerSatiety(){
        return playerSatiety;
    }
}

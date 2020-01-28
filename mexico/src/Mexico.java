
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;

/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 *
 */
public class Mexico {

    public static void main(String[] args) {
        new Mexico().program();
    }

    final Random rand = new Random();
    final Scanner sc = new Scanner(in);
    final int maxRolls = 3;      // No player may exceed this
    final int startAmount = 3;   // Money for a player. Select any
    final int mexico = 1000;     // A value greater than any other

    void program() {
        test();            // <----------------- UNCOMMENT to test

        int pot = 0;         // What the winner will get
        Player[] players;    // The players (array of Player objects)
        Player current;      // Current player for round
        Player leader;       // Player starting the round

        players = getPlayers();
        current = getRandomPlayer(players);
        leader = current;

        out.println("Mexico Game Started");
        statusMsg(players);
        int playamount = 0; // amount of players that has played this round

        while (players.length > 1) {   // Game over when only one player left

            // ----- In ----------
            String cmd = getPlayerChoice(current);
            if ("r".equals(cmd)) {

                    // --- Process ------
                if (current.nRolls < leader.nRolls || (leader == current && current.nRolls < maxRolls)){
                    current = rollDice(current);
                    roundMsg(current);
                }
                else{
                    out.println("exceeded max rolls");
                    current = next(players, current);
                    playamount++;
                }

            } else if ("n".equals(cmd)) {
                current = next(players, current);
                playamount++;
                 // Process
            } else {
                out.println("?");
            }

            if (playamount == players.length) {
                // --- Process -----
                Player loser = getLoser(players);
                current = loser;
                loser.amount--;
                pot++;
                if(loser.amount == 0) {
                    players = removeLoser(players, loser);
                    current = getRandomPlayer(players);
                }
                clearRoundResults(players);
                playamount = 0;
                leader = current;
                // ----- Out --------------------
                out.println("Round done " + loser.name + " lost!");
                out.println("Next to roll is " + current.name);

                statusMsg(players);
            }
        }
        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }


    // ---- Game logic methods --------------

    // TODO


    int indexOf(Player[] players, Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i + 1;
            }
        }
        return -1;
    }

    Player next(Player[] players , Player player){
        if(players.length == indexOf(players, player)){
            player = players[0];
        }
        else {
            player = players[indexOf(players, player)];
        }
        return player;
    }

    int getScore(Player player){
        int score = 0;
        if(player.fstDice + player.secDice == 3){
            score = mexico;
        }
        else if(player.fstDice == player.secDice){
            score = player.fstDice * 100;
        }
        else if(player.fstDice > player.secDice){
            score = (player.fstDice * 10) + player.secDice;
        }
        else{
            score = (player.secDice * 10) + player.fstDice;
        }
        return score;
    }

    Player getLoser(Player[] players){
        int loserindex = 0;
        for (int x = 0; x < players.length; x++){
            if(getScore(players[x]) < getScore(players[loserindex]) ){
                loserindex = x;
            }
        }
        return players[loserindex];
    }

    Player[] removeLoser(Player[] players, Player loser){
        Player[] narr = new Player[players.length - 1];
        int k = 0;
        for (int i = 0; i < players.length; i++){
            if (players[i] != loser){
                narr[i - k] = players[i];
            }
            else{
                k = 1;
            }
        }
        return narr;
    }


    Player getRandomPlayer(Player[] players) {
        return players[rand.nextInt(players.length)];
    }

    Player[] clearRoundResults(Player[] players){
        for (int i = 0; i < players.length; i++){
            players[i].fstDice = 0;
            players[i].secDice = 0;
            players[i].nRolls = 0;
        }
        return players;
    }

    Player rollDice(Player player){
        player.fstDice = rand.nextInt(6) + 1;
        player.secDice = rand.nextInt(6) + 1;
        player.nRolls++;
        return player;
    }




    // ---------- IO methods -----------------------

    Player[] getPlayers() {
        // Ugly for now. If using a constructor this may
        // be cleaned up.
        Player[] players = new Player[3];
        players[0] = new Player("Olle",  startAmount);
        players[1] = new Player("Fia", startAmount);
        players[2] = new Player("Lisa", startAmount);
        return players;
    }

    void statusMsg(Player[] players) {
        out.print("Status: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " " + players[i].amount + " ");
        }
        out.println();
    }

    void roundMsg(Player current) {
        out.println(current.name + " got " +
                current.fstDice + " and " + current.secDice);
    }

    String getPlayerChoice(Player player) {
        out.print("Player is " + player.name + " > ");
        return sc.nextLine();
    }

    // Possibly useful utility during development
    String toString(Player p){
        return p.name + ", " + p.amount + ", " + p.fstDice + ", "
                + p.secDice + ", " + p.nRolls;
    }

    // Class for a player
    class Player {
        String name;
        int amount;   // Start amount (money)
        int fstDice;  // Result of first dice
        int secDice;  // Result of second dice
        int nRolls;   // Current number of rolls

        Player(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    /**************************************************
     *  Testing
     *
     *  Test are logical expressions that should
     *  evaluate to true (and then be written out)
     *  No testing of IO methods
     *  Uncomment in program() to run test (only)
     ***************************************************/
    void test() {
        // A few hard coded player to use for test
        // NOTE: Possible to debug tests from here, very efficient!
        Player[] ps = {new Player("t", 3), new Player("T", 3), new Player("f", 3)};
        ps[0].fstDice = 2;
        ps[0].secDice = 6;
        ps[1].fstDice = 6;
        ps[1].secDice = 5;
        ps[2].fstDice = 1;
        ps[2].secDice = 1;

        out.println(getScore(ps[0]) == 62);
        out.println(getScore(ps[1]) == 65);
        out.println(next(ps, ps[0]) == ps[1]);
        out.println(getLoser(ps) == ps[0]);

        exit(0);
    }


}

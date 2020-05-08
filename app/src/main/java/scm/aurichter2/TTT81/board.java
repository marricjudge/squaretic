package scm.aurichter2.TTT81;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.util.Arrays;

public class board extends AppCompatActivity implements OnClickListener { //implementing View.OnClickListener, idea taken from https://stackoverflow.com/questions/35827904/incompatible-types-mainactivity-cannot-be-converted-to-onclicklistener

    private Button[][][] buttonArray = new Button[9][3][3]; //This line is taken from https://www.youtube.com/watch?v=9nVSYkQoV5I
    private boolean PlayerBLUE = true; //Player blue is playing first. When boolean is false the its the others players turn
    private int lastButtonID = 0;
    private int playedRounds = 0; //Counts the played rounds
    private int[] playedTiles = new int[81]; //Integer array for all played tiles
    private String lastButtonIDString; //Last button ID as string
    private String[] playedTilesString = new String[81]; //Array with ALL played tiles with playedRounds as index
    private String[] playedTilesStringBLUE = new String[81]; //Array with all BLUE played tiles with playedRounds as index
    private String[] playedTilesStringRED =  new String[81]; //Array with all RED  played tiles with playedRounds as index
    private int finishedField0 = 0; //0 for unfinished, 1 finished by BLUE, 2 finished by RED
    private int finishedField1 = 0; private int finishedField2 = 0; private int finishedField3 = 0; private int finishedField4 = 0;
    private int finishedField5 = 0; private int finishedField6 = 0; private int finishedField7 = 0; private int finishedField8 = 0;
    private boolean resetFlag = false; //When true resetting the game

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toast.makeText(this,"Press on any tile to start", Toast.LENGTH_LONG).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        for(int i = 0; i < 9; i++){ //Nest loop idea based on https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    String btnID = "button_" + i + j + k; //Generates unique button ID as string
                    int ressourcesButtonID = getResources().getIdentifier(btnID, "id", getPackageName()); //Syntax from https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
                    buttonArray[i][j][k] = findViewById(ressourcesButtonID);
                    buttonArray[i][j][k].setOnClickListener(this);
                    disablePlayedButtons();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (!((Button) view).getText().toString().equals("")) { //Method to check if the clicked button was used before (Contains an empty string or not). If false it returns the method. Syntax from https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
            return;
        }

        int passButtonID = view.getId(); //Concept from https://stackoverflow.com/questions/3920833/android-imageview-getid-returning-integer
        lastButtonIDString = getResources().getResourceName(passButtonID); //Get the button ID string
        lastButtonID = ((Button)view).getId(); //https://stackoverflow.com/questions/3920833/android-imageview-getid-returning-integer

        if (PlayerBLUE == true) { //When its player blues turn
            StringArrayPlayedTilesAddBLUE();
            ((Button)view).setBackgroundColor(Color.rgb(107,149,255)); //Set clicked button to blue
            ((Button)view).setPadding(10,10,10,10);
            Log.d("TTT81", "LastButtonID = " + lastButtonID);
            greyOut((Button)view);
            PlayerBLUE = false; //Swaps the player turn

        } else {
            StringArrayPlayedTilesAddRED();
            ((Button)view).setBackgroundColor(Color.rgb(255,90,83)); //RED color
            Log.d("TTT81", "LastButtonID = " + lastButtonID);
            greyOut((Button)view);
            PlayerBLUE = true;
        }

        playedTilesAdd(); //Adds played tiles to corresponding arrays
        StringArrayPlayedTilesAdd();  //Adds played tiles to corresponding arrays
        playedRounds++; //Increase round count
        Log.d("TTT81","Played rounds " + playedRounds);
        checkFieldsforWin();
        fieldWinColourFill((Button)view);
    }

    private void playedTilesAdd(){ //Function to add played buttons to array
        playedTiles[playedRounds] = lastButtonID; disablePlayedButtons();
        Log.d("TTT81", "playedTiles Array is " + playedTiles[playedRounds] + " at array position " + playedRounds);
    }
    private  void StringArrayPlayedTilesAdd(){ //Adds the IDs of all played buttons
        playedTilesString[playedRounds] = lastButtonIDString; disablePlayedButtons();
        Log.d("TTT81", "StringArrayPlayedTilesAdd is " + playedTilesString[playedRounds] + " at array position " + playedRounds);
    }
    private  void StringArrayPlayedTilesAddBLUE(){ //Adds the IDs of all BLUE buttons
        playedTilesStringBLUE[playedRounds] = lastButtonIDString; disablePlayedButtons();
        Log.d("TTT81", "StringArrayPlayedTilesAddBLUE is " + playedTilesStringBLUE[playedRounds] + " at array position " + playedRounds);
    }
    private  void StringArrayPlayedTilesAddRED(){ //Adds the IDs of all RED buttons
        playedTilesStringRED[playedRounds] = lastButtonIDString; disablePlayedButtons();
        Log.d("TTT81", "StringArrayPlayedTilesAddRED is " + playedTilesStringRED[playedRounds] + " at array position " + playedRounds);
    }

    private void checkFieldsforWin(){ //Checks if any of the fields is won by one player
        field0win(); field1win(); field2win(); field3win(); field4win(); field5win(); field6win(); field7win(); field8win();
        checkForFinalWin();
    }

    private void greyOut(View view){ //Greys out and disable all the not to be accessable buttons
        for(int i = 0; i < 9; i++){ //Nest loop idea based on https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    String btnID = "button_" + i + j + k; //Generates unique button ID as string
                    int ressourcesButtonID = getResources().getIdentifier(btnID, "id", getPackageName()); //Syntax from https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
                    buttonArray[i][j][k] = findViewById(ressourcesButtonID);
                    Button buttonTurn = findViewById(R.id.button_turn);
                    buttonTurn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) { //When the top button is clicked it restarts the board activity and restarts the game
                            Intent intent = getIntent(); //Restarts the activity https://stackoverflow.com/questions/1397361/how-do-i-restart-an-android-activity
                            finish();
                            startActivity(intent);
                        }
                    });

                    if (PlayerBLUE == true){ //Changes the top button according to the next players turn
                        buttonTurn.setBackgroundColor(Color.rgb(255,90,83)); //When it was player blues turn, it displays that player RED is next
                        buttonTurn.setText("Player RED's turn");

                    } else {
                        buttonTurn.setBackgroundColor(Color.rgb(107,149,255)); //BLUE color
                        buttonTurn.setText("Player BLUE's turn");
                    }

                    //The following block disables all buttons and enables the one clickable in the next selected field
                    if (lastButtonID == 2131165251 || lastButtonID == 2131165260 || lastButtonID == 2131165269 || lastButtonID == 2131165278 || lastButtonID == 2131165287 || lastButtonID == 2131165296 || lastButtonID == 2131165305 || lastButtonID == 2131165314 || lastButtonID == 2131165323) { // Button IDs for Block 0(upper left corner)
                        Log.d("TTT81","Buttons in field 0 (upper left) are activated");
                        buttonArray[i][j][k].setEnabled(false); //Greys out the buttons https://stackoverflow.com/questions/41353918/disable-one-button-when-another-is-clicked-in-android
                        buttonArray[0][j][k].setEnabled(true);  }//Enables the buttons only in field 0 (upper left) to be clickable
                    if (lastButtonID == 2131165252 || lastButtonID == 2131165261 || lastButtonID == 2131165270 || lastButtonID == 2131165279 || lastButtonID == 2131165288 || lastButtonID == 2131165297 || lastButtonID == 2131165306 || lastButtonID == 2131165315 || lastButtonID == 2131165324) {
                        Log.d("TTT81","Buttons in field 1 (upper middle) are activated"); buttonArray[i][j][k].setEnabled(false); buttonArray[1][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165253 || lastButtonID == 2131165262 || lastButtonID == 2131165271 || lastButtonID == 2131165280 || lastButtonID == 2131165289 || lastButtonID == 2131165298 || lastButtonID == 2131165307 || lastButtonID == 2131165316 || lastButtonID == 2131165325) {
                        Log.d("TTT81","Buttons in field 2 (upper right) are activated");  buttonArray[i][j][k].setEnabled(false); buttonArray[2][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165254 || lastButtonID == 2131165263 || lastButtonID == 2131165272 || lastButtonID == 2131165281 || lastButtonID == 2131165290 || lastButtonID == 2131165299 || lastButtonID == 2131165308 || lastButtonID == 2131165317 || lastButtonID == 2131165326) {
                        Log.d("TTT81","Buttons in field 3 (middle left) are activated");  buttonArray[i][j][k].setEnabled(false); buttonArray[3][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165255 || lastButtonID == 2131165264 || lastButtonID == 2131165273 || lastButtonID == 2131165282 || lastButtonID == 2131165291 || lastButtonID == 2131165300 || lastButtonID == 2131165309 || lastButtonID == 2131165318 || lastButtonID == 2131165327) {
                        Log.d("TTT81","Buttons in field 4 (middle middle) are activated");buttonArray[i][j][k].setEnabled(false); buttonArray[4][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165256 || lastButtonID == 2131165265 || lastButtonID == 2131165274 || lastButtonID == 2131165283 || lastButtonID == 2131165292 || lastButtonID == 2131165301 || lastButtonID == 2131165310 || lastButtonID == 2131165319 || lastButtonID == 2131165328) {
                        Log.d("TTT81","Buttons in field 5 (middle right) are activated"); buttonArray[i][j][k].setEnabled(false); buttonArray[5][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165257 || lastButtonID == 2131165266 || lastButtonID == 2131165275 || lastButtonID == 2131165284 || lastButtonID == 2131165293 || lastButtonID == 2131165302 || lastButtonID == 2131165311 || lastButtonID == 2131165320 || lastButtonID == 2131165329) {
                        Log.d("TTT81","Buttons in field 6 (lower left) are activated");   buttonArray[i][j][k].setEnabled(false); buttonArray[6][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165258 || lastButtonID == 2131165267 || lastButtonID == 2131165276 || lastButtonID == 2131165285 || lastButtonID == 2131165294 || lastButtonID == 2131165303 || lastButtonID == 2131165312 || lastButtonID == 2131165321 || lastButtonID == 2131165330) {
                        Log.d("TTT81","Buttons in field 7 (lower middle) are activated"); buttonArray[i][j][k].setEnabled(false); buttonArray[7][j][k].setEnabled(true); }
                    if (lastButtonID == 2131165259 || lastButtonID == 2131165268 || lastButtonID == 2131165277 || lastButtonID == 2131165286 || lastButtonID == 2131165295 || lastButtonID == 2131165304 || lastButtonID == 2131165313 || lastButtonID == 2131165322 || lastButtonID == 2131165331) {
                        Log.d("TTT81","Buttons in field 8 (lower right) are activated");  buttonArray[i][j][k].setEnabled(false); buttonArray[8][j][k].setEnabled(true); }

                    //The following block disables a field when it was won and enables all other buttons as the player can choose freely now
                    if (finishedField0 != 0) { buttonArray[0][j][k].setEnabled(false);//Checks if the field 0 upper left was won //Disables all the buttons in the won field, as they cant be played anymore
                        if ((lastButtonID == 2131165251 || lastButtonID == 2131165260 || lastButtonID == 2131165269 || lastButtonID == 2131165278 || lastButtonID == 2131165287 || lastButtonID == 2131165296 || lastButtonID == 2131165305 || lastButtonID == 2131165314 || lastButtonID == 2131165323)){
                            buttonArray[i][j][k].setEnabled(true);  } }//When one of the upper left corner fields was played all other tiles are availabe as the field 0 (upper left) is finished and cant be played anymore
                    if (finishedField1 != 0) { buttonArray[1][j][k].setEnabled(false);//Checks if the field 1 upper middle was won //Disables all the buttons in the won field, as they cant be played anymore//
                        if (lastButtonID == 2131165252 || lastButtonID == 2131165261 || lastButtonID == 2131165270 || lastButtonID == 2131165279 || lastButtonID == 2131165288 || lastButtonID == 2131165297 || lastButtonID == 2131165306 || lastButtonID == 2131165315 || lastButtonID == 2131165324) {
                            buttonArray[i][j][k].setEnabled(true); } }//When one of the upper middle corner fields was played all other tiles are availabe as the field 1 (upper middle) is finished and cant be played anymore
                    if (finishedField2 != 0) { buttonArray[2][j][k].setEnabled(false);
                        if (lastButtonID == 2131165253 || lastButtonID == 2131165262 || lastButtonID == 2131165271 || lastButtonID == 2131165280 || lastButtonID == 2131165289 || lastButtonID == 2131165298 || lastButtonID == 2131165307 || lastButtonID == 2131165316 || lastButtonID == 2131165325) {
                            buttonArray[i][j][k].setEnabled(true); } }
                    if (finishedField3 != 0) { buttonArray[3][j][k].setEnabled(false);
                        if (lastButtonID == 2131165254 || lastButtonID == 2131165263 || lastButtonID == 2131165272 || lastButtonID == 2131165281 || lastButtonID == 2131165290 || lastButtonID == 2131165299 || lastButtonID == 2131165308 || lastButtonID == 2131165317 || lastButtonID == 2131165326) {
                            buttonArray[i][j][k].setEnabled(true); } }
                    if (finishedField4 != 0) { buttonArray[4][j][k].setEnabled(false);
                        if (lastButtonID == 2131165255 || lastButtonID == 2131165264 || lastButtonID == 2131165273 || lastButtonID == 2131165282 || lastButtonID == 2131165291 || lastButtonID == 2131165300 || lastButtonID == 2131165309 || lastButtonID == 2131165318 || lastButtonID == 2131165327) {
                            buttonArray[i][j][k].setEnabled(true); } }
                    if (finishedField5 != 0) { buttonArray[5][j][k].setEnabled(false);
                        if (lastButtonID == 2131165256 || lastButtonID == 2131165265 || lastButtonID == 2131165274 || lastButtonID == 2131165283 || lastButtonID == 2131165292 || lastButtonID == 2131165301 || lastButtonID == 2131165310 || lastButtonID == 2131165319 || lastButtonID == 2131165328) {
                            buttonArray[i][j][k].setEnabled(true); } }
                    if (finishedField6 != 0) { buttonArray[6][j][k].setEnabled(false);
                        if (lastButtonID == 2131165257 || lastButtonID == 2131165266 || lastButtonID == 2131165275 || lastButtonID == 2131165284 || lastButtonID == 2131165293 || lastButtonID == 2131165302 || lastButtonID == 2131165311 || lastButtonID == 2131165320 || lastButtonID == 2131165329) {
                            buttonArray[i][j][k].setEnabled(true); } }
                    if (finishedField7 != 0) { buttonArray[7][j][k].setEnabled(false);
                        if (lastButtonID == 2131165258 || lastButtonID == 2131165267 || lastButtonID == 2131165276 || lastButtonID == 2131165285 || lastButtonID == 2131165294 || lastButtonID == 2131165303 || lastButtonID == 2131165312 || lastButtonID == 2131165321 || lastButtonID == 2131165330) {
                            buttonArray[i][j][k].setEnabled(true); } }
                    if (finishedField8 != 0) { buttonArray[8][j][k].setEnabled(false);
                        if (lastButtonID == 2131165259 || lastButtonID == 2131165268 || lastButtonID == 2131165277 || lastButtonID == 2131165286 || lastButtonID == 2131165295 || lastButtonID == 2131165304 || lastButtonID == 2131165313 || lastButtonID == 2131165322 || lastButtonID == 2131165331) {
                            buttonArray[i][j][k].setEnabled(true); } }
                }
            }
        }
        disablePlayedButtons(); //Double checks that all played buttons are disabled
    }

    private void fieldWinColourFill(View view) { //Fills a field with one color when one field was won by one player
        for (int i = 0; i < 9; i++) { //Nest loop idea based on https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    String btnID = "button_" + i + j + k; //Generates unique button ID as string
                    int ressourcesButtonID = getResources().getIdentifier(btnID, "id", getPackageName()); //Syntax from https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
                    buttonArray[i][j][k] = findViewById(ressourcesButtonID);

                    if (finishedField0 != 0){ buttonArray[0][j][k].setEnabled(false); } //Disables an entire field when it was won (finishedField variable is not zero)
                    if (finishedField1 != 0){ buttonArray[1][j][k].setEnabled(false); } // 0 = not won yet, 1 = blue won, 2 = red won
                    if (finishedField2 != 0){ buttonArray[2][j][k].setEnabled(false); }
                    if (finishedField3 != 0){ buttonArray[3][j][k].setEnabled(false); }
                    if (finishedField4 != 0){ buttonArray[4][j][k].setEnabled(false); }
                    if (finishedField5 != 0){ buttonArray[5][j][k].setEnabled(false); }
                    if (finishedField6 != 0){ buttonArray[6][j][k].setEnabled(false); }
                    if (finishedField7 != 0){ buttonArray[7][j][k].setEnabled(false); }
                    if (finishedField8 != 0){ buttonArray[8][j][k].setEnabled(false); }

                    if (finishedField0 != 0){
                        if ((lastButtonID == 2131165251 || lastButtonID == 2131165260 || lastButtonID == 2131165269 || lastButtonID == 2131165278 || lastButtonID == 2131165287 || lastButtonID == 2131165296 || lastButtonID == 2131165305 || lastButtonID == 2131165314 || lastButtonID == 2131165323)){
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField0 == 1){ buttonArray[0][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField0 == 2){ buttonArray[0][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField1 != 0){
                        if (lastButtonID == 2131165252 || lastButtonID == 2131165261 || lastButtonID == 2131165270 || lastButtonID == 2131165279 || lastButtonID == 2131165288 || lastButtonID == 2131165297 || lastButtonID == 2131165306 || lastButtonID == 2131165315 || lastButtonID == 2131165324) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField1 == 1){ buttonArray[1][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField1 == 2){ buttonArray[1][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField2 != 0){
                        if (lastButtonID == 2131165253 || lastButtonID == 2131165262 || lastButtonID == 2131165271 || lastButtonID == 2131165280 || lastButtonID == 2131165289 || lastButtonID == 2131165298 || lastButtonID == 2131165307 || lastButtonID == 2131165316 || lastButtonID == 2131165325) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField2 == 1){ buttonArray[2][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField2 == 2){ buttonArray[2][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField3 != 0){
                        if (lastButtonID == 2131165254 || lastButtonID == 2131165263 || lastButtonID == 2131165272 || lastButtonID == 2131165281 || lastButtonID == 2131165290 || lastButtonID == 2131165299 || lastButtonID == 2131165308 || lastButtonID == 2131165317 || lastButtonID == 2131165326) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField3 == 1){ buttonArray[3][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField3 == 2){ buttonArray[3][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField4 != 0){
                        if (lastButtonID == 2131165255 || lastButtonID == 2131165264 || lastButtonID == 2131165273 || lastButtonID == 2131165282 || lastButtonID == 2131165291 || lastButtonID == 2131165300 || lastButtonID == 2131165309 || lastButtonID == 2131165318 || lastButtonID == 2131165327) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField4 == 1){ buttonArray[4][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField4 == 2){ buttonArray[4][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField5 != 0){
                        if (lastButtonID == 2131165256 || lastButtonID == 2131165265 || lastButtonID == 2131165274 || lastButtonID == 2131165283 || lastButtonID == 2131165292 || lastButtonID == 2131165301 || lastButtonID == 2131165310 || lastButtonID == 2131165319 || lastButtonID == 2131165328) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField5 == 1){ buttonArray[5][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField5 == 2){ buttonArray[5][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField6 != 0){
                        if (lastButtonID == 2131165257 || lastButtonID == 2131165266 || lastButtonID == 2131165275 || lastButtonID == 2131165284 || lastButtonID == 2131165293 || lastButtonID == 2131165302 || lastButtonID == 2131165311 || lastButtonID == 2131165320 || lastButtonID == 2131165329) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField6 == 1){ buttonArray[6][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField6 == 2){ buttonArray[6][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField7 != 0){
                        if (lastButtonID == 2131165258 || lastButtonID == 2131165267 || lastButtonID == 2131165276 || lastButtonID == 2131165285 || lastButtonID == 2131165294 || lastButtonID == 2131165303 || lastButtonID == 2131165312 || lastButtonID == 2131165321 || lastButtonID == 2131165330) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField7 == 1){ buttonArray[7][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField7 == 2){ buttonArray[7][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                    if (finishedField8 != 0){
                        if (lastButtonID == 2131165259 || lastButtonID == 2131165268 || lastButtonID == 2131165277 || lastButtonID == 2131165286 || lastButtonID == 2131165295 || lastButtonID == 2131165304 || lastButtonID == 2131165313 || lastButtonID == 2131165322 || lastButtonID == 2131165331) {
                            buttonArray[i][j][k].setEnabled(true); }
                        if (finishedField8 == 1){ buttonArray[8][j][k].setBackgroundColor(Color.rgb(107,149,255)); }
                        else if (finishedField8 == 2){ buttonArray[8][j][k].setBackgroundColor(Color.rgb(255,90,83)); } }
                }
            }
        }
    }

    private void resetGame(){ //Resets the game
        for(int i = 0; i < 9; i++) { //Nest loop idea based on https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    String btnID = "button_" + i + j + k; //Generates unique button ID as string
                    int ressourcesButtonID = getResources().getIdentifier(btnID, "id", getPackageName()); //Syntax from https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
                    buttonArray[i][j][k] = findViewById(ressourcesButtonID);

                    if (resetFlag){ //When resetFlag was set true
                        buttonArray[i][j][k].setEnabled(false); //Disables all buttons when the game was won
                        Button buttonTurn = findViewById(R.id.button_turn);
                        buttonTurn.setText("Tap to restart");
                        buttonTurn.setBackgroundColor(Color.LTGRAY);
                        buttonTurn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = getIntent(); //Restarts the activity https://stackoverflow.com/questions/1397361/how-do-i-restart-an-android-activity
                                finish();
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }
    }

    private void checkForFinalWin(){ //Checks if any player won enough fields to win the entire game
        if ((finishedField0 == 1 && finishedField1 == 1 && finishedField2 == 1) //Checking for the winning conditions of player blue; Horizontal first row
                || (finishedField3 == 1 && finishedField4 == 1 && finishedField5 == 1) //Horizontal second row
                || (finishedField6 == 1 && finishedField7 == 1 && finishedField8 == 1) //Horizontal third row
                || (finishedField0 == 1 && finishedField3 == 1 && finishedField6 == 1) //Vertical first column
                || (finishedField1 == 1 && finishedField4 == 1 && finishedField7 == 1) //Vertical second column
                || (finishedField2 == 1 && finishedField5 == 1 && finishedField8 == 1) //Vertical third column
                || (finishedField0 == 1 && finishedField4 == 1 && finishedField8 == 1) //Diagonal from upper left to lower right
                || (finishedField6 == 1 && finishedField4 == 1 && finishedField2 == 1) ) { //Diagonal from lower left to upper right
            Toast.makeText(this,"Player BLUE won", Toast.LENGTH_LONG).show(); //Output message to screen
            resetFlag = true;
            resetGame(); //Call reset

        } else if ((finishedField0 == 2 && finishedField1 == 2 && finishedField2 == 2) //Checking for the winning conditions of player blue
                || (finishedField3 == 2 && finishedField4 == 2 && finishedField5 == 2)
                || (finishedField6 == 2 && finishedField7 == 2 && finishedField8 == 2)
                || (finishedField0 == 2 && finishedField3 == 2 && finishedField6 == 2)
                || (finishedField1 == 2 && finishedField4 == 2 && finishedField7 == 2)
                || (finishedField2 == 2 && finishedField5 == 2 && finishedField8 == 2)
                || (finishedField0 == 2 && finishedField4 == 2 && finishedField8 == 2) //Diagonal from upper left to lower right
                || (finishedField6 == 2 && finishedField4 == 2 && finishedField2 == 2) ) { //Diagonal from lower left to upper right
            Toast.makeText(this,"Player RED won", Toast.LENGTH_LONG).show();
            resetFlag = true;
            resetGame();
        } else if (finishedField0 != 0 && finishedField1 != 0 && finishedField2 != 0 && finishedField3 != 0 && finishedField4 != 0 && finishedField5 != 0 && finishedField6 != 0 && finishedField7 != 0 && finishedField8 != 0 ){ //Makes it a draw when all fields have been won
            Toast.makeText(this,"It's a draw!", Toast.LENGTH_LONG).show();
            resetFlag = true;
            resetGame();
        }
    }

    private void field0win(){ //Checks if the field 0 upper left was won by a player
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_000"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_000"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_001"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_001");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_002"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_002");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_010"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_010");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_011"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_011");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_012"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_012");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_020"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_020");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_021"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_021");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_022"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_022");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField0 = 1;     Log.d("TTT81","Field 0 Upper Left was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField0 = 2;     Log.d("TTT81","Field 0 Upper Left was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field1win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_100"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_100"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_101"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_101");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_102"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_102");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_110"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_110");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_111"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_111");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_112"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_112");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_120"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_120");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_121"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_121");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_122"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_122");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField1 = 1;     Log.d("TTT81","Field 1 Upper Middle was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField1 = 2; Log.d("TTT81","Field 1 Upper Middle was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field2win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_200"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_200"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_201"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_201");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_202"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_202");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_210"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_210");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_211"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_211");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_212"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_212");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_220"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_220");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_221"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_221");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_222"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_222");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField2 = 1;     Log.d("TTT81","Field 2 Upper Right was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField2 = 2; Log.d("TTT81","Field 2 Upper Right was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field3win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_300"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_300"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_301"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_301");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_302"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_302");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_310"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_310");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_311"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_311");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_312"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_312");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_320"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_320");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_321"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_321");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_322"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_322");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField3 = 1;     Log.d("TTT81","Field 3 Middle Left was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField3 = 2; Log.d("TTT81","Field 3 Middle Left was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field4win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_400"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_400"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_401"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_401");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_402"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_402");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_410"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_410");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_411"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_411");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_412"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_412");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_420"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_420");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_421"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_421");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_422"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_422");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField4 = 1;     Log.d("TTT81","Field 4 Middle Middle was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField4 = 2; Log.d("TTT81","Field 4 Middle Middle was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field5win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_500"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_500"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_501"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_501");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_502"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_502");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_510"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_510");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_511"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_511");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_512"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_512");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_520"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_520");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_521"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_521");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_522"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_522");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField5 = 1;     Log.d("TTT81","Field 5 Middle Right was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField5 = 2; Log.d("TTT81","Field 5 Middle Right was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field6win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_600"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_600"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_601"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_601");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_602"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_602");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_610"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_610");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_611"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_611");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_612"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_612");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_620"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_620");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_621"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_621");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_622"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_622");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField6 = 1;     Log.d("TTT81","Field 6 Lower Left was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField6 = 2; Log.d("TTT81","Field 6 Lower Left was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field7win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_700"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_700"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_701"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_701");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_702"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_702");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_710"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_710");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_711"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_711");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_712"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_712");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_720"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_720");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_721"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_721");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_722"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_722");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField7 = 1;     Log.d("TTT81","Field 7 Lower Middle was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField7 = 2; Log.d("TTT81","Field 7 Lower Middle was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won

    private void field8win(){
        boolean B00 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_800"); boolean R00 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_800"); //Method from https://www.android-examples.com/check-if-a-string-array-contains-a-certain-value-java-android/
        boolean B01 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_801"); boolean R01 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_801");
        boolean B02 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_802"); boolean R02 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_802");
        boolean B10 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_810"); boolean R10 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_810");
        boolean B11 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_811"); boolean R11 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_811");
        boolean B12 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_812"); boolean R12 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_812");
        boolean B20 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_820"); boolean R20 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_820");
        boolean B21 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_821"); boolean R21 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_821");
        boolean B22 = Arrays.asList(playedTilesStringBLUE).contains("scm.aurichter2.TTT81:id/button_822"); boolean R22 = Arrays.asList(playedTilesStringRED).contains("scm.aurichter2.TTT81:id/button_822");

        if ((B00 && B01 && B02) || (B10 && B11 && B12) || (B20 && B21 && B22) || (B00 && B10 && B20) || (B01 && B11 && B21) || (B02 && B12 && B22) || (B00 && B11 && B22) || (B02 && B11 && B20) ){ //All possible combinations for blue to win
            finishedField8 = 1;     Log.d("TTT81","Field 8 Lower Right was won by BLUE"); //Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won
        } else if ((R00 && R01 && R02) || (R10 && R11 && R12) || (R20 && R21 && R22) || (R00 && R10 && R20) || (R01 && R11 && R21) || (R02 && R12 && R22) || (R00 && R11 && R22) || (R02 && R11 && R20) ){
            finishedField8 = 2; Log.d("TTT81","Field 8 Lower Right was won by RED"); } }//Blue won field 0 (Index 0 = unfinished, 1 = BLUE won, 2 = RED won


    private void disablePlayedButtons() { //Disables all played buttons by reading out of the arrays
        for (int i = 0; i < 9; i++) { //Nest loop idea based on https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    String btnID = "button_" + i + j + k; //Generates unique button ID as string
                    int ressourcesButtonID = getResources().getIdentifier(btnID, "id", getPackageName());//Syntax from https://codinginflow.com/tutorials/android/tic-tac-toe/part-4-configuration-changes-reset-game
                    buttonArray[i][j][k] = findViewById(ressourcesButtonID);

                    Log.d("TTT81","disablePlayedButtons is at position with " + playedTilesString[playedRounds]);

                    if (finishedField0 != 0){ buttonArray[0][j][k].setEnabled(false); } //Disables an entire field when it was won (finishedField variables is not zero)
                    if (finishedField1 != 0){ buttonArray[1][j][k].setEnabled(false); }
                    if (finishedField2 != 0){ buttonArray[2][j][k].setEnabled(false); }
                    if (finishedField3 != 0){ buttonArray[3][j][k].setEnabled(false); }
                    if (finishedField4 != 0){ buttonArray[4][j][k].setEnabled(false); }
                    if (finishedField5 != 0){ buttonArray[5][j][k].setEnabled(false); }
                    if (finishedField6 != 0){ buttonArray[6][j][k].setEnabled(false); }
                    if (finishedField7 != 0){ buttonArray[7][j][k].setEnabled(false); }
                    if (finishedField8 != 0){ buttonArray[8][j][k].setEnabled(false); }

                    //Disables every button permanentely when it is part of the playedTilesString, where every played button gets added to
                    boolean b000 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_000");  if (b000){ buttonArray[0][0][0].setEnabled(false); }
                    boolean b001 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_001");  if (b001){ buttonArray[0][0][1].setEnabled(false); }
                    boolean b002 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_002");  if (b002){ buttonArray[0][0][2].setEnabled(false); }
                    boolean b010 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_010");  if (b010){ buttonArray[0][1][0].setEnabled(false); }
                    boolean b011 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_011");  if (b011){ buttonArray[0][1][1].setEnabled(false); }
                    boolean b012 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_012");  if (b012){ buttonArray[0][1][2].setEnabled(false); }
                    boolean b020 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_020");  if (b020){ buttonArray[0][2][0].setEnabled(false); }
                    boolean b021 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_021");  if (b021){ buttonArray[0][2][1].setEnabled(false); }
                    boolean b022 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_022");  if (b022){ buttonArray[0][2][2].setEnabled(false); }

                    boolean b100 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_100");  if (b100){ buttonArray[1][0][0].setEnabled(false); }
                    boolean b101 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_101");  if (b101){ buttonArray[1][0][1].setEnabled(false); }
                    boolean b102 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_102");  if (b102){ buttonArray[1][0][2].setEnabled(false); }
                    boolean b110 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_110");  if (b110){ buttonArray[1][1][0].setEnabled(false); }
                    boolean b111 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_111");  if (b111){ buttonArray[1][1][1].setEnabled(false); }
                    boolean b112 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_112");  if (b112){ buttonArray[1][1][2].setEnabled(false); }
                    boolean b120 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_120");  if (b120){ buttonArray[1][2][0].setEnabled(false); }
                    boolean b121 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_121");  if (b121){ buttonArray[1][2][1].setEnabled(false); }
                    boolean b122 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_122");  if (b122){ buttonArray[1][2][2].setEnabled(false); }

                    boolean b200 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_200");  if (b200){ buttonArray[2][0][0].setEnabled(false); }
                    boolean b201 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_201");  if (b201){ buttonArray[2][0][1].setEnabled(false); }
                    boolean b202 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_202");  if (b202){ buttonArray[2][0][2].setEnabled(false); }
                    boolean b210 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_210");  if (b210){ buttonArray[2][1][0].setEnabled(false); }
                    boolean b211 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_211");  if (b211){ buttonArray[2][1][1].setEnabled(false); }
                    boolean b212 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_212");  if (b212){ buttonArray[2][1][2].setEnabled(false); }
                    boolean b220 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_220");  if (b220){ buttonArray[2][2][0].setEnabled(false); }
                    boolean b221 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_221");  if (b221){ buttonArray[2][2][1].setEnabled(false); }
                    boolean b222 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_222");  if (b222){ buttonArray[2][2][2].setEnabled(false); }

                    boolean b300 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_300");  if (b300){ buttonArray[3][0][0].setEnabled(false); }
                    boolean b301 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_301");  if (b301){ buttonArray[3][0][1].setEnabled(false); }
                    boolean b302 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_302");  if (b302){ buttonArray[3][0][2].setEnabled(false); }
                    boolean b310 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_310");  if (b310){ buttonArray[3][1][0].setEnabled(false); }
                    boolean b311 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_311");  if (b311){ buttonArray[3][1][1].setEnabled(false); }
                    boolean b312 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_312");  if (b312){ buttonArray[3][1][2].setEnabled(false); }
                    boolean b320 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_320");  if (b320){ buttonArray[3][2][0].setEnabled(false); }
                    boolean b321 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_321");  if (b321){ buttonArray[3][2][1].setEnabled(false); }
                    boolean b322 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_322");  if (b322){ buttonArray[3][2][2].setEnabled(false); }

                    boolean b400 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_400");  if (b400){ buttonArray[4][0][0].setEnabled(false); }
                    boolean b401 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_401");  if (b401){ buttonArray[4][0][1].setEnabled(false); }
                    boolean b402 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_402");  if (b402){ buttonArray[4][0][2].setEnabled(false); }
                    boolean b410 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_410");  if (b410){ buttonArray[4][1][0].setEnabled(false); }
                    boolean b411 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_411");  if (b411){ buttonArray[4][1][1].setEnabled(false); }
                    boolean b412 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_412");  if (b412){ buttonArray[4][1][2].setEnabled(false); }
                    boolean b420 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_420");  if (b420){ buttonArray[4][2][0].setEnabled(false); }
                    boolean b421 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_421");  if (b421){ buttonArray[4][2][1].setEnabled(false); }
                    boolean b422 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_422");  if (b422){ buttonArray[4][2][2].setEnabled(false); }

                    boolean b500 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_500");  if (b500){ buttonArray[5][0][0].setEnabled(false); }
                    boolean b501 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_501");  if (b501){ buttonArray[5][0][1].setEnabled(false); }
                    boolean b502 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_502");  if (b502){ buttonArray[5][0][2].setEnabled(false); }
                    boolean b510 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_510");  if (b510){ buttonArray[5][1][0].setEnabled(false); }
                    boolean b511 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_511");  if (b511){ buttonArray[5][1][1].setEnabled(false); }
                    boolean b512 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_512");  if (b512){ buttonArray[5][1][2].setEnabled(false); }
                    boolean b520 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_520");  if (b520){ buttonArray[5][2][0].setEnabled(false); }
                    boolean b521 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_521");  if (b521){ buttonArray[5][2][1].setEnabled(false); }
                    boolean b522 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_522");  if (b522){ buttonArray[5][2][2].setEnabled(false); }

                    boolean b600 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_600");  if (b600){ buttonArray[6][0][0].setEnabled(false); }
                    boolean b601 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_601");  if (b601){ buttonArray[6][0][1].setEnabled(false); }
                    boolean b602 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_602");  if (b602){ buttonArray[6][0][2].setEnabled(false); }
                    boolean b610 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_610");  if (b610){ buttonArray[6][1][0].setEnabled(false); }
                    boolean b611 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_611");  if (b611){ buttonArray[6][1][1].setEnabled(false); }
                    boolean b612 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_612");  if (b612){ buttonArray[6][1][2].setEnabled(false); }
                    boolean b620 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_620");  if (b620){ buttonArray[6][2][0].setEnabled(false); }
                    boolean b621 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_621");  if (b621){ buttonArray[6][2][1].setEnabled(false); }
                    boolean b622 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_622");  if (b622){ buttonArray[6][2][2].setEnabled(false); }

                    boolean b700 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_700");  if (b700){ buttonArray[7][0][0].setEnabled(false); }
                    boolean b701 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_701");  if (b701){ buttonArray[7][0][1].setEnabled(false); }
                    boolean b702 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_702");  if (b702){ buttonArray[7][0][2].setEnabled(false); }
                    boolean b710 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_710");  if (b710){ buttonArray[7][1][0].setEnabled(false); }
                    boolean b711 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_711");  if (b711){ buttonArray[7][1][1].setEnabled(false); }
                    boolean b712 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_712");  if (b712){ buttonArray[7][1][2].setEnabled(false); }
                    boolean b720 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_720");  if (b720){ buttonArray[7][2][0].setEnabled(false); }
                    boolean b721 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_721");  if (b721){ buttonArray[7][2][1].setEnabled(false); }
                    boolean b722 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_722");  if (b722){ buttonArray[7][2][2].setEnabled(false); }

                    boolean b800 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_800");  if (b800){ buttonArray[8][0][0].setEnabled(false); }
                    boolean b801 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_801");  if (b801){ buttonArray[8][0][1].setEnabled(false); }
                    boolean b802 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_802");  if (b802){ buttonArray[8][0][2].setEnabled(false); }
                    boolean b810 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_810");  if (b810){ buttonArray[8][1][0].setEnabled(false); }
                    boolean b811 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_811");  if (b811){ buttonArray[8][1][1].setEnabled(false); }
                    boolean b812 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_812");  if (b812){ buttonArray[8][1][2].setEnabled(false); }
                    boolean b820 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:i.d/button_820");  if (b820){ buttonArray[8][2][0].setEnabled(false); }
                    boolean b821 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_821");  if (b821){ buttonArray[8][2][1].setEnabled(false); }
                    boolean b822 = Arrays.asList(playedTilesString).contains("scm.aurichter2.TTT81:id/button_822");  if (b822){ buttonArray[8][2][2].setEnabled(false); }

                    //When all tiles in a field are played it is a draw for this field. Then the field buttons are disabled, all other remaining button enabled and the corresponding field painted in dark grey
                    if(b000 && b001 && b002 && b010 & b011 && b012 && b020 && b021 && b022) { finishedField0 = 0; //When there is a draw on field 0 upper left
                        buttonArray[i][j][k].setEnabled(true); buttonArray[0][j][k].setEnabled(false); buttonArray[0][j][k].setBackgroundColor(Color.DKGRAY); }//Enable all buttons apart from the field with the draw
                    if(b100 && b101 && b102 && b110 & b111 && b112 && b120 && b121 && b122) { finishedField1 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[1][j][k].setEnabled(false); buttonArray[1][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b200 && b201 && b202 && b210 & b211 && b212 && b220 && b221 && b222) { finishedField2 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[2][j][k].setEnabled(false); buttonArray[2][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b300 && b301 && b302 && b310 & b311 && b312 && b320 && b321 && b322) { finishedField3 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[3][j][k].setEnabled(false); buttonArray[3][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b400 && b401 && b402 && b410 & b411 && b412 && b420 && b421 && b422) { finishedField4 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[4][j][k].setEnabled(false); buttonArray[4][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b500 && b501 && b502 && b510 & b511 && b512 && b520 && b521 && b522) { finishedField5 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[5][j][k].setEnabled(false); buttonArray[5][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b600 && b601 && b602 && b610 & b611 && b612 && b620 && b621 && b622) { finishedField6 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[6][j][k].setEnabled(false); buttonArray[6][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b700 && b701 && b702 && b710 & b711 && b712 && b720 && b721 && b722) { finishedField7 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[7][j][k].setEnabled(false); buttonArray[7][j][k].setBackgroundColor(Color.DKGRAY); }
                    if(b800 && b801 && b802 && b810 & b811 && b812 && b820 && b821 && b822) { finishedField8 = 0;
                        buttonArray[i][j][k].setEnabled(true); buttonArray[8][j][k].setEnabled(false); buttonArray[8][j][k].setBackgroundColor(Color.DKGRAY); }
                }
            }
        }
    }
}
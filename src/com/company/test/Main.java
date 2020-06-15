package com.company.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        int userLives = 10;
        long gameStartTimeCounter = System.currentTimeMillis();
        ArrayList<String> encryptedWordsList = new ArrayList<>();
        ArrayList<String> lettersNotInWord = new ArrayList<>();

        String countryCapital = printCountryCapital();
        String[] countryAndCapitalList = countryCapital.split("[|]+");
        String country = countryAndCapitalList[0].strip().toUpperCase();
        String capitalLetterToGuess = countryAndCapitalList[1].strip().toUpperCase();

        changeToDashes(encryptedWordsList, capitalLetterToGuess);

        do {
            printEncryptedCapital(encryptedWordsList);
            System.out.println("Type a letter to guess!");
            printMissedLetters(lettersNotInWord);
            System.out.println("Your lives " + userLives);
            Scanner scan = new Scanner(System.in);
            String userLetter = scan.next().toUpperCase();
            clearScreen();
            if (capitalLetterToGuess.contains(userLetter) && !encryptedWordsList.contains(userLetter)) {
                System.out.println("YOU GUESSED THE LETTER");
                replaceDashesWithLetters(encryptedWordsList, capitalLetterToGuess, userLetter);
            } else if (!capitalLetterToGuess.contains(userLetter) && !lettersNotInWord.contains(userLetter)) {
                System.out.println("Missed!");
                userLives -= 1;
                lettersNotInWord.add(userLetter);
                if (userLives == 0) {
                    endGame("loose", capitalLetterToGuess, country, 0);
                    playAgain();
                }
            } else
                System.out.println("You guessed that letter already!");
            if (userLives == 1) {
                System.out.println("Your last chance! The city is the capital of " + country);
            }
        } while (encryptedWordsList.contains("_"));
        long gameEndTime = System.currentTimeMillis();
        long gameTime = (gameEndTime - gameStartTimeCounter) / 1000;
        endGame("win", capitalLetterToGuess, country, gameTime);
        playAgain();
    }




    public static String printCountryCapital() throws FileNotFoundException {
        String countryCapital = readFromFileAndChooseRandom();
        System.out.println(countryCapital);
        return countryCapital;
    }


    public static void changeToDashes(ArrayList<String> encryptedWordsList, String capitalLetterToGuess) {
        for (int i = 0; i < capitalLetterToGuess.length(); i++) {
            encryptedWordsList.add("_");
        }
    }


    public static void replaceDashesWithLetters(ArrayList<String> dashedWordList, String capitalToGuess, String userLetter) {
        for (int i = 0; i < dashedWordList.size(); i++) {
            char capitalLetter = capitalToGuess.charAt(i);
            char userCharacter = userLetter.charAt(0);
            if (capitalLetter == userCharacter) {
                dashedWordList.set(i, userLetter);
            }
        }
    }


    public static void printMissedLetters(List<String> lettersNotInWord) {
        if (lettersNotInWord.size() > 0) {
            String lettersJoined = String.join(",", lettersNotInWord);
            System.out.println("Letters you have missed: " + lettersJoined);
        }
    }


    public static void printEncryptedCapital(ArrayList<String> encryptedString) {
        String result = "";
        for (String dash : encryptedString) {
            result += dash + " ";
        }
        System.out.println(result);
    }


    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    public static void endGame(String winLose, String capitalToGuess, String country, long gameTime) throws IOException {
        clearScreen();
        System.out.println("You " + winLose + "!");
        System.out.println("The capital of " + country + ", " + capitalToGuess + " was the encrypted word!");
        if (winLose.equals("win")) {
            System.out.println("Your time was " + gameTime + " seconds!");
            formatUserGameResultAndTime(capitalToGuess, gameTime);
        }
        printUserScores();
    }


    public static void playAgain() throws IOException {
        System.out.println("Do you want to play again? [Y/N] ");
        Scanner scan = new Scanner(System.in);
        String userInput = scan.next().toUpperCase();
        if (userInput.equals("Y")) {
            clearScreen();
            mainCaller();
        } else {
            System.exit(0);
        }
    }


    static void mainCaller() throws IOException {
        main(null);
    }


    public static String readFromFileAndChooseRandom() throws FileNotFoundException {
        ArrayList<String> fullData = new ArrayList<String>();
            File textObject = new File("/home/samurai/IdeaProjects/Hangman/countries_and_capitals");
            Scanner textReader = new Scanner(textObject);
            while (textReader.hasNextLine()) {
                String data = textReader.nextLine();
                fullData.add(data);
            }
            textReader.close();
        String countryCapital = getRandomCapital(fullData);
        return countryCapital;
    }


    public static String getRandomCapital(ArrayList<String> fullData) {
        Random randomInt = new Random();
        return fullData.get(randomInt.nextInt(fullData.size()));
    }


    public static void formatUserGameResultAndTime(String capitalToGuess, long gameTime) throws IOException {
        System.out.println("Type in your nickname: ");
        Scanner scan = new Scanner(System.in);
        String userName = scan.next();
        LocalDate localDate = LocalDate.now();
        String[] highScoreEntry = {userName, localDate.toString(), capitalToGuess, Long.toString(gameTime)};
        String joinedEntry = String.join("|", highScoreEntry);
        addUserGameResultAndTimeToFile(joinedEntry);
    }

    public static void addUserGameResultAndTimeToFile(String joinedEntry) throws IOException {
        File file = new File("userscores.txt");
        FileWriter highScores = new FileWriter(file, true);
        highScores.write(joinedEntry + System.lineSeparator());
        highScores.close();
    }


    public static void printUserScores() throws FileNotFoundException {
        ArrayList<String> highScoresList = new ArrayList<>();
            File textObject = new File("userscores.txt");
            Scanner textReader = new Scanner(textObject);
            while (textReader.hasNextLine()) {
                String data = textReader.nextLine();
                highScoresList.add(data);
            }
            textReader.close();
        clearScreen();
        printScoresLoop(highScoresList);

    }

    public static void printScoresLoop(ArrayList<String> highScoresList) {
        System.out.println("HIGH SCORES");
        if (highScoresList.size() < 10) {
            for (String element : highScoresList){
                System.out.println(element);
            }}
        else {
            for (int i=0; i<10; i++) {
                System.out.println(highScoresList.get(i));
            }
        }
    }
}


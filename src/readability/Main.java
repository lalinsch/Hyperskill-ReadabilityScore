package readability;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static int wordCount;
    public static int sentences;
    public static int characters;
    public static int syllables;
    public static int polysyllables;
    public static double ariScore;
    public static double fkScore;
    public static double smogScore;
    public static double clScore;

    public static void main(String[] args) {
        try {
            String text = Files.readString(Path.of(args[0]));
            //for testing
//            String text = "This is the front page of the Simple English Wikipedia. Wikipedias are places where people work together to write encyclopedias in different languages. We use Simple English words and grammar here. The Simple English Wikipedia is for everyone! That includes children and adults who are learning English. There are 142,262 articles on the Simple English Wikipedia. All of the pages are free to use. They have all been published under both the Creative Commons License and the GNU Free Documentation License. You can help here! You may change these pages and make new pages. Read the help pages and other good pages to learn how to write pages here. If you need help, you may ask questions at Simple talk. Use Basic English vocabulary and shorter sentences. This allows people to understand normally complex terms or phrases.";
            Scanner scan = new Scanner(System.in);
            String[] words = countWords(text);
            wordCount = words.length;
            sentences = countSentences(text);
            characters = countCharacters(text);
            syllables = totalSyllables(words);
            polysyllables = countPolysyllables(words);


            System.out.println("The text is: \n" + text);
            System.out.println("Words: " + wordCount);
            System.out.println("Sentences: " + sentences);
            System.out.println("Characters: " + characters);
            System.out.println("Syllables: " + syllables);
            System.out.println("Polysyllables: " + polysyllables);
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
            String input = scan.nextLine();
            switch (input) {
                case "ARI":
                    printARI();
                    break;
                case "FK":
                    printFK();
                    break;
                case "SMOG":
                    printSMOG();
                    break;
                case "CL":
                    printCL();
                    break;
                case "all":
                    printAll();
                    break;
            }
//            printAge(readabilityScore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String[] countWords(String text) {
        return text.split(" ");
    }

    public static int countSentences(String text) {
        return text.split("[.!?]").length;
    }

    public static int countCharacters(String text) {
        return text.replaceAll(" ", "").length();
    }

    public static int totalSyllables(String[] words) {
        int syllables = 0;
        for (String word : words) {
            syllables += countSyllables(word);
        }
        return syllables;
    }

    public static int countPolysyllables(String[] words) {
        int polysyllables = 0;
        for (String word : words) {
            if (countSyllables(word) > 2) {
                polysyllables++;
            }
        }
        return polysyllables;
    }

    public static int countSyllables(String word) {
        String regex = "([aeiouyAEIOUY]+[^e.\\s])|([aiouyAEIOUY]+\\b)|(\\b[^aeiouy0-9.']+e\\b)";
        Matcher m = Pattern.compile(regex).matcher(word.toLowerCase());
        int count = 0;
        while (m.find()) {
            count++;
        }

        return Math.max(count, 1);
    }

    public static double calculateARI() {
        double first = (double) characters / wordCount;
        double second = (double) wordCount / sentences;
        return (4.71 * first) + (0.5 * second) - 21.43;
    }

    public static double calculateFK() {
        return 0.39 * ((double) wordCount / sentences) + 11.8 * ((double) syllables / wordCount) - 15.19;
    }

    public static double calculateSMOG() {
        double sqrt = Math.sqrt(polysyllables * ((double) 30 / sentences));
        return 1.043 * sqrt + 3.1291;
    }

    public static double calculateCL() {
        double l = (double) characters / wordCount * 100;
        double s = (double) sentences / wordCount * 100;

        return 0.0588 * l - 0.296 * s - 15.8;
    }

    public static void printARI() {
        ariScore = calculateARI();
        System.out.println("Automated Readability Index: " + Math.round(ariScore * 100.0) / 100.0 + printAge(ariScore));

    }

    public static void printFK() {
        fkScore = calculateFK();
        System.out.println("Flesch-Kincaid readability tests: " + Math.round(fkScore * 100.0) / 100.0 + printAge(fkScore));
    }

    public static void printSMOG() {
        smogScore = calculateSMOG();
        System.out.println("Simple Measure of Gobbledygook: " + Math.round(smogScore * 100.0) / 100.0 + printAge(smogScore));
    }

    public static void printCL() {
        clScore = calculateCL();
        System.out.println("Coleman-Liau index: " + Math.round(clScore * 100.0) / 100.0 + printAge(clScore));
    }

    public static void printAll() {
        printARI();
        printFK();
        printSMOG();
        printCL();
        System.out.println();
        printAverageAge();
    }

    public static void printAverageAge() {
        int ageSum = getAge(ariScore) + getAge(fkScore) + getAge(smogScore) + getAge(clScore);
        double average = (double) ageSum / 4;
        System.out.println("This text should be understood in average by " + average + " year olds");
    }

    public static int getAge(double score) {

        switch ((int) Math.ceil(score)) {
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 8;
            case 4:
                return 9;
            case 5:
                return 10;
            case 6:
                return 11;
            case 7:
                return 12;
            case 8:
                return 13;
            case 9:
                return 14;
            case 10:
                return 15;
            case 11:
                return 16;
            case 12:
                return 17;
            case 13:
                return 18;
            case 14:
                return 24;
        }
        return 0;
    }

    public static String printAge(double score) {
        return " (About " + getAge(score) + " year olds)";
    }
}

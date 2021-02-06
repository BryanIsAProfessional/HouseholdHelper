package com.example.householdhelper.helpers;

import androidx.annotation.NonNull;

import java.util.Random;

/**
 * Generates a generic greeting, or a personalized greeting if given a name
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class WelcomeGenerator {
    public static final String[] GREETINGS = {"Welcome back ", "Hi ", "Hey ", "Howdy ", "Good to see you again "};
    public static final String DEFAULT_GREETING = "Welcome to Household Helper!";
    public Random rand;

    /**
     * Starts the welcome generator
     */
    public WelcomeGenerator(){
        rand = new Random();
    }

    /**
     * Returns a generic greeting as a String
     * @return Generic greeting text
     */
    public String generateWelcome(){
        return DEFAULT_GREETING;
    }

    /**
     * Returns a greeting as a String. May be given a name to personalize
     * the greeting, or empty to return the generic greeting. Name cannot be null.
     * @param name The name of the person to greet. (cannot be null)
     * @return Greeting text
     */
    public String generateWelcome(@NonNull String name){
        if(name.isEmpty()){
            return DEFAULT_GREETING;
        }
        return GREETINGS[rand.nextInt(GREETINGS.length)] + name;
    }
}

package com.example.householdhelper.helpers;

import java.util.Random;

public class WelcomeGenerator {
    public static final String[] GREETINGS = {"Welcome back ", "Hi ", "Hey ", "Howdy ", "Good to see you again "};
    public static final String DEFAULT_GREETING = "Welcome to Household Helper!";
    public Random rand;

    public WelcomeGenerator(){
        rand = new Random();
    }

    public String generateWelcome(String name){
        if(name.isEmpty()){
            return DEFAULT_GREETING;
        }
        return GREETINGS[rand.nextInt(GREETINGS.length)] + name;
    }
}

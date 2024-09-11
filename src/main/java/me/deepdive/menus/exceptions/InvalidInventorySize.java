package me.deepdive.menus.exceptions;

/**
 * The exception that's thrown when you configure a MenuInterface with an invalid inventory size
 */
public class InvalidInventorySize extends Exception{
    public InvalidInventorySize(String s){
        super(s);
    }
}